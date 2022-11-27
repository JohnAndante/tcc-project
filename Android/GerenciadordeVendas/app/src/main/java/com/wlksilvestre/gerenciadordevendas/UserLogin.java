package com.wlksilvestre.gerenciadordevendas;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserLogin extends AppCompatActivity {

    private EditText editEmailLogin;
    private EditText editSenhaLogin;
    private Button btVoltar;
    private Button btEntrar;
    private Button btEsqueciSenha;
    private Button btCriarConta;
    private Dialog loadingDialog;
    private Dialog resetPasswdDialog;

    public static final int RESETAR_SENHA = 401;

    final static int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    final static int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    private final BancoDadosCliente db = new BancoDadosCliente(this);
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fireDB;
    private Usuario usuario;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();

        initEditTexts();
        initButtons();
        initButtonsOnClick();
        initDialogCfg();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == RESETAR_SENHA) && (resultCode == RESULT_OK)) {
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "E-mail para redefinição de senha enviado", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(getColor(R.color.yellow_02));
            snackbar.setTextColor(getColor(R.color.white));
            snackbar.show();
        }
    }

    private void initEditTexts() {
        editEmailLogin = findViewById(R.id.editEmailUsuario);
        editSenhaLogin = findViewById(R.id.editSenhaUsuario);
    }

    private void initButtons () {
        btVoltar = findViewById(R.id.btLoginVoltar);
        btEntrar = findViewById(R.id.btLoginEntrar);
        btEsqueciSenha = findViewById(R.id.btEsqueciSenhaLogin);
        btCriarConta = findViewById(R.id.btCriarContaLogin);
    }

    private void initButtonsOnClick () {
        btVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(UserLogin.this, StartScreen.class);
            startActivity(intent);
        });

        btEntrar.setOnClickListener(view -> {
            if (checkfields(view))
                getDataFromLogin(view);
        });

        btEsqueciSenha.setOnClickListener(view -> {
            Intent intent = new Intent(UserLogin.this, UserPasswordRetrieval.class);
            Bundle bundle = new Bundle();

            bundle.putString("email", editEmailLogin.getText().toString());
            intent.putExtras(bundle);
            startActivityForResult(intent, RESETAR_SENHA);
        });

        btCriarConta.setOnClickListener(view -> intentCriarConta());
    }

    private void intentCriarConta () {
        Intent intent = new Intent(UserLogin.this, UserRegister.class);
        startActivity(intent);
        finish();
    }

    private void intentMainActivity () {
        Intent intent = new Intent(UserLogin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDataFromLogin (View view) {
        String email = editEmailLogin.getText().toString();
        String senha = editSenhaLogin.getText().toString();

        usuario = new Usuario();
        usuario.setEmail(email);

        try {
            Log.d("INFO TRY CATCH LOGIN", "Iniciando tentativa de autenticação de usuário" +
                    "\n " + email);

            loadingDialog.show();

            mAuth.getInstance()
                    .signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("INFO LOG", "signInWithEmailAndPassword:success");

                            Snackbar snackbar = Snackbar.make(view, "Usuário logado com sucesso!", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(getColor(R.color.green_01));
                            snackbar.setTextColor(getColor(R.color.white));
                            snackbar.show();

                            recoverUserData();

                            loadingDialog.dismiss();
                        }
                    })
                    .addOnCanceledListener(() -> {
                        loadingDialog.dismiss();

                        Log.e("INFO CANCELED LOGIN", "Canceled");
                        Snackbar snackbar = Snackbar.make(view, "Usuário não autenticado. Verifique os dados e tente novamente.", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(getColor(R.color.dark_red_01));
                        snackbar.setTextColor(getColor(R.color.white));
                        snackbar.show();
                    })
                    .addOnFailureListener(e -> {
                        String msg = "Login não realizado, revise os dados e tente novamente.";
                        if (Objects.equals(e.getMessage(), "An internal error has occurred. [ The service is currently unavailable. ]")) {
                            msg = "Erro interno. Serviço temporariamente indisponível";
                        }
                        if (Objects.equals(e.getMessage(), "The email address is already in use by another account.")) {
                            msg = "O e-mail inserido já está sendo utilizado por outra conta";
                            editEmailLogin.requestFocus();
                        }
                        if (Objects.equals(e.getMessage(), "There is no user record corresponding to this identifier. The user may have been deleted.")) {
                            msg = "E-mail não cadastrado";
                            editEmailLogin.requestFocus();
                        }
                        if (Objects.equals(e.getMessage(), "The password is invalid or the user does not have a password.")) {
                            msg = "Senha incorreta";
                            editSenhaLogin.requestFocus();
                        }
                        loadingDialog.dismiss();

                        Log.e("INFO FAILURE REGISTER", " =" + e.getMessage() + "= ");
                        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(getColor(R.color.dark_red_01));
                        snackbar.setTextColor(getColor(R.color.white));
                        snackbar.show();
                    });
        } catch (Exception e) {
            Log.e("INFO ERROR CATCH", e.getMessage());
            loadingDialog.dismiss();
        }
    }

    private boolean checkfields (View view) {
        String email = editEmailLogin.getText().toString();
        String senha = editSenhaLogin.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            // Toast.makeText(this, "Favor insira um endereço de e-mail válido", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(view, "Favor insira um endereço de e-mail válido", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(getColor(R.color.dark_red_01));
            snackbar.setTextColor(getColor(R.color.white));
            snackbar.show();
            editEmailLogin.requestFocus();
            return false;
        }
        if (senha.length() < 8) {
            //Toast.makeText(this, "Favor insira uma senha de no mínimo 8 caracteres", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(view, "Favor insira uma senha de no mínimo 8 caracteres", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(getColor(R.color.dark_red_01));
            snackbar.setTextColor(getColor(R.color.white));
            snackbar.show();
            editSenhaLogin.requestFocus();
            return false;
        }
        return true;
    }

    private void recoverUserData () {
        usuario = new Usuario();
        currentUser = mAuth.getCurrentUser();

        if (db.selectCountUsuarios() > 0) {
            usuario = db.selectUsuarioByUID(currentUser.getUid());
            Log.e("INFO recoverUserData", "usuario recuperado do banco de dados - " + usuario.getNome());


            new Handler().postDelayed(this::intentMainActivity, 500);

        } else {
            DocumentReference docUsuarios = fireDB.collection("usuarios").document(currentUser.getUid());
            docUsuarios.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists())
                                    Log.e("INFO recoverUserData", "DocumentSnapshot data: " + document.getData());

                                else
                                    Log.e("INFO recoverUserData", "No such document");
                            } else {
                                Log.e("INFO recoverUserData", "get failed with ", task.getException());
                            }
                        }
                    })
                    .addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Log.e("INFO recoverUserData", "DOCUMENT GET CANCELED");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("INFO recoverUserData", "DOCUMENT GET FAILED");
                        }
                    });
        }
    }

    private void initDialogCfg () {
        // Dialog de Loading
        loadingDialog = new Dialog(UserLogin.this);
        loadingDialog.setContentView(R.layout.spinner_loading_user_reg);

        TextView tv = loadingDialog.getWindow().findViewById(R.id.textView3);
        tv.setText("Verificando dados...");

        loadingDialog.getWindow().setLayout((int)(deviceWidth * 0.75), (int) (deviceHeight * 0.75));
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}