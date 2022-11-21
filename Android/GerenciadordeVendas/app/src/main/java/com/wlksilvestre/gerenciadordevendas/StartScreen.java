package com.wlksilvestre.gerenciadordevendas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class StartScreen extends AppCompatActivity {

    private Button btFazerLogin;
    private Button btCriarConta;
    private Button btFecharApp;

    private Dialog loadingDialog;

    private final BancoDadosCliente db = new BancoDadosCliente(this);

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fireDB;
    private Usuario usuario;


    public final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireDB = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            Log.e("INFO USER 2", currentUser.getEmail());
            loadingDialog.show();
            recoverUserData();
        } else {
            Log.e("INFO USER 2", "Nenhum usuário conectado");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireDB = FirebaseFirestore.getInstance();

        initButtons();
        initButtonsOnClick();
        initDialogCfg();


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

    private void recoverUserData () {
        usuario = new Usuario();

        if (db.selectCountUsuarios() > 0) {
            usuario = db.selectUsuarioByUID(currentUser.getUid());
            Log.e("INFO recoverUserData", "usuario recuperado do banco de dados - " + usuario.getNome());


            Intent intent = new Intent(StartScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
            loadingDialog.dismiss();

        } else {
            DocumentReference docUsuarios = fireDB.collection("Usuarios").document(currentUser.getUid());
            docUsuarios.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.e("INFO recoverUserData", "DocumentSnapshot data: " + document.getData());

                                usuario.setId       (Integer.parseInt(document.get("id").toString()));
                                usuario.setNome     (document.get("nome").toString());
                                usuario.setEmail    (document.get("email").toString());
                                usuario.setTelefone (document.get("telefone").toString());
                                usuario.setUid      (document.get("uid").toString());

                                db.addUsuario(usuario);


                                Intent intent = new Intent(StartScreen.this, MainActivity.class);
                                startActivity(intent);

                                loadingDialog.dismiss();
                                finish();
                            }
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
                }
            );
        }
    }

    private void initDialogCfg () {
        loadingDialog = new Dialog(StartScreen.this);
        loadingDialog.setContentView(R.layout.spinner_loading_user_reg);
        TextView tv = loadingDialog.getWindow().findViewById(R.id.textView3);
        tv.setText("Verificando dados...");
        loadingDialog.getWindow().setLayout((int)(deviceWidth * 0.75), (int) (deviceHeight * 0.75));
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}