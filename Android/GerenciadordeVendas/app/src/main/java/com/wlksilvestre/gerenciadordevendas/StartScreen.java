package com.wlksilvestre.gerenciadordevendas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class StartScreen extends AppCompatActivity {

    private Button btFazerLogin;
    private Button btCriarConta;
    private Button btFecharApp;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
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
        setContentView(R.layout.activity_start_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initButtons();
        initButtonsOnClick();
    }

    private void initButtons () {
        btFazerLogin    = findViewById(R.id.btLoginStart);
        btCriarConta    = findViewById(R.id.btRegisterStart);
        btFecharApp     = findViewById(R.id.btSairStart);
    }


    private void initButtonsOnClick () {
        btFazerLogin.setOnClickListener(view -> {
            intentFazerLogin();
        });

        btCriarConta.setOnClickListener(view -> {
            intentCriarConta();
        });

        btFecharApp.setOnClickListener(view -> {
            finish();
            System.exit(0);
        });
    }

    private void intentCriarConta () {
        Intent intent = new Intent(StartScreen.this, UserRegister.class);
        startActivity(intent);
        finish();
    }

    private void intentFazerLogin () {
        Intent intent = new Intent(StartScreen.this, UserLogin.class);
        startActivity(intent);
        finish();
    }

    private void intentMainActivity () {
        Intent intent = new Intent(StartScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}