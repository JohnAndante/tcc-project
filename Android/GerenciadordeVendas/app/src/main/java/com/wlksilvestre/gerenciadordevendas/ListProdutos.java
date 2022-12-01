package com.wlksilvestre.gerenciadordevendas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class ListProdutos extends AppCompatActivity {

    private Button btProdutoVoltar;
    private Button btProdutoAdicionar;
    private EditText editBuscaProduto;
    private ImageButton imgbtNovoProduto;
    private ImageView imgSearch;
    private ConstraintLayout clAdicionarProduto;
    private ListView listViewProdutos;
    private AdapterProduto adapter;
    private ArrayList<Produto> listaDinamicaProdutos;


    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVO_PRODUTO = 101;
    public static final int ALTERAR_PRODUTO = 102;
    public static final int CONSULTAR_PRODUTO = 103;
    public static final int RESULT_ALT_PRODUTO = 202;
    private boolean isBuscaOpen = false;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produtos);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //addDefaultData();
        initButtonsHub();
        initEditTexts();
        initImgs();

        listProdutos();

        imgbtNovoProduto = (ImageButton) findViewById(R.id.imgbtIconeNovoProduto);
        imgbtNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewProduto();
            }
        });

        clAdicionarProduto = (ConstraintLayout) findViewById(R.id.clAdicionarProduto);
        clAdicionarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewProduto();
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = getIntent();

        //-- Criando novos dados
        if ((requestCode == NOVO_PRODUTO) && (resultCode == RESULT_OK)) {

            Produto produtoMax = new Produto();

            try {
                produtoMax = db.selectMaxProduto();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            listaDinamicaProdutos.add(produtoMax);
            adapter.notifyDataSetChanged();
        }

        //-- Alterando dados existentes
        if ((requestCode == CONSULTAR_PRODUTO) && (resultCode == RESULT_ALT_PRODUTO)) {
            listProdutos();
            adapter.notifyDataSetChanged();
        }

    }

    private void initButtonsHub () {
        btProdutoVoltar = findViewById(R.id.btProdutoVoltar);

        btProdutoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void initEditTexts () {
        editBuscaProduto = findViewById(R.id.editBuscarProduto);

        editBuscaProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                atualizaListaProdutos(charSequence);
                if (!isBuscaOpen) {
                    isBuscaOpen = true;
                    imgSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_cancel_24));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initImgs () {
        imgSearch = findViewById(R.id.imgSearchProdutos);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBuscaOpen)
                    editBuscaProduto.requestFocus();
                else {
                    imgSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_search_24_whit));
                    editBuscaProduto.setText("");
                    closeSoftKeyboard();
                    editBuscaProduto.clearFocus();
                }
            }
        });
    }

    private void atualizaListaProdutos(CharSequence _desc) {
        List<Produto> produtos = db.listProdutosOnSearch(_desc);
        listaDinamicaProdutos = new ArrayList<>();

        if (!produtos.isEmpty()) {
            listaDinamicaProdutos.addAll(produtos);
        } else {
            Toast.makeText(getApplicationContext(), "Não há dados compatíveis para sua busca.", Toast.LENGTH_SHORT).show();
        }

        adapter = new AdapterProduto(this, 0, listaDinamicaProdutos);

        listViewProdutos = (ListView) findViewById(R.id.listVProdutos);
        listViewProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        listViewProdutos.setAdapter(adapter);

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Produto p = (Produto) listViewProdutos.getItemAtPosition(i);
                    openProdutoData(p, i);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });

        adapter.notifyDataSetChanged();
    }

    private void addNewProduto () {
        Intent intent = new Intent(getApplication(), AddProduto.class);
        startActivityForResult(intent, NOVO_PRODUTO);
    }

    private void listProdutos () {

        List<Produto> produtos = db.listAllProdutosOrdered();
        listaDinamicaProdutos = new ArrayList<Produto>();

        if (!produtos.isEmpty()) {
            listaDinamicaProdutos.addAll(produtos);
        } else {
            Toast.makeText(getApplicationContext(), "Não há produtos cadastrados", Toast.LENGTH_SHORT).show();
        }

        adapter = new AdapterProduto(this, 0, listaDinamicaProdutos);

        listViewProdutos = (ListView) findViewById(R.id.listVProdutos);
        listViewProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        listViewProdutos.setAdapter(adapter);

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Produto p = (Produto) listViewProdutos.getItemAtPosition(i);
                    openProdutoData(p, i);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });
    }

    private void openProdutoData (Produto p, int position) {

        Intent intent = new Intent (ListProdutos.this, ViewProduto.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", p.getId());
        bundle.putInt("posicao", position);
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_PRODUTO);
    }

    private void closeSoftKeyboard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

