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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserLogin extends AppCompatActivity {

    private EditText editEmailLogin;
    private EditText editSenhaLogin;
    private Button btVoltar;
    private Button btEntrar;
    private Button btEsqueciSenha;
    private Button btCriarConta;
    private FirebaseAuth mAuth;
    private Dialog loadingDialog;
    private Usuario usuario;

    public final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.e("INFO USER", currentUser.getEmail());
            intentMainActivity();
        } else {
            Log.e("INFO USER", "Nenhum usuário conectado");
        }
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

                            new Handler().postDelayed(this::intentMainActivity, 500);

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
                    })
                    .addOnSuccessListener(authResult -> {

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

    private void initDialogCfg () {
        loadingDialog = new Dialog(UserLogin.this);
        loadingDialog.setContentView(R.layout.spinner_loading_user_reg);
        TextView tv = loadingDialog.getWindow().findViewById(R.id.textView3);
        tv.setText("Verificando dados...");
        loadingDialog.getWindow().setLayout((int)(deviceWidth * 0.75), (int) (deviceHeight * 0.75));
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}