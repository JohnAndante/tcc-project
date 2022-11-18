package com.example.gerenciadordevendas_tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class UserLogin extends AppCompatActivity {

    private EditText editEmail;
    private EditText editSenha;
    private Button btVoltar;
    private Button btEntrar;
    private Button btEsqueciSenha;
    private Button btCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initEditTexts();
        initButtons();
        initButtonsOnClick();
    }

    private void initEditTexts() {
        editEmail = findViewById(R.id.editEmailUsuario);
        editSenha = findViewById(R.id.editSenhaUsuario);
    }

    private void initButtons () {
        btVoltar = findViewById(R.id.btLoginVoltar);
        btEntrar = findViewById(R.id.btLoginEntrar);
        btEsqueciSenha = findViewById(R.id.btEsqueciSenhaLogin);
        btCriarConta = findViewById(R.id.btCriarContaLogin);
    }

    private void initButtonsOnClick () {
        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCriarConta();
            }
        });
    }

    private void intentCriarConta () {
        Intent intent = new Intent(UserLogin.this, UserRegister.class);
        startActivity(intent);
    }

    private void getDataFromLogin () {

    }

}