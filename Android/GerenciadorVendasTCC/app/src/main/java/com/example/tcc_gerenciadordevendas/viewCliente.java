package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class viewCliente extends AppCompatActivity {

    private Button Editar;
    private Button Voltar;

    private TextView textNome;
    private TextView textTelefone;
    private TextView textRua;
    private TextView textNum;
    private TextView textCompl;
    private TextView textBairro;
    private TextView textUf;
    private TextView textCidade;

    BancoDadosCliente db = new BancoDadosCliente(this);

    int posicao;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cliente);

        initTexts();
        initButtons();
        initButtonsOnClick();

        Intent intent = getIntent();
        posicao = 0;

        if (intent.hasExtra("posicao")) {
            posicao = intent.getIntExtra("posicao", 0);
            Cliente cliente = db.selectCliente(posicao);

            textNome.setText(cliente.getNome());
            textTelefone.setText(cliente.getTelefone());
        }
    }

    private void initTexts () {
        textNome     = (TextView) findViewById(R.id.tvNomeCliente);
        textTelefone = (TextView) findViewById(R.id.tvTelefoneCliente);
        textRua      = (TextView) findViewById(R.id.tvRuaEndereco);
        textNum      = (TextView) findViewById(R.id.tvNumEndereco);
        textCompl    = (TextView) findViewById(R.id.tvComplEndereco);
        textBairro   = (TextView) findViewById(R.id.tvBairroEndereco);
        textUf       = (TextView) findViewById(R.id.tvUfEndereco);
        textCidade   = (TextView) findViewById(R.id.tvCidadeEndereco);
    }

    private void initButtons () {
        Editar = (Button) findViewById(R.id.btClienteEditar);
        Voltar = (Button) findViewById(R.id.btClienteVoltar);

    }

    private void initButtonsOnClick () {
        Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
