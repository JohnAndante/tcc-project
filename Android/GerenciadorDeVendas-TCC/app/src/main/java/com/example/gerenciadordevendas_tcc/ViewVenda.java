package com.example.gerenciadordevendas_tcc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewVenda extends AppCompatActivity {

    private Button Voltar;
    private Button NovaVenda;
    private Button EnviaComprovante;
    private Button AdicionaPgto;
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
    private ConstraintLayout clFormaPgto;

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

        Cliente cliente = new Cliente();
        Pgto pgto = new Pgto();
        Produto produto = new Produto();
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
            pgto = venda.getPgto();
            List<ProdVenda> prodsVenda = db.listProdVendaByVenda(venda);

            if (!prodsVenda.isEmpty()) {
                for (ProdVenda pv : prodsVenda)
                    arrayListProdVenda.add(pv);
            }

            // retirar SDF adicionais se não tiverem uso
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            String dataVenda = venda.getData();

            Date dateVenda = new Date();
            try {
                dateVenda = dtf.parse(dataVenda);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            textDataVenda.setText(DateCustomText.getExtenseDate(dataVenda));
            textHoraVenda.setText(DateCustomText.getCustomTime(dataVenda));

            textNomeCliente.setText(cliente.getNome());
            textTelefoneCliente.setText(cliente.getTelefone());

            textValorTotal.setText(MaskEditUtil.doubleToMoneyValue(venda.getValor()));

            clFormaPgto = (ConstraintLayout) findViewById(R.id.clFormaPgto);

            if (pgto == null || pgto.getId() < 0){
                clFormaPgto.setVisibility(View.GONE);
                AdicionaPgto.setVisibility(View.VISIBLE);
            } else {
                textPgtoVenda.setText(pgto.getFormaPgto().getDescricao());
                textQtdParcelas.setText(pgto.getParcelas());
                textValorParcela.setText(Double.valueOf(pgto.getValor()/pgto.getParcelas()).toString());
                AdicionaPgto.setVisibility(View.GONE);
            }

            adapterProdutoVenda = new AdapterProdutoVenda(this, 0, arrayListProdVenda);

            listProdutos = findViewById(R.id.lvProdutosVenda);
            listProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            listProdutos.setAdapter(adapterProdutoVenda);

            justifyListViewHeightBasedOnChildren(listProdutos);
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
        AdicionaPgto = (Button) findViewById(R.id.btAdicionarPgtoVenda);
    }

    private void initButtonsOnClick () {
        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
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

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter listadp = listView.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            for (int i = 0; i < listadp.getCount(); i++) {
                View listItem = listadp.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listadp.getCount() - 1) + 2);
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

}
