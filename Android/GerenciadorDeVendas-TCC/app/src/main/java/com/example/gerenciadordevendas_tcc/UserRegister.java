package com.example.gerenciadordevendas_tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initEditTexts();
        initButtons();
        initButtonsOnClick();
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
                    Toast.makeText(UserRegister.this, "Dados válidos", Toast.LENGTH_SHORT).show();
                    getDataFromRegister();
                }
            }
        });
    }

    private void intentFazerLogin () {
        Intent intent = new Intent(UserRegister.this, UserLogin.class);
        startActivity(intent);
    }

    private void getDataFromRegister () {
        String nome = editNomeUsuario.getText().toString();
        String email = editEmailUsuario.getText().toString();
        String telefone = editTelefoneUsuario.getText().toString();
        String senha = editSenhaUsuario.getText().toString();
        
    }

    private boolean checkFields () {
        String nome = editNomeUsuario.getText().toString();
        String email = editEmailUsuario.getText().toString();
        String telefone = editTelefoneUsuario.getText().toString();
        String senha = editSenhaUsuario.getText().toString();

        if (nome.isEmpty() || nome == " " || !nome.matches("[a-zA-Z ]+")) {
            Toast.makeText(this, "Favor preencha corretamente o nome de usuário", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty() || email == "" || (!email.contains("@"))) {
            Toast.makeText(this, "Favor insira um endereço de e-mail válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (telefone.length() < 8 || !telefone.matches("[0-9() ]+")) {
            Toast.makeText(this, "Favor insira um número de telefone válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (senha.length() < 8) {
            Toast.makeText(this, "Favor insira uma senha de no mínimo 8 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}