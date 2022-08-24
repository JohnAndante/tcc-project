package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    BancoDadosCliente db = new BancoDadosCliente(this);

    int id_cliente;

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

        if (intent.hasExtra("ID")) {
            Salvar.setText("Alterar");
            id_cliente = intent.getIntExtra("ID", 0);

            Cliente c = db.selectCliente(id_cliente);
            Log.i("INFO CLIENTE", "ID: " + c.getId() +
                    "\nNome: " + c.getNome() +
                    "\nTelefone: " + c.getTelefone());

            editTextNome.setText(c.getNome());
            editTextTelefone.setText(c.getTelefone());
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
                if (id_cliente > 0 && confereCampos()) {
                    bundle.putInt("id", id_cliente);
                    Cliente c = new Cliente();
                    c.setId(id_cliente);
                    c.setNome(editTextNome.getText().toString());
                    c.setTelefone(editTextTelefone.getText().toString());

                    db.updateCliente(c);
                }

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
