package com.wlksilvestre.gerenciadordevendas;

import android.annotation.SuppressLint;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddVenda extends AppCompatActivity {

    private LinearLayout llClienteVenda;
    private LinearLayout llProdutoVenda;
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

    private List<ClienteTelefone> clienteTelefoneList;
    private ArrayList<ClienteTelefone> listaDinamicaClienteTelefone;
    private AdapterCliente adapterCliente;
    private Dialog dialogCliente;
    private ListView listViewClientes;

    private List<Produto> produtos;
    private ArrayList<Produto> listaDinamicaProdutos;
    private AdapterProdutoDropdown adapterProdutoDropdown;
    private Dialog dialogProduto;
    private ListView listViewProdutos;

    private ArrayList<ProdVenda> listaDinamicaProdVenda;
    private AdapterProdutoVenda2 adapterProdutoVenda2;

    private Cliente clienteSelecionado = null;
    private Produto produtoSelecionado = null;
    private Venda vendaRascunho = null;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Usuario usuario;

    private int qtdProdutos = 0;
    private Double valorTotal = 0.00;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venda_01);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        usuario = new Usuario();
        usuario = db.selectUsuarioByUID(currentUser.getUid());

        initButtons();
        initTextViews();
        initLinearLayouts();
        initEditTexts();
        initImageViews();
        initListViews();

        clienteDropdown();
        produtoDropdown();

        editQtdProdutoVenda.setText(String.valueOf(0));
        editValorProdutoVenda.addTextChangedListener(new MoneyTextWatcher(editValorProdutoVenda));

        initLvProdVenda();
    }

    private void clienteDropdown () {
        tvClienteVenda.setOnClickListener(view -> {

            clienteTelefoneList = db.listClientesAdapterOrdered(usuario.getUid());
            listaDinamicaClienteTelefone = new ArrayList<>();

            if (!clienteTelefoneList.isEmpty()) {
                listaDinamicaClienteTelefone.addAll(clienteTelefoneList);
            }

            dialogCliente = new Dialog(AddVenda.this);
            dialogCliente.setContentView(R.layout.spinner_cliente);

            adapterCliente = new AdapterCliente(dialogCliente.getContext(), 0, listaDinamicaClienteTelefone);

            dialogCliente.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
            dialogCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialogCliente.show();

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

            listViewClientes.setOnItemClickListener((adapterView, view1, i, l) -> {
                try {
                    ClienteTelefone ct = (ClienteTelefone) listViewClientes.getItemAtPosition(i);
                    Cliente c = ct.getCliente();
                    Log.e("CLIENTE SELECIONADO - Lista", c.getNome());
                    tvClienteVenda.setText(c.getNome());
                    clienteSelecionado = c;
                    dialogCliente.dismiss();
                    criaVenda();
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            });
        });
    }

    private void produtoDropdown () {

        tvProdutoVenda.setOnClickListener(view -> {
            produtos = db.listAllProdutosOrdered();
            listaDinamicaProdutos = new ArrayList<>();

            if (!produtos.isEmpty()) {
                listaDinamicaProdutos.addAll(produtos);
            }

            dialogProduto = new Dialog(AddVenda.this);
            dialogProduto.setContentView(R.layout.spinner_produto);

            adapterProdutoDropdown = new AdapterProdutoDropdown(dialogProduto.getContext(), 0, listaDinamicaProdutos);

            dialogProduto.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
            dialogProduto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogProduto.setCanceledOnTouchOutside(true);

            dialogProduto.show();

            EditText editProduto = dialogProduto.findViewById(R.id.editTextSpinnerProduto);

            listViewProdutos = (ListView) dialogProduto.findViewById(R.id.lvSpinnerProduto);
            listViewProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            listViewProdutos.setAdapter(adapterProdutoDropdown);

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

            listViewProdutos.setOnItemClickListener((adapterView, view1, i, l) -> {
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
                    Log.e("ERROR", e.getMessage());
                }
            });
        });
    }

    private void atualizaListaClientes (CharSequence _nome) {

        clienteTelefoneList = db.listClientesAdapterByNomeOrdered(_nome, usuario.getUid());
        listaDinamicaClienteTelefone = new ArrayList<>();

        if (clienteTelefoneList != null && !clienteTelefoneList.isEmpty()) {
            listaDinamicaClienteTelefone.addAll(clienteTelefoneList);
        }

        adapterCliente = new AdapterCliente(dialogCliente.getContext(), 0, listaDinamicaClienteTelefone);
        listViewClientes.setAdapter(adapterCliente);
        adapterCliente.notifyDataSetChanged();
    }

    private void atualizaListaProdutos (CharSequence _desc) {
        produtos = db.listAllProdutosByDesc(_desc.toString());
        listaDinamicaProdutos = new ArrayList<>();

        if (produtos != null && !produtos.isEmpty()) {
            listaDinamicaProdutos.addAll(produtos);
        }

        adapterProdutoDropdown = new AdapterProdutoDropdown(dialogProduto.getContext(), 0, listaDinamicaProdutos);
        listViewProdutos.setAdapter(adapterProdutoDropdown);
        adapterProdutoDropdown.notifyDataSetChanged();
    }

    private void initButtons () {
        btAdicionarProdutoVenda = (Button) findViewById(R.id.btAdicionarProdutoVenda);
        btAvancar = (Button) findViewById(R.id.btAvancarVenda);
        btCancelar = (Button) findViewById(R.id.btCancelarVenda);

        btAdicionarProdutoVenda.setOnClickListener(view -> {
            if (checkFields()) {
                addProdutoToSelecionados(produtoSelecionado,
                        Integer.parseInt(editQtdProdutoVenda.getText().toString()),
                        MaskEditUtil.moneyToDoubleTest(editValorProdutoVenda.getText().toString()));

            }
        });

        btAvancar.setOnClickListener(view -> {
            if (clienteSelecionado != null && vendaRascunho != null && !listaDinamicaProdVenda.isEmpty()) {
                saveVendaAndProdVenda();
                startNextAddVendaActivity();
            } else {
                assert clienteSelecionado != null;
                Log.e("INFO BT AVANÇAR", "/// não entrou no if " + clienteSelecionado.getNome() + " " + vendaRascunho + " " + listaDinamicaProdVenda);
            }
        });

        btCancelar.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
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

    }

    private void initEditTexts () {
        editQtdProdutoVenda = (EditText) findViewById(R.id.editQtdProdutoVenda);
        editValorProdutoVenda = (EditText) findViewById(R.id.editValorProdutoVenda);
    }

    private void initImageViews () {
        imgLessQtdProduto = (ImageView) findViewById(R.id.imgLessQtdProduto);
        imgMoreQtdProduto = (ImageView) findViewById(R.id.imgMoreQtdProduto);

        imgLessQtdProduto.setOnClickListener(view -> {
            int qtdProduto = Integer.parseInt(editQtdProdutoVenda.getText().toString());
            if (qtdProduto > 0) {
                qtdProduto--;
                editQtdProdutoVenda.setText(String.valueOf(qtdProduto));
            } else {
                Toast.makeText(AddVenda.this, "Quantidade incorreta", Toast.LENGTH_SHORT).show();
            }
        });

        imgMoreQtdProduto.setOnClickListener(view -> {
            int qtdProduto = Integer.parseInt(editQtdProdutoVenda.getText().toString());
            if (qtdProduto >= 0) {
                qtdProduto++;
                editQtdProdutoVenda.setText(String.valueOf(qtdProduto));
            } else {
                Toast.makeText(AddVenda.this, "Quantidade incorreta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListViews () {
        lvProdutosAdicionados = (ListView) findViewById(R.id.lvProdVendaAdicionados);
    }

    private void initLvProdVenda () {
        listaDinamicaProdVenda = new ArrayList<>();
        adapterProdutoVenda2 = new AdapterProdutoVenda2(this, 0, listaDinamicaProdVenda);

        lvProdutosAdicionados.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        lvProdutosAdicionados.setAdapter(adapterProdutoVenda2);
        lvProdutosAdicionados.setOnItemClickListener((adapterView, view, i, l) -> {
            ProdVenda pv = (ProdVenda) adapterView.getItemAtPosition(i);
            try {
                Produto p = pv.getProduto();
                tvProdutoVenda.setText(p.getDescricao());
                if (produtoSelecionado != p) {
                    produtoSelecionado = p;
                }
                editQtdProdutoVenda.setText(String.valueOf(pv.getQtd()));
                editValorProdutoVenda.setText(MaskEditUtil.doubleToMoneyTest(pv.getValor_unit()));
                listaDinamicaProdVenda.remove(i);
                adapterProdutoVenda2.notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren(lvProdutosAdicionados);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }

    private void criaVenda () {
        vendaRascunho = new Venda();
        int idMaxVenda = -1;
        try {
            idMaxVenda = db.selectMaxVenda().getId();
        } catch (Exception e) {
            Log.e("INFO ERRO MAX VENDA", e.getMessage());
        }
        if (idMaxVenda >= 0)
            vendaRascunho.setId(idMaxVenda + 1);
        vendaRascunho.setData(DateCustomText.getActualDateTime());
        vendaRascunho.setCliente(clienteSelecionado);
    }

    @SuppressLint("SetTextI18n")
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
            Toast.makeText(AddVenda.this, "Insira um cliente para sua venda", Toast.LENGTH_LONG).show();
        } else
        if (produtoSelecionado == null || produtoSelecionado.getId() < 0) {
            ok = false;
            llProdutoVenda.requestFocus();
            Toast.makeText(AddVenda.this, "Insira um produto para adicionar á sua venda", Toast.LENGTH_LONG).show();
        } else
        if (Integer.parseInt(editQtdProdutoVenda.getText().toString()) <= 0) {
            ok = false;
            editQtdProdutoVenda.requestFocus();
            Toast.makeText(AddVenda.this, "Insira uma quantidade válida para o produto selecionado", Toast.LENGTH_LONG).show();
        } else
        if (MaskEditUtil.moneyToDoubleTest(editValorProdutoVenda.getText().toString()) > produtoSelecionado.getValor()) {
            ok = false;
            Toast.makeText(AddVenda.this, "Valor do produto não pode ser maior que o cadastrado", Toast.LENGTH_LONG).show();
        } else
        if (MaskEditUtil.moneyToDoubleTest(editValorProdutoVenda.getText().toString()) <= 0) {
            ok = false;
            Toast.makeText(AddVenda.this, "Insira um valor válido para o produto", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(AddVenda.this, ViewVenda.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", vendaRascunho.getId());
        intent.putExtras(bundle);
        finish();

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
