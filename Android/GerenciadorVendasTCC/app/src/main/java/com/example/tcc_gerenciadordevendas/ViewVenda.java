package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewVenda extends AppCompatActivity {

    private Button Exportar;
    private Button NovaVenda;
    private Button Voltar;

    private TextView textData;
    private TextView textHora;
    private TextView tvNomeCliente;
    private TextView tvTelefoneCliente;
    private TextView tvValorTotal;
    private TextView tvFormaPgto;
    private TextView tvQtdParcelas;
    private TextView tvValorParcela;

    private ListView lvProdutos;
    private ConstraintLayout clFormaPgto;
    private AdapterProduto adapter;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private int idVenda;
    private Venda venda;
    private int posicao;
    private ArrayList<Produto> arrayListProdutos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venda);

        Cliente cliente = new Cliente();
        Pgto pgto = new Pgto();
        FormaPgto formaPgto = new FormaPgto();

        initTexts();
        initButtons();
        initButtonsOnClick();


        Intent intent = getIntent();
        idVenda = 0;

        if (intent.hasExtra("ID")) {
            idVenda = intent.getIntExtra("ID", 0);
            venda = db.selectVenda(idVenda);
            cliente = venda.getCliente();

            List<ProdVenda> prodVendas = db.listProdVendaByVenda(venda);

            if (!prodVendas.isEmpty()) {
                for (ProdVenda pv : prodVendas) {
                    arrayListProdutos.add(pv.getProduto());

                    listProdutos();
                }
            }

            // manipular a string de data
            textData.setText(venda.getData());
            textHora.setText(venda.getData());
            tvNomeCliente.setText(cliente.getNome());
            tvTelefoneCliente.setText(cliente.getTelefone());


            tvValorTotal.setText(venda.getValor().toString());

            if (venda.getPgto() != null){
                pgto = venda.getPgto();
                formaPgto = venda.getPgto().getFormaPgto();

                tvFormaPgto.setText(pgto.getFormaPgto().getDescricao());
                tvQtdParcelas.setText(pgto.getParcelas());
                tvValorParcela.setText(String.valueOf(pgto.getValor()/pgto.getParcelas()));

                clFormaPgto.setVisibility(View.VISIBLE);
            } else {
                clFormaPgto.setVisibility(View.GONE);
            }
        }
    }

    private void initTexts () {

        textData = findViewById(R.id.textDataVenda);
        textHora = findViewById(R.id.textHoraVenda);
        tvNomeCliente = findViewById(R.id.tvNomeCliente);
        tvTelefoneCliente = findViewById(R.id.tvTelefoneCliente);
        tvValorTotal = findViewById(R.id.tvValorTotalVenda);
        tvFormaPgto = findViewById(R.id.tvFormaDePgto);
        tvQtdParcelas = findViewById(R.id.tvQtdParcela);
        tvValorParcela = findViewById(R.id.tvValorParcela);

        clFormaPgto = findViewById(R.id.clFormaPgto);
        lvProdutos = findViewById(R.id.lvProdutosVenda);

    }

    private void initButtons () {

        Exportar = findViewById(R.id.btExportarComprovante);
        NovaVenda = findViewById(R.id.btNovaVenda);
        Voltar = findViewById(R.id.btPgtoVoltar);
    }

    private void initButtonsOnClick () {
        Exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ERROR INFO //", " ///// Botão de Exportar Clicado");
            }
        });

        NovaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewVenda.this, AddVenda.class);
                startActivity(intent);
            }
        });

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
            }
        });

    }

    private void listProdutos () {

        adapter = new AdapterProduto(this, 0, arrayListProdutos);

        lvProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        lvProdutos.setAdapter(adapter);
        lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("ERROR", "item da lista de produtos clicado");
            }
        });
    }
}
