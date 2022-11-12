package com.example.gerenciadordevendas_tcc;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText editValorProdutoVenda;
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

    private List<ProdVenda> prodVendaSelecionados;
    private ArrayList<ProdVenda> listaDinamicaProdVenda;
    private AdapterProdutoVenda2 adapterProdutoVenda2;

    private Cliente clienteSelecionado = null;
    private Produto produtoSelecionado = null;
    private Venda vendaRascunho = null;

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

        editQtdProdutoVenda.setText(String.valueOf(0));
        editValorProdutoVenda.addTextChangedListener(new MoneyTextWatcher(editValorProdutoVenda));

        listaDinamicaProdVenda = new ArrayList<ProdVenda>();
        adapterProdutoVenda2 = new AdapterProdutoVenda2(this, 0, listaDinamicaProdVenda);

        lvProdutosAdicionados.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        lvProdutosAdicionados.setAdapter(adapterProdutoVenda2);

        justifyListViewHeightBasedOnChildren(lvProdutosAdicionados);
    }

    private void clienteDropdown () {
        tvClienteVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientes = db.listAllClientes();
                listaDinamicaClientes = new ArrayList<Cliente>();

                if (!clientes.isEmpty()) {
                    for (Cliente c : clientes)
                        listaDinamicaClientes.add(c);
                }

                dialogCliente = new Dialog(AddVenda.this);
                dialogCliente.setContentView(R.layout.spinner_cliente);

                adapterCliente = new AdapterCliente(dialogCliente.getContext(), 0, listaDinamicaClientes);

                dialogCliente.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
                dialogCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogCliente.show();

                TextView tvCliente = dialogCliente.findViewById(R.id.tvSpinnerCliente);
                EditText editCliente = dialogCliente.findViewById(R.id.editTextSpinnerCliente);

                listViewClientes = (ListView) dialogCliente.findViewById(R.id.lvSpinnerCliente);
                listViewClientes.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewClientes.setAdapter(adapterCliente);

                editCliente.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        atualizaListaClientes(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            Cliente c = (Cliente) listViewClientes.getItemAtPosition(i);
                            tvClienteVenda.setText(c.getNome());
                            if (clienteSelecionado != c)
                                clienteSelecionado = c;
                            dialogCliente.dismiss();
                            criaVenda();
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage().toString());
                        }
                    }
                });
            }
        });
    }

    private void produtoDropdown () {

        tvProdutoVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                produtos = db.listAllProdutosOrdered();
                listaDinamicaProdutos = new ArrayList<Produto>();

                if (!produtos.isEmpty()) {
                    for (Produto p : produtos)
                        listaDinamicaProdutos.add(p);
                }

                dialogProduto = new Dialog(AddVenda.this);
                dialogProduto.setContentView(R.layout.spinner_produto);

                adapterProduto = new AdapterProduto(dialogProduto.getContext(), 0, listaDinamicaProdutos);

                dialogProduto.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
                dialogProduto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogProduto.show();

                TextView tvProduto = dialogProduto.findViewById(R.id.tvSpinnerProduto);
                EditText editProduto = dialogProduto.findViewById(R.id.editTextSpinnerProduto);

                listViewProdutos = (ListView) dialogProduto.findViewById(R.id.lvSpinnerProduto);
                listViewProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewProdutos.setAdapter(adapterProduto);

                editProduto.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        atualizaListaProdutos(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            Produto p = (Produto) listViewProdutos.getItemAtPosition(i);
                            tvProdutoVenda.setText(p.getDescricao());
                            if (produtoSelecionado != p) {
                                produtoSelecionado = p;
                            }
                            editQtdProdutoVenda.setText("0");
                            editValorProdutoVenda.setText(MaskEditUtil.doubleToMoneyTest(produtoSelecionado.getValor()));
                            dialogProduto.dismiss();
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage().toString());
                        }
                    }
                });
            }
        });
    }

    private void atualizaListaClientes (CharSequence _nome) {
        clientes = db.listClientesByNome(_nome.toString());
        listaDinamicaClientes = new ArrayList<Cliente>();

        if (clientes != null && !clientes.isEmpty()) {
            for (Cliente c : clientes)
                listaDinamicaClientes.add(c);
        }

        adapterCliente = new AdapterCliente(dialogCliente.getContext(), 0, listaDinamicaClientes);
        listViewClientes.setAdapter(adapterCliente);
        adapterCliente.notifyDataSetChanged();
    }

    private void atualizaListaProdutos (CharSequence _desc) {
        produtos = db.listAllProdutosByDesc(_desc.toString());
        listaDinamicaProdutos = new ArrayList<Produto>();

        if (produtos != null && !produtos.isEmpty()) {
            for (Produto p : produtos)
                listaDinamicaProdutos.add(p);
        }

        adapterProduto = new AdapterProduto(dialogProduto.getContext(), 0, listaDinamicaProdutos);
        listViewProdutos.setAdapter(adapterProduto);
        adapterProduto.notifyDataSetChanged();
    }

    private void initButtons () {
        btAdicionarProdutoVenda = (Button) findViewById(R.id.btAdicionarProdutoVenda);
        btAvancar = (Button) findViewById(R.id.btAvancarVenda);
        btCancelar = (Button) findViewById(R.id.btCancelarVenda);

        btAdicionarProdutoVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    addProdutoToSelecionados(produtoSelecionado,
                            Integer.parseInt(editQtdProdutoVenda.getText().toString()),
                            MaskEditUtil.moneyToDoubleTest(editValorProdutoVenda.getText().toString()));
                }
            }
        });

        btAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clienteSelecionado != null && vendaRascunho != null && !listaDinamicaProdVenda.isEmpty()) {
                    saveVendaAndProdVenda();
                    startNextAddVendaActivity();
                } else {
                    Log.e("INFO BT AVANÇAR", "/// não entrou no if " + String.valueOf(clienteSelecionado) + " " + String.valueOf(vendaRascunho) + " " + String.valueOf(listaDinamicaProdVenda));
                }
            }
        });
    }

    private void initTextViews () {
        tvValorTotalProdutosVenda = (TextView) findViewById(R.id.tvValorTotalVenda);
        tvQtdProdutosVenda = (TextView) findViewById(R.id.tvQtdTotalVenda);
        tvClienteVenda = (TextView) findViewById(R.id.tvClienteVenda);
        tvProdutoVenda = (TextView) findViewById(R.id.tvProdutoVenda);
    }

    private void initLinearLayouts () {
        llClienteVenda = (LinearLayout) findViewById(R.id.llClienteNovaVenda);
        llProdutoVenda = (LinearLayout) findViewById(R.id.llProdutoNovaVenda);
        llProdutosAdicionados = (LinearLayout) findViewById(R.id.llProdutosAdicionadosNovaVenda);

    }

    private void initEditTexts () {
        editQtdProdutoVenda = (EditText) findViewById(R.id.editQtdProdutoVenda);
        editValorProdutoVenda = (EditText) findViewById(R.id.editValorProdutoVenda);
    }

    private void initImageViews () {
        imgLessQtdProduto = (ImageView) findViewById(R.id.imgLessQtdProduto);
        imgMoreQtdProduto = (ImageView) findViewById(R.id.imgMoreQtdProduto);

        imgLessQtdProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qtdProduto = Integer.parseInt(editQtdProdutoVenda.getText().toString());
                if (qtdProduto > 0) {
                    qtdProduto--;
                    editQtdProdutoVenda.setText(String.valueOf(qtdProduto));
                } else {
                    Toast.makeText(AddVenda.this, "Quantidade incorreta", Toast.LENGTH_SHORT);
                }
            }
        });

        imgMoreQtdProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qtdProduto = Integer.parseInt(editQtdProdutoVenda.getText().toString());
                if (qtdProduto >= 0) {
                    qtdProduto++;
                    editQtdProdutoVenda.setText(String.valueOf(qtdProduto));
                } else {
                    Toast.makeText(AddVenda.this, "Quantidade incorreta", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void initListViews () {
        lvProdutosAdicionados = (ListView) findViewById(R.id.lvProdVendaAdicionados);
    }

    private void criaVenda () {
        vendaRascunho = new Venda();
        vendaRascunho.setId(db.selectMaxVenda().getId() + 1);
        vendaRascunho.setData(DateCustomText.getActualDateTime());
        vendaRascunho.setCliente(clienteSelecionado);

    }

    private void addProdutoToSelecionados (Produto _produto, int _qtd, Double _valor) {
        ProdVenda pv = new ProdVenda(vendaRascunho, _produto, _qtd, _valor);
        listaDinamicaProdVenda.add(pv);
        produtoSelecionado = null;
        tvProdutoVenda.setText("Selecione um produto");
        editQtdProdutoVenda.setText("0");
        editValorProdutoVenda.setText("0,00");
        adapterProdutoVenda2.notifyDataSetChanged();
        justifyListViewHeightBasedOnChildren(lvProdutosAdicionados);
        atualizaValoresVenda();
    }

    private void atualizaValoresVenda () {
        valorTotal = 0.00;
        qtdProdutos = 0;
        for (ProdVenda pv : listaDinamicaProdVenda) {
            valorTotal += (pv.getValor_unit() * pv.getQtd());
            qtdProdutos += pv.getQtd();
        }
        vendaRascunho.setValor(valorTotal);
        tvValorTotalProdutosVenda.setText(MaskEditUtil.doubleToMoneyValue(valorTotal));
        tvQtdProdutosVenda.setText(String.valueOf(qtdProdutos));
    }

    private boolean checkFields () {
        boolean ok = true;
        if (clienteSelecionado == null || clienteSelecionado.getId() < 0) {
            ok = false;
            llClienteVenda.requestFocus();
            Toast.makeText(AddVenda.this, "Insira um cliente para sua venda", Toast.LENGTH_LONG);
        } else
        if (produtoSelecionado == null || produtoSelecionado.getId() < 0) {
            ok = false;
            llProdutoVenda.requestFocus();
            Toast.makeText(AddVenda.this, "Insira um produto para adicionar á sua venda", Toast.LENGTH_LONG);
        } else
        if (Integer.parseInt(editQtdProdutoVenda.getText().toString()) <= 0) {
            ok = false;
            editQtdProdutoVenda.requestFocus();
            Toast.makeText(AddVenda.this, "Insira uma quantidade válida para o produto selecionado", Toast.LENGTH_LONG);
        }
        return ok;
    }

    private void saveVendaAndProdVenda () {
        db.addVenda(vendaRascunho);
        vendaRascunho = db.selectMaxVenda();
        for (ProdVenda p : listaDinamicaProdVenda) {
            db.addProdVenda(p);
        }
    }

    private void startNextAddVendaActivity () {
        Intent intent = new Intent(AddVenda.this, AddVendaDetails.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", vendaRascunho.getId());
        intent.putExtras(bundle);

        startActivity(intent);
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
