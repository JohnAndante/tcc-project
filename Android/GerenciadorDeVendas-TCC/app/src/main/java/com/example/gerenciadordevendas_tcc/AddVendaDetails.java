package com.example.gerenciadordevendas_tcc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddVendaDetails extends AppCompatActivity {

    private Button btSalvar;
    private Button btVoltar;
    private TextView tvDataVenda;
    private TextView tvHoraVenda;
    private TextView tvNomeCliente;
    private TextView tvTelefoneCliente;
    private TextView tvValorTotalVenda;
    private TextView tvFormaPgto;
    private TextView tvTituloParcelas;
    private EditText editParcelas;
    private ListView lvProdutosVenda;
    private LinearLayout llParcelas;
    private ImageView imgLessParcela;
    private ImageView imgMoreParcela;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private ArrayList<ProdVenda> arrayListProdVenda;
    private ArrayList<FormaPgto> listaDinamicaFormaPgto;
    private AdapterProdutoVenda adapterProdutoVenda;
    private int idVenda;
    private Venda venda;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venda_02);

        Cliente cliente = new Cliente();
        arrayListProdVenda = new ArrayList<ProdVenda>();

        initTextViews();
        initButtons();
        initEditTexts();
        initListViews();
        initLinearLayouts();
        initImageViews();

        Intent intent = getIntent();
        idVenda = 0;

        if (intent.hasExtra("ID")) {
            idVenda = intent.getIntExtra("ID", 0);

            venda = db.selectVenda(idVenda);
            cliente = venda.getCliente();
            List<ProdVenda> prodVendaList = db.listProdVendaByVenda(venda);

            if (!prodVendaList.isEmpty()) {
                for (ProdVenda pv : prodVendaList)
                    arrayListProdVenda.add(pv);
            }

            tvDataVenda.setText(DateCustomText.getExtenseDate(venda.getData()));
            tvHoraVenda.setText(DateCustomText.getCustomTime(venda.getData()));
            tvNomeCliente.setText(cliente.getNome());
            tvTelefoneCliente.setText(cliente.getTelefone());
            tvValorTotalVenda.setText(MaskEditUtil.doubleToMoneyValue(venda.getValor()));

            tvTituloParcelas.setVisibility(View.GONE);
            llParcelas.setVisibility(View.GONE);

            adapterProdutoVenda = new AdapterProdutoVenda(this, 0, arrayListProdVenda);
            lvProdutosVenda.setAdapter(adapterProdutoVenda);

            justifyListViewHeightBasedOnChildren(lvProdutosVenda);

            initButtonsOnClick();
            initImageViewsOnClick();
            dropdownFormaPgto();
        }
    }

    private void initTextViews () {
        tvDataVenda         = findViewById(R.id.textDataNovaVenda);
        tvHoraVenda         = findViewById(R.id.textHoraNovaVenda);
        tvNomeCliente       = findViewById(R.id.tvNomeClienteNovaVenda);
        tvTelefoneCliente   = findViewById(R.id.tvTelefoneClienteVenda);
        tvValorTotalVenda   = findViewById(R.id.tvValorTotalVenda);
        tvFormaPgto         = findViewById(R.id.tvFormaPgtoOpcao);
        tvTituloParcelas    = findViewById(R.id.textParcelas);
    }

    private void initEditTexts () {
        editParcelas = findViewById(R.id.tvQtdParcelasVenda);
    }

    private void initButtons () {
        btSalvar = findViewById(R.id.btViewSalvar);
        btVoltar = findViewById(R.id.btViewVoltar);
    }

    private void initListViews () {
        lvProdutosVenda = findViewById(R.id.lvProdutosNovaVenda);
    }

    private void initLinearLayouts () {
        llParcelas = findViewById(R.id.llParcelas);
    }

    private void initImageViews () {
        imgLessParcela = findViewById(R.id.imgLessQtdParcela);
        imgMoreParcela = findViewById(R.id.imgMoreQtdParcela);
    }

    private void justifyListViewHeightBasedOnChildren (ListView listView) {
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

    private void initButtonsOnClick () {
        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initImageViewsOnClick () {
        imgLessParcela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imgLessParcela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void dropdownFormaPgto () {
        tvFormaPgto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FormaPgto> formaPgtos = db.listFormaPgtoOrdered();
                listaDinamicaFormaPgto = new ArrayList<FormaPgto>();
            }
        });
    }


}
