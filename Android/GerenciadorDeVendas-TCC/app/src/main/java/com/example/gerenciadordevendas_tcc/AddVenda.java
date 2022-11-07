package com.example.gerenciadordevendas_tcc;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddVenda extends AppCompatActivity {

    private LinearLayout llClienteVenda;
    private LinearLayout llProdutoVenda;
    private LinearLayout llProdutosAdicionados;
    private TextView tvClienteVenda;
    private TextView tvProdutoVenda;
    private TextView tvValorTotalProdutosVenda;
    private TextView tvQtdProdutosVenda;
    private EditText editQtdProdutoVenda;
    private ImageView imgLessQtdProduto;
    private ImageView imgMoreQtdProduto;
    private ListView lvProdutosAdicionados;
    private Button btAdicionarProdutoVenda;
    private Button btAvancar;
    private Button btCancelar;

    private final int deviceHeight  = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final int deviceWidth   = Resources.getSystem().getDisplayMetrics().widthPixels;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private List<Cliente> clientes;
    private ArrayList<Cliente> listaDinamicaClientes;
    private AdapterCliente adapterCliente;
    private Dialog dialogCliente;
    private ListView listViewClientes;


    private List<Produto> produtos;
    private ArrayList<Produto> listaDinamicaProdutos;
    private AdapterProduto adapterProduto;
    private Dialog dialogProduto;
    private ListView listViewProdutos;

    private List<ProdVenda> produtosSelecionados;
    private ArrayList<ProdVenda> listaDinamicaProdVenda;
    private AdapterProdutoVenda2 adapterProdVenda;

    private Cliente clienteSelecionado = null;
    private Produto produtoSelecionado = null;

    private int qtdProdutos = 0;
    private Double valorTotal = 0.00;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venda_01);

        initButtons();
        initTextViews();
        initLinearLayouts();
        initEditTexts();
        initImageViews();
        initListViews();

        Intent intent = getIntent();

        clienteDropdown();
        produtoDropdown();
    }

    private void clienteDropdown () {
        llClienteVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientes = db.listAllClientes();
                listaDinamicaClientes = new ArrayList<Cliente>();

                if (!clientes.isEmpty()) {
                    for (Cliente c : clientes)
                        listaDinamicaClientes.add(c);
                }

                dialogCliente = new Dialog(AddVenda.this);
                dialogProduto.setContentView(R.layout.activity_add_venda_01);

                adapterCliente = new AdapterCliente(dialogCliente.getContext(), 0, listaDinamicaClientes);

                dialogCliente.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
                dialogCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogCliente.show();

                TextView
            }
        });
    }

    private void initButtons () {
        btAdicionarProdutoVenda = (Button) findViewById(R.id.btAdicionarProdutoVenda);
        btAvancar = (Button) findViewById(R.id.btAvancarVenda);
        btCancelar = (Button) findViewById(R.id.btCancelarVenda);
    }

    private void initTextViews () {
        tvValorTotalProdutosVenda = (TextView) findViewById(R.id.tvValorTotalVenda);
        tvQtdProdutosVenda = (TextView) findViewById(R.id.tvQtdTotalVenda);
    }

    private void initLinearLayouts () {
        llClienteVenda = (LinearLayout) findViewById(R.id.llClienteNovaVenda);
        llProdutoVenda = (LinearLayout) findViewById(R.id.llProdutoNovaVenda);
        llProdutosAdicionados = (LinearLayout) findViewById(R.id.llProdutosAdicionadosNovaVenda);

    }

    private void initEditTexts () {
        editQtdProdutoVenda = (EditText) findViewById(R.id.editQtdProdutoVenda);
    }

    private void initImageViews () {
        imgLessQtdProduto = (ImageView) findViewById(R.id.imgLessQtdProduto);
        imgMoreQtdProduto = (ImageView) findViewById(R.id.imgMoreQtdProduto);
    }

    private void initListViews () {
        lvProdutosAdicionados = (ListView) findViewById(R.id.lvProdVendaAdicionados);
    }

}
