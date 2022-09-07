package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    int id_cliente;

    public static final int ALTERAR_CLIENTE = 102;
    public static final int RESULT_ALT_CLIENTE = 202;

    private boolean cliente_alterado;

    private Cliente cliente;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cliente);

        initTexts();
        initButtons();
        initButtonsOnClick();

        Intent intent = getIntent();
        id_cliente = 0;
        cliente_alterado = false;

        if (intent.hasExtra("ID")) {
            id_cliente = intent.getIntExtra("ID", 0);
            cliente = db.selectCliente(id_cliente);
            textNome.setText(cliente.getNome());
            textTelefone.setText(cliente.getTelefone());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Alterando dados
        if ((requestCode == ALTERAR_CLIENTE) && (resultCode == RESULT_OK)) {
            Log.i("INFO", "\n\nDados Alterados");
            cliente_alterado = true;

            cliente = db.selectCliente(id_cliente);

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
                Intent intent = new Intent(viewCliente.this, AddCliente.class);
                Bundle bundle = new Bundle();

                bundle.putInt("ID", id_cliente);
                intent.putExtras(bundle);
                startActivityForResult(intent, ALTERAR_CLIENTE);
            }
        });

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliente_alterado) {
                    Intent intent2 = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", cliente.getId());
                    intent2.putExtras(bundle);

                    setResult(RESULT_ALT_CLIENTE, intent2);
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }
}