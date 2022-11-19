package com.example.gerenciadordevendas_tcc;

import static android.content.ContentValues.TAG;
import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;

public class UserRegister extends AppCompatActivity {

    private EditText editNomeUsuario;
    private EditText editEmailUsuario;
    private EditText editTelefoneUsuario;
    private EditText editSenhaUsuario;
    private Button btLogin;
    private Button btVoltar;
    private Button btCriarConta;
    //private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //mAuth = FirebaseAuth.getInstance();

        initEditTexts();
        initButtons();
        initButtonsOnClick();
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.e("INFO ", "DEU NULL AQUI LINHA 60");
        }
    }

 */

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
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentFazerLogin();
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    getDataFromRegister(view);
                }
            }
        });
    }

    private void intentFazerLogin () {
        Intent intent = new Intent(UserRegister.this, UserLogin.class);
        startActivity(intent);
    }

    private void getDataFromRegister (View view) {
        String nome = editNomeUsuario.getText().toString();
        String email = editEmailUsuario.getText().toString();
        String telefone = editTelefoneUsuario.getText().toString();
        String senha = editSenhaUsuario.getText().toString();

        try {
            Log.e("INFO TRY CATCH", "Iniciando tentativa de criação de usuário" +
                    "\n " + email + " - " + senha);
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("INFO LOG ??????///", "createUserWithEmail:success");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(UserRegister.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnCanceledListener(this, new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Log.e("INFO CANCELED LOGIN", "Canceled");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("INFO FAILURE LOGIN", e.getMessage());
                        }
                    })
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.e("INFO SUCESS LOGIN", "SUCCESS");
                        }
                    });
        } catch (Exception e) {
            Log.e("INFO ERROR CATCH", e.getMessage());
        }

        /*
        Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(getColor(R.color.light_gray_01));
                    snackbar.setTextColor(getColor(R.color.white));
                    snackbar.show();

                    Snackbar snackbar = Snackbar.make(view, "Erro, cadastro não realizado!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(getColor(R.color.light_gray_01));
                    snackbar.setTextColor(getColor(R.color.white));
                    snackbar.show();
         */

    }

    private boolean checkFields () {
        String nome = editNomeUsuario.getText().toString();
        String email = editEmailUsuario.getText().toString();
        String telefone = editTelefoneUsuario.getText().toString();
        String senha = editSenhaUsuario.getText().toString();

        if (nome.isEmpty() || nome == " " || !nome.matches("[a-zA-Z ]+")) {
            // Toast.makeText(this, "Favor preencha corretamente o nome de usuário", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Favor preencha corretamente o nome de usuário", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(getColor(R.color.light_gray_01));
            snackbar.setTextColor(getColor(R.color.white));
            snackbar.show();
            editNomeUsuario.requestFocus();
            return false;
        }
        if (email.isEmpty() || email == "" || (!email.contains("@"))) {
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
}