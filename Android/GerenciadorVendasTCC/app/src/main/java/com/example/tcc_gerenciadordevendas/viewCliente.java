package com.example.tcc_gerenciadordevendas;

import android.os.Bundle;
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

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cliente);

        initTexts();
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
}
