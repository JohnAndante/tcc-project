package com.wlksilvestre.gerenciadordevendas;

import static com.wlksilvestre.gerenciadordevendas.UserLogin.deviceHeight;
import static com.wlksilvestre.gerenciadordevendas.UserLogin.deviceWidth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UserPasswordRetrieval extends AppCompatActivity {

    private EditText editEmail;
    private Button btReset;
    private Button btCancelar;
    private AlertDialog alertDialogLoading;
    private Dialog loadingDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password_retrieval);
        Objects.requireNonNull(getSupportActionBar()).hide();


        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String email = "";

        initEditTexts();
        initButtons();
        initDialogCfg();

        if (intent.hasExtra("email")) {
            editEmail.setText(intent.getStringExtra("email"));
        }
    }

    private void initEditTexts () {
        editEmail = findViewById(R.id.editEmailUsuario);
    }

    private void initButtons () {
        btReset = findViewById(R.id.btResetResetar);
        btCancelar = findViewById(R.id.btResetCancelar);

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                if (!email.isEmpty() || email.contains("@")) {
                    try {
                        loadingDialog.show();

                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingDialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    } catch (Exception e) {
                        loadingDialog.dismiss();
                        Log.e("INFO ERROR RESET SENHA", e.getMessage());
                    }

                }
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void initDialogCfg () {
        // Dialog de Loading
        loadingDialog = new Dialog(UserPasswordRetrieval.this);
        loadingDialog.setContentView(R.layout.spinner_loading_user_reg);

        TextView tv = loadingDialog.getWindow().findViewById(R.id.textView3);
        tv.setText("Verificando dados...");

        loadingDialog.getWindow().setLayout((int)(deviceWidth * 0.75), (int) (deviceHeight * 0.75));
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


}