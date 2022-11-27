package com.wlksilvestre.gerenciadordevendas;

import static android.content.ContentValues.TAG;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserRegister extends AppCompatActivity {

    private EditText editNomeUsuario;
    private EditText editEmailUsuario;
    private EditText editTelefoneUsuario;
    private EditText editSenhaUsuario;
    private Button btLogin;
    private Button btVoltar;
    private Button btCriarConta;
    private FirebaseAuth mAuth;
    private Dialog loadingDialog;
    private Usuario usuario;
    FirebaseUser currentUser;

    BancoDadosCliente db = new BancoDadosCliente(this);

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
        } else {
            Log.e("INFO USER", "Nenhum usuário conectado");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();

        initEditTexts();
        initButtons();
        initButtonsOnClick();
        initDialogCfg();
    }

    private void initEditTexts () {
        editNomeUsuario = findViewById(R.id.editNomeUsuario);
        editEmailUsuario = findViewById(R.id.editEmailUsuario);
        editTelefoneUsuario = findViewById(R.id.editTelefoneUsuario);
        editSenhaUsuario = findViewById(R.id.editSenhaUsuario);
    }

    private void initButtons () {
        btLogin = findViewById(R.id.btLogin);
        btVoltar = findViewById(R.id.btRegisterVoltar);
        btCriarConta = findViewById(R.id.btRegisterCriar);
    }

    private void initButtonsOnClick () {
        btLogin.setOnClickListener(view -> intentFazerLogin());

        btVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(UserRegister.this, StartScreen.class);
            startActivity(intent);
        });

        btCriarConta.setOnClickListener(view -> {
            if (checkFields()) {
                getDataFromRegister(view);
            }
        });
    }

    private void intentFazerLogin () {
        Intent intent = new Intent(UserRegister.this, UserLogin.class);
        startActivity(intent);
        finish();
    }

    private void intentMainActivity () {
        Intent intent = new Intent(UserRegister.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDataFromRegister (View view) {
        String nome = editNomeUsuario.getText().toString();
        String email = editEmailUsuario.getText().toString();
        String telefone = editTelefoneUsuario.getText().toString();
        String senha = editSenhaUsuario.getText().toString();

        usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);

        try {
            Log.e("INFO TRY CATCH REGISTER", "Iniciando tentativa de criação de usuário" +
                    "\n " + email + " - " + senha);

            loadingDialog.show();

            mAuth.getInstance()
                    .createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("INFO LOG REGISTER", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getInstance().getCurrentUser();

                            if (user != null) {
                                usuario.setUid(user.getUid());
                                db.addUsuario(usuario);

                                usuario = db.selectMaxUsuario();
                                saveUserData(usuario);
                            }

                            loadingDialog.dismiss();

                            Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(getColor(R.color.green_01));
                            snackbar.setTextColor(getColor(R.color.white));
                            snackbar.show();

                            new Handler().postDelayed(this::intentMainActivity, 500);

                        } else {
                            String msg;

                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthWeakPasswordException e) {
                                msg = "Digite uma senha com no mínimo 8 caracteres.";
                            } catch (FirebaseAuthUserCollisionException e) {
                                msg = "Essa conta já foi cadastrada";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                msg = "E-mail inválido";
                            } catch (Exception e) {
                                msg = "Cadastro não realizado. Erro ao cadastrar usuário";
                            }

                            loadingDialog.dismiss();

                            Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(getColor(R.color.dark_red_01));
                            snackbar.setTextColor(getColor(R.color.white));
                            snackbar.show();

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    })
                    .addOnCanceledListener(this, () -> {
                        loadingDialog.dismiss();

                        Log.e("INFO CANCELED REGISTER", "Canceled");
                            Snackbar snackbar = Snackbar.make(view, "Cadastro não realizado, revise os dados e tente novamente.", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(getColor(R.color.dark_red_01));
                            snackbar.setTextColor(getColor(R.color.white));
                            snackbar.show();
                    })
                    .addOnFailureListener(this, e -> {
                        String msg = "Cadastro não realizado, revise os dados e tente novamente.";
                        if (Objects.equals(e.getMessage(), "An internal error has occurred. [ The service is currently unavailable. ]")) {
                            msg = "Erro interno. Serviço temporariamente indisponível.";
                        }
                        if (Objects.equals(e.getMessage(), "The email address is already in use by another account.")) {
                            msg = "O e-mail inserido já está sendo utilizado por outra conta.";
                            editEmailUsuario.requestFocus();
                        }
                        loadingDialog.dismiss();

                        Log.e("INFO FAILURE REGISTER", " =" + e.getMessage() + "= ");
                        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(getColor(R.color.dark_red_01));
                        snackbar.setTextColor(getColor(R.color.white));
                        snackbar.show();
                    })
                    .addOnSuccessListener(this, authResult -> {
                        Log.e("INFO SUCESS REGISTER", "SUCCESS");
                        FirebaseUser user = mAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            usuario.setUid(user.getUid());
                            db.addUsuario(usuario);
                            saveUserData(usuario);
                        }

                        Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(getColor(R.color.green_01));
                        snackbar.setTextColor(getColor(R.color.white));
                        snackbar.show();

                        new Handler().postDelayed(this::intentMainActivity, 3000);

                        loadingDialog.dismiss();
                    });
        } catch (Exception e) {
            loadingDialog.dismiss();

            Log.e("INFO ERROR CATCH", e.getMessage());
        }
    }

    private void saveUserData(@NonNull Usuario usuario) {
        FirebaseFirestore fireDB = FirebaseFirestore.getInstance();

        Map<String, Object> values = new HashMap<>();
        values.put("id", usuario.getId());
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("telefone", usuario.getTelefone());
        values.put("uid", usuario.getUid());

        DocumentReference dr = fireDB.collection("usuarios").document(String.valueOf(usuario.getUid()));
        dr.set(values)
                .addOnSuccessListener(unused -> Log.d("INFO FIREDB SUCCESS", "Sucesso ao salvar dados"))
                .addOnFailureListener(e -> Log.d("INFO FIREDB FAILURE", "Erro ao salvar dados \n" + e));
    }

    private boolean checkFields () {
        String nome = editNomeUsuario.getText().toString();
        String email = editEmailUsuario.getText().toString();
        String telefone = editTelefoneUsuario.getText().toString();
        String senha = editSenhaUsuario.getText().toString();

        if (nome.isEmpty() || nome.equals(" ") || nome.matches("[0-9]+[$%|_*!]+[$%|_*!]+[$%|_*!]")) {
            // Toast.makeText(this, "Favor preencha corretamente o nome de usuário", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Favor preencha corretamente o nome de usuário", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(getColor(R.color.light_gray_01));
            snackbar.setTextColor(getColor(R.color.white));
            snackbar.show();
            editNomeUsuario.requestFocus();
            return false;
        }
        if (email.isEmpty() || !email.contains("@")) {
            // Toast.makeText(this, "Favor insira um endereço de e-mail válido", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Favor insira um endereço de e-mail válido", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(getColor(R.color.light_gray_01));
            snackbar.setTextColor(getColor(R.color.white));
            snackbar.show();
            editEmailUsuario.requestFocus();
            return false;
        }
        if (telefone.length() < 8 || !telefone.matches("[0-9() ]+")) {
            // Toast.makeText(this, "Favor insira um número de telefone válido", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Favor insira um número de telefone válido", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(getColor(R.color.light_gray_01));
            snackbar.setTextColor(getColor(R.color.white));
            snackbar.show();
            editTelefoneUsuario.requestFocus();
            return false;
        }
        if (senha.length() < 8) {
            //Toast.makeText(this, "Favor insira uma senha de no mínimo 8 caracteres", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Favor insira uma senha de no mínimo 8 caracteres", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(getColor(R.color.light_gray_01));
            snackbar.setTextColor(getColor(R.color.white));
            snackbar.show();
            editSenhaUsuario.requestFocus();
            return false;
        }
        return true;
    }

    private void initDialogCfg () {
        loadingDialog = new Dialog(UserRegister.this);
        loadingDialog.setContentView(R.layout.spinner_loading_user_reg);
        TextView tv = loadingDialog.getWindow().findViewById(R.id.textView3);
        tv.setText("Cadastrando dados do usuário");
        loadingDialog.getWindow().setLayout((int)(deviceWidth * 0.75), (int) (deviceHeight * 0.75));
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}