package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddCliente extends AppCompatActivity {

    private Button Salvar;
    private Button Cancelar;

    private EditText editTextNome;
    private EditText editTextTelefone;
    private EditText editTextRua;
    private EditText editTextNum;
    private EditText editTextCompl;
    private EditText editTextBairro;
    private EditText editTextUf;
    private EditText editTextCidade;

    private LinearLayout llButtons;

    int posicao;

    private Boolean buttonsVisibility = true;
    private Boolean isEditTextFocused = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);

        llButtons = (LinearLayout) findViewById(R.id.llBotoesCliente);

        initButtonsCfg();
        initButtonsOnclick();
        initEditTexts();
        changellButtons();
        initEditOnFocus();

        Intent intent = getIntent();

        if (intent.hasExtra("posicao")) {
            Salvar.setText("Alterar");
            posicao = intent.getIntExtra("posicao", 0);

            editTextNome.setText(intent.getStringExtra("nome"));
            editTextTelefone.setText(intent.getStringExtra("telefone"));
        }

    }

    private void initButtonsCfg(){
        Salvar   = (Button) findViewById(R.id.btClienteSalvar);
        Cancelar = (Button) findViewById(R.id.btClienteCancelar);
    }

    private void initButtonsOnclick(){
        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if (posicao > 0)
                    bundle.putInt("posicao", posicao);

                if (confereCampos()){
                    bundle.putString("nome", editTextNome.getText().toString());
                    bundle.putString("telefone", editTextTelefone.getText().toString());

                    Intent intent = new Intent();
                    intent.putExtras(bundle);

                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private boolean confereCampos(){
        String clienteNome      = editTextNome.getText().toString();
        String clienteTelefone  = editTextTelefone.getText().toString();

        if (clienteNome.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Favor preencher Nome", Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (clienteTelefone.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Favor preencher Telefone!", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private void initEditTexts(){
        editTextNome        = (EditText) findViewById(R.id.editNomeCliente);
        editTextTelefone    = (EditText) findViewById(R.id.editTelefoneCliente);
        editTextRua         = (EditText) findViewById(R.id.editRuaEndereco);
        editTextNum         = (EditText) findViewById(R.id.editNumEndereco);
        editTextCompl       = (EditText) findViewById(R.id.editComplEndereco);
        editTextBairro      = (EditText) findViewById(R.id.editBairroEndereco);
        editTextUf          = (EditText) findViewById(R.id.editUfEndereco);
        editTextCidade      = (EditText) findViewById(R.id.editCidadeEndereco);
    }

    private void initEditOnFocus(){
        editTextNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            //    if (editTextNome.isFocused() && )
                changellButtons();
            }
        });

        editTextTelefone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextRua.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextCompl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextUf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextCidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });
    }

    private void changellButtons(){
       /*
        if (    !editTextNome.isFocused()       &&
                !editTextTelefone.isFocused()   &&
                !editTextRua.isFocused()        &&
                !editTextNum.isFocused()        &&
                !editTextBairro.isFocused()     &&
                !editTextCompl.isFocused()      &&
                !editTextUf.isFocused()         &&
                !editTextCidade.isFocused())
            llButtons.setVisibility(View.VISIBLE);
        else
            llButtons.setVisibility(View.GONE);

        */
    }
}
