package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListProdutos extends AppCompatActivity {

    private Button btProdutoVoltar;
    private Button btProdutoAdicionar;
    private ImageButton imgbtNovoProduto;
    private LinearLayout llAdicionarProduto;
    private ListView listViewProdutos;
    private AdapterProduto adapter;
    private GestureDetector gestureDetector;
    private ArrayList<Produto> listaDinamicaProdutos;
    private ArrayList<String> arraylist;

    private static final int LIMITE_SWIPE = 100;
    private static final int LIMITE_VELOCIDADE = 100;

    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVO_PRODUTO = 101;
    public static final int ALTERAR_PRODUTO = 102;
    public static final int CONSULTAR_PRODUTO = 103;
    public static final int RESULT_ALT_PRODUTO = 202;

    private int viewCounter = 0;

    /*
    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        // Método do Swipe
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diferencaX = e2.getX() - e1.getX();
            if (Math.abs(diferencaX) > LIMITE_SWIPE && Math.abs(velocityX) > LIMITE_VELOCIDADE){
                if (diferencaX > 0){
                    Log.i("MOVIMENTO", "Movimento para a direita");
                }
                else {
                    Log.i("MOVIMENTO", "Movimento para a esquerda");
                }
            }
            return true;
        }
    };

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch (View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };
    */

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produtos);

        initButtonsHub();
        //listProdutos();

        imgbtNovoProduto = (ImageButton) findViewById(R.id.imgbtIconeNovoProduto);
        imgbtNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addNewProduto();
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Alterando dados
        // Verificar se ainda é útil

        // Criando novos dados
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

        List<Produto> produtos = db.listAllProdutos();
        listaDinamicaProdutos = new ArrayList<Produto>();

        if (!produtos.isEmpty()) {
            for (Produto p : produtos)
                listaDinamicaProdutos.add(p);
        }

        adapter = new AdapterProduto(this, 0, listaDinamicaProdutos);

        listViewProdutos = (ListView) findViewById(R.id.listVProdutos);
        listViewProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        listViewProdutos.setAdapter(adapter);
        //listViewProdutos.setOnTouchListener(touchListener);

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Produto p = (Produto) listViewProdutos.getItemAtPosition(i);
                    openProdutoData(p);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }
            }
        });
    }

    private void openProdutoData (Produto p) {

        Intent intent = new Intent (ListProdutos.this, viewProduto.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", p.getId());
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_PRODUTO);
    }
}
