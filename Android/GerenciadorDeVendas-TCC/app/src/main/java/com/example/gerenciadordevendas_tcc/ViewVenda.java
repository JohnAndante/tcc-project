package com.example.gerenciadordevendas_tcc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewVenda extends AppCompatActivity {

    private Button Voltar;
    private Button NovaVenda;
    private Button EnviaComprovante;
    private TextView textNomeCliente;
    private TextView textTelefoneCliente;
    private TextView textDataVenda;
    private TextView textHoraVenda;
    private TextView textValorTotal;
    private TextView textFormaPgto;
    private TextView textPgtoVenda;
    private TextView textQtdParcelas;
    private TextView textValorParcela;
    private ListView listProdutos;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private int idVenda;
    private Venda venda;
    private List<Produto> produtos;
    private ArrayList<Produto> arrayListProdutos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venda);

        Cliente cliente = new Cliente();
        Pgto pgto = new Pgto();
        Produto produto = new Produto();
        arrayListProdutos = new ArrayList<Produto>();

        initTextViews();
        initButtons();
        initButtonsOnClick();

        Intent intent = getIntent();
        idVenda = 0;

        if (intent.hasExtra("ID")) {
            idVenda = intent.getIntExtra("ID", 0);

            venda = db.selectVenda(idVenda);
            cliente = venda.getCliente();
            pgto = venda.getPgto();
            List<ProdVenda> prodsVenda = db.listProdVendaByVenda(venda);

            if (!prodsVenda.isEmpty()) {
                for (ProdVenda pv : prodsVenda)
                    arrayListProdutos.add(pv.getProduto());
            }

            textNomeCliente.setText(cliente.getNome());
            textTelefoneCliente.setText(cliente.getTelefone());
            textDataVenda.setText("somedate");
        }
    }

    private void initTextViews () {
        textNomeCliente = (TextView) findViewById(R.id.tvNomeClienteVenda);
        textTelefoneCliente = (TextView) findViewById(R.id.tvTelefoneClienteVenda);
        textDataVenda = (TextView) findViewById(R.id.textDataVenda);
        textHoraVenda = (TextView) findViewById(R.id.textHoraVenda);
        textValorTotal = (TextView) findViewById(R.id.tvValorTotalVenda);
        textFormaPgto = (TextView) findViewById(R.id.textFormaPgto);
        textPgtoVenda = (TextView) findViewById(R.id.tvFormaPgtoVenda);
        textQtdParcelas = (TextView) findViewById(R.id.tvQtdParcela);
        textValorParcela = (TextView) findViewById(R.id.tvValorParcela);
    }

    private void initButtons () {
        Voltar = (Button) findViewById(R.id.btViewVoltar);
        NovaVenda = (Button) findViewById(R.id.btViewNovaVenda);
        EnviaComprovante = (Button) findViewById(R.id.btExportarComprovante);
    }

    private void initButtonsOnClick () {
        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        NovaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        EnviaComprovante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
