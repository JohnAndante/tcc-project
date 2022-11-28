package com.wlksilvestre.gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
    private ImageButton imgbtNovoProduto;
    private ConstraintLayout clAdicionarProduto;
    private ListView listViewProdutos;
    private AdapterProduto adapter;
    private ArrayList<Produto> listaDinamicaProdutos;


    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVO_PRODUTO = 101;
    public static final int ALTERAR_PRODUTO = 102;
    public static final int CONSULTAR_PRODUTO = 103;
    public static final int RESULT_ALT_PRODUTO = 202;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produtos);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //addDefaultData();
        initButtonsHub();

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
}

