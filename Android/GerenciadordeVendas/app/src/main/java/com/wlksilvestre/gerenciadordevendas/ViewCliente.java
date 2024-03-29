package com.wlksilvestre.gerenciadordevendas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ViewCliente extends AppCompatActivity {

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
    private ImageView imgWhatsapp;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private int id_cliente;

    public static final int ALTERAR_CLIENTE = 102;
    public static final int RESULT_ALT_CLIENTE = 202;

    private boolean cliente_alterado;

    private Cliente cliente;
    private Telefone telefone;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cliente);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initTexts();
        initButtons();
        initButtonsOnClick();

        Intent intent = getIntent();
        id_cliente = 0;
        cliente_alterado = false;

        if (intent.hasExtra("ID")) {
            id_cliente = intent.getIntExtra("ID", 0);
            cliente = db.selectCliente(id_cliente);
            telefone = db.selectTelefoneFirst(cliente);

            textNome.setText(cliente.getNome());
            textTelefone.setText(telefone.getNum());

            Endereco e = db.selectEnderecoByCliente(cliente);

            if (e != null) {
                textRua.setText(e.getRua());
                textNum.setText(e.getNum());
                textCompl.setText(e.getComp());
                textBairro.setText(e.getBairro());

                Estado estado  = e.getCidade().getEstado();
                Cidade cidade = e.getCidade();

                textUf.setText(estado.getNome());
                textCidade.setText(cidade.getNome());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Alterando dados
        if ((requestCode == ALTERAR_CLIENTE) && (resultCode == RESULT_OK)) {
            cliente_alterado = true;

            cliente = db.selectCliente(id_cliente);
            telefone = db.selectTelefoneFirst(cliente);

            textNome.setText(cliente.getNome());
            textTelefone.setText(telefone.getNum());

            Endereco e = db.selectEnderecoByCliente(cliente);

            if (e != null) {
                textRua.setText(e.getRua());
                textNum.setText(e.getNum());
                textCompl.setText(e.getComp());
                textBairro.setText(e.getBairro());

                Estado estado = e.getCidade().getEstado();

                textUf.setText(estado.getNome());
                textCidade.setText(estado.getNome());
            }

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
        imgWhatsapp  = (ImageView) findViewById(R.id.imgWhatsapp);

        textTelefone.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + telefone.getNum().replaceAll("[^0-9]+", "")));
            startActivity(intent);
        });

        imgWhatsapp.setOnClickListener(view -> {
            String a = String.valueOf(telefone.getNum().replaceAll("[^0-9]+", "").charAt(0));
            String b = String.valueOf(telefone.getNum().replaceAll("[^0-9]+", "").charAt(1));
            String url;
            if ( a.equals("5") && b.equals("5")) {
                url = "https://api.whatsapp.com/send?phone=" + telefone.getNum().replaceAll("[^0-9]+", "");
            } else {
                url = "https://api.whatsapp.com/send?phone=" + "55" + telefone.getNum().replaceAll("[^0-9]+", "");
            }
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

    }

    private void initButtons () {
        Editar = (Button) findViewById(R.id.btClienteEditar);
        Voltar = (Button) findViewById(R.id.btClienteVoltar);
    }

    private void initButtonsOnClick () {
        Editar.setOnClickListener(view -> {
            Intent intent = new Intent(ViewCliente.this, AddCliente.class);
            Bundle bundle = new Bundle();

            bundle.putInt("ID", id_cliente);
            intent.putExtras(bundle);
            startActivityForResult(intent, ALTERAR_CLIENTE);
        });

        Voltar.setOnClickListener(view -> {
            if (cliente_alterado) {
                Intent intent2 = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("ID", cliente.getId());
                intent2.putExtras(bundle);

                setResult(RESULT_ALT_CLIENTE, intent2);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        });
    }
}

