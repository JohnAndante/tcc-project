package com.wlksilvestre.gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewVenda extends AppCompatActivity {

    private Button Voltar;
    private Button NovaVenda;
    private Button EnviaComprovante;
    private Button AdicionaPgto;
    private TextView textNomeCliente;
    private TextView textTelefoneCliente;
    private TextView textDataVenda;
    private TextView textHoraVenda;
    private TextView textValorVenda;
    private TextView textFormaPgto;
    private TextView textPgtoVenda;
    private TextView textQtdParcelas;
    private TextView textValorJuros;
    private TextView textValorTotalVenda;
    private ListView listProdutos;
    private ConstraintLayout clFormaPgto;
    private ConstraintLayout clJurosAplicados;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private int idVenda;
    private Venda venda;
    private List<Produto> produtos;
    private ArrayList<ProdVenda> arrayListProdVenda;
    private AdapterProdutoVenda adapterProdutoVenda;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venda);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Cliente cliente = new Cliente();
        Telefone telefone = new Telefone();
        Pgto pgto = new Pgto();
        arrayListProdVenda = new ArrayList<ProdVenda>();

        initTextViews();
        initButtons();
        initButtonsOnClick();

        Intent intent = getIntent();
        idVenda = 0;

        if (intent.hasExtra("ID")) {
            idVenda = intent.getIntExtra("ID", 0);

            venda = db.selectVenda(idVenda);
            cliente = venda.getCliente();
            telefone = db.selectTelefoneFirst(cliente);
            pgto = venda.getPgto();
            List<ProdVenda> prodsVenda = db.listProdVendaByVenda(venda);

            if (!prodsVenda.isEmpty()) {
                for (ProdVenda pv : prodsVenda)
                    arrayListProdVenda.add(pv);
            }

            String dataVenda = venda.getData();

            textDataVenda.setText(DateCustomText.getExtenseDate(dataVenda));
            textHoraVenda.setText(DateCustomText.getCustomTime(dataVenda));

            textNomeCliente.setText(cliente.getNome());
            textTelefoneCliente.setText(telefone.getNum());

            textValorVenda.setText(MaskEditUtil.doubleToMoneyValue(venda.getValor()));

            clFormaPgto         = (ConstraintLayout) findViewById(R.id.clFormaPgto);
            clJurosAplicados    = (ConstraintLayout) findViewById(R.id.clJurosAplicados);

            if (pgto == null || pgto.getId() < 0){
                clFormaPgto.setVisibility(View.GONE);
                AdicionaPgto.setVisibility(View.VISIBLE);
            } else {
                clFormaPgto.setVisibility(View.VISIBLE);
                textPgtoVenda.setText(pgto.getFormaPgto().getDescricao());
                textQtdParcelas.setText(Integer.toString(pgto.getParcelas()));
                textValorTotalVenda.setText(MaskEditUtil.doubleToMoneyValue(pgto.getValor()));
                if (pgto.getJuros() != 0.00) {
                    clJurosAplicados.setVisibility(View.VISIBLE);
                    textValorJuros.setText(MaskEditUtil.doubleToMoneyValue(pgto.getJuros()));
                } else {
                    clJurosAplicados.setVisibility(View.GONE);
                }
                AdicionaPgto.setVisibility(View.GONE);
            }

            adapterProdutoVenda = new AdapterProdutoVenda(this, 0, arrayListProdVenda);

            listProdutos = findViewById(R.id.lvProdutosVenda);
            listProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            listProdutos.setAdapter(adapterProdutoVenda);
        }
    }

    private void initTextViews () {
        textNomeCliente     = (TextView) findViewById(R.id.tvNomeClienteVenda);
        textTelefoneCliente = (TextView) findViewById(R.id.tvTelefoneClienteVenda);
        textDataVenda       = (TextView) findViewById(R.id.textDataVenda);
        textHoraVenda       = (TextView) findViewById(R.id.textHoraVenda);
        textValorVenda      = (TextView) findViewById(R.id.tvValorVenda);
        textFormaPgto       = (TextView) findViewById(R.id.textLabelFormaPgto);
        textPgtoVenda       = (TextView) findViewById(R.id.tvFormaPgtoDesc);
        textQtdParcelas     = (TextView) findViewById(R.id.tvQtdParcela);
        textValorJuros      = (TextView) findViewById(R.id.tvValorJurosAplicados);
        textValorTotalVenda = (TextView) findViewById(R.id.tvValorTotalComJuros);
    }

    private void initButtons () {
        Voltar = (Button) findViewById(R.id.btViewVoltar);
        NovaVenda = (Button) findViewById(R.id.btViewNovaVenda);
        EnviaComprovante = (Button) findViewById(R.id.btExportarComprovante);
        AdicionaPgto = (Button) findViewById(R.id.btAdicionarPgtoVenda);
    }

    private void initButtonsOnClick () {
        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewVenda.this, ListVendas.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        NovaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewVenda.this, AddVenda.class);
                startActivity(intent);
            }
        });

        EnviaComprovante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        AdicionaPgto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPgtoActivity();
            }
        });
    }

    private void startPgtoActivity () {
        Intent intent = new Intent(ViewVenda.this, AddVendaDetails.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", venda.getId());
        intent.putExtras(bundle);

        startActivity(intent);
    }

}
