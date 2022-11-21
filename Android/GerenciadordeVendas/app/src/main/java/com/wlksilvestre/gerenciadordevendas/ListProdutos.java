package com.wlksilvestre.gerenciadordevendas;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListProdutos extends AppCompatActivity {

    private Button btProdutoVoltar;
    private Button btProdutoAdicionar;
    private ImageButton imgbtNovoProduto;
    private ConstraintLayout clAdicionarProduto;
    private ListView listViewProdutos;
    private AdapterProduto adapter;
    private ArrayList<Produto> listaDinamicaProdutos;
    private ArrayList<String> arraylist;


    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVO_PRODUTO = 101;
    public static final int ALTERAR_PRODUTO = 102;
    public static final int CONSULTAR_PRODUTO = 103;
    public static final int RESULT_ALT_PRODUTO = 202;

    private int viewCounter = 0;
    static int occupiedHeight = 0;
    final static int deviceHeight  = Resources.getSystem().getDisplayMetrics().heightPixels;

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

        clAdicionarProduto = (ConstraintLayout) findViewById(R.id.clPaiAdicionarProduto);
        clAdicionarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int llAddProdutoHeight = clAdicionarProduto.getHeight();
                Log.i("INFO LLADDPRODUTOHEIGHT", String.valueOf(llAddProdutoHeight));
                addNewProduto();
            }
        });

        adjustView();
        occupiedHeight = getActionBarHeight() + getNavigationBarHeight() + getStatusBarHeight() + 169;
        // lladdproduto height = 169

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
                startActivity(new Intent(ListProdutos.this, MainActivity.class));
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
            for (Produto p : produtos)
                listaDinamicaProdutos.add(p);
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
                    Log.e("ERROR", e.getMessage().toString());
                }
            }
        });

        justifyListViewHeightBasedOnChildren(listViewProdutos);
    }

    private void openProdutoData (Produto p, int position) {

        Intent intent = new Intent (ListProdutos.this, ViewProduto.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", p.getId());
        bundle.putInt("posicao", position);
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_PRODUTO);
    }

    private void adjustView () {
        int llAddProdutoHeight = 170;
        int deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int newlistViewHeight = (deviceHeight - llAddProdutoHeight) - 30;
    }

    private int getActionBarHeight () {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        return actionBarHeight;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getNavigationBarHeight() {
        int result = 0;
        boolean hasMenuKey = ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey();
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && !hasMenuKey)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static void justifyListViewHeightBasedOnChildren (@NonNull ListView listView) {

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
            Log.e("INFO HEIGHT A", String.valueOf(params.height));
            Log.e("INFO HEIGHT B", String.valueOf(deviceHeight - occupiedHeight));
            if (params.height >= (deviceHeight - occupiedHeight - 500)) {
                params.height = (deviceHeight - occupiedHeight - 500);
            }
            listView.setLayoutParams(params);
            listView.requestLayout();

        }
    }

}

