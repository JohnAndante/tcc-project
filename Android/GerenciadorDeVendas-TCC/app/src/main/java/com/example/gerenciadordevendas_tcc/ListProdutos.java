package com.example.gerenciadordevendas_tcc;

import android.content.Intent;
import android.content.res.Resources;
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

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produtos);


        addDefaultData();
        initButtonsHub();

        listProdutos();

        imgbtNovoProduto = (ImageButton) findViewById(R.id.imgbtIconeNovoProduto);
        imgbtNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewProduto();
            }
        });

        llAdicionarProduto = (LinearLayout) findViewById(R.id.llAdicionarProduto);
        llAdicionarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int llAddProdutoHeight = llAdicionarProduto.getHeight();
                Log.i("INFO LLADDPRODUTOHEIGHT", String.valueOf(llAddProdutoHeight));
                addNewProduto();
            }
        });

        adjustView();
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
        gestureDetector = new GestureDetector(this, gestureListener);

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
        listViewProdutos.setOnTouchListener(touchListener);

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
    }

    private void openProdutoData (Produto p, int position) {

        Intent intent = new Intent (ListProdutos.this, ViewProduto.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", p.getId());
        bundle.putInt("posicao", position);
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_PRODUTO);
    }

    private void addDefaultData () {
        if (!checkCategorias())
            addDefaultCategorias();
        if (!checkSubcategorias())
            addDefaultSubcats();
        if (!checkMarcas())
            addDefaultMarcas();
        if (!checkLinhas())
            addDefaultLinhas();
        if (!checkProdutos())
            addDefaultProdutos();
        if (!checkProdSubcats())
            addDefaultProdSubcats();
    }

    private void adjustView () {
        int llAddProdutoHeight = 170;
        int deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int newlistViewHeight = (deviceHeight - llAddProdutoHeight) - 30;
    }

    private boolean checkCategorias () {
        // Confere se a quantia de categorias criadas é a desejada para testes
        // No momento teremos 4 categorias
        int correto = 4;
        List<Categoria> categorias = db.listCategorias();
        Log.e("INFO SIZE CATEGORIAS", String.valueOf(categorias.size()));

        if (categorias.size() < correto)
            return false;
        else
            Log.e("INFO SIZE CATEGORIAS", String.valueOf(categorias.size()));
        return true;
    }

    private void addDefaultCategorias () {
        // Adiciona as categorias padrões para teste
        // teremos 4 categorias para teste
        db.addCategoria(new Categoria(1, "Perfumaria",  null));
        db.addCategoria(new Categoria(2, "Vestuário",   null));
        db.addCategoria(new Categoria(3, "Maquiagem",   null));
        db.addCategoria(new Categoria(4, "Informática", null));
    }

    private boolean checkSubcategorias () {
        // Confere se a quantia de subcategorias criadas é a desejada para testes
        // No momento teremos 8 subcategorias
        int correto = 8;
        List<Subcat> subcats = db.listSubcats();
        Log.e("INFO SIZE SUBCATS", String.valueOf(subcats.size()));

        if (subcats.size() < correto)
            return false;
        else
            Log.e("INFO SIZE SUBCATS", String.valueOf(subcats.size()));
        return true;
    }

    private void addDefaultSubcats () {
        // Adiciona as subcategorias padrões para teste
        // teremos 9 subcategorias
        db.addSubcat(new Subcat(1, "Colônia", db.selectCategoria(1)));
        db.addSubcat(new Subcat(2, "Desodorante", db.selectCategoria(1)));

        db.addSubcat(new Subcat(3, "Blusa", db.selectCategoria(2)));
        db.addSubcat(new Subcat(4, "Calça", db.selectCategoria(2)));
        db.addSubcat(new Subcat(5, "Camiseta", db.selectCategoria(2)));

        db.addSubcat(new Subcat(6, "Batom", db.selectCategoria(3)));
        db.addSubcat(new Subcat(7, "Rímel", db.selectCategoria(3)));

        db.addSubcat(new Subcat(8, "Masculino", db.selectCategoria(4)));
        db.addSubcat(new Subcat(9, "Unissex", db.selectCategoria(4)));
    }

    private boolean checkMarcas () {
        // Confere se a quantia de marcas criadas é a desejada para testes
        // No momento teremos 2 marcas
        int correto = 2;
        List<Marca> marcas = db.listMarcas();
        Log.e("INFO SIZE MARCAS", String.valueOf(marcas.size()));

        if (marcas.size() < correto)
            return false;
        else
            Log.e("INFO SIZE MARCAS", String.valueOf(marcas.size()));
        return true;
    }

    private void addDefaultMarcas () {
        // Adiciona as marcas padrões para teste
        // teremos 2 marcas
        db.addMarca(new Marca(1, "Natura"));
        db.addMarca(new Marca(2, "Nicoboco"));
    }

    private boolean checkLinhas () {
        // Confere se a quantia de linhas criadas é a desejada para testes
        // No momento teremos 4 linhas
        int correto = 4;
        List<Linha> linhas = db.listLinhas();
        Log.e("INFO SIZE LINHAS", String.valueOf(linhas.size()));

        if (linhas.size() < 4)
            return false;
        else
            Log.e("INFO SIZE LINHAS", String.valueOf(linhas.size()));
        return true;
    }

    private void addDefaultLinhas () {
        // Adiciona as linhas padrões para teste
        // teremos 4 linhas
        db.addLinha(new Linha(1, "Kaiak", db.selectMarca(1)));
        db.addLinha(new Linha(2, "Faces", db.selectMarca(1)));

        db.addLinha(new Linha(3, "Athna", db.selectMarca(2)));
        db.addLinha(new Linha(4, "Persephone", db.selectMarca(2)));
    }

    private boolean checkProdutos () {
        // Confere se a quantia de produtos criados é a desejada para testes
        // No momento teremos 8 produtos
        int correto = 8;
        List<Produto> produtos = db.listAllProdutos();
        Log.e("INFO SIZE PRODUTOS", String.valueOf(produtos.size()));


        if (produtos.size() < correto)
            return false;
        else
            Log.e("INFO SIZE PRODUTOS", String.valueOf(produtos.size()));
        return true;
    }

    private void addDefaultProdutos () {
        // Adiciona os produtos padrões para teste
        // teremos 8 produtos
        db.addProduto(new Produto(1, "Colônia Kaiak 200ml",89.9, db.selectLinha(1)));
        db.addProduto(new Produto(2, "Desodorante Kaiak 150ml",39.9, db.selectLinha(1)));

        db.addProduto(new Produto(3, "Camiseta Athena Azul Nicoboco Masculina G",99.9, db.selectLinha(3)));
        db.addProduto(new Produto(4, "Camiseta Athena Preta Nicoboco Masculina P",99.9, db.selectLinha(3)));

        db.addProduto(new Produto(5, "Batom Faces Vinho 10g", 79.9, db.selectLinha(2)));
        db.addProduto(new Produto(6, "Batom Faces Rosa Claro 10g", 79.9, db.selectLinha(2)));

        db.addProduto(new Produto(7, "Blusa Persephone Cinza Canguru Unissex G", 199.9, db.selectLinha(4)));
        db.addProduto(new Produto(8, "Calça Persephone Moletom Preta Unissex GG", 109.9, db.selectLinha(4)));
    }

    private boolean checkProdSubcats () {
        // Confere se a quantia de produtosXsubcategorias criados é a desejada para testes
        // No momento teremos 14 prodsubcats
        int correto = 14;
        List<ProdSubcat> prodSubcats = db.listAllProdSubcats();
        Log.e("INFO SIZE PRODSUBCATS", String.valueOf(prodSubcats.size()));

        if (prodSubcats.size() < correto)
            return false;
        else
            Log.e("INFO SIZE PRODSUBCATS", String.valueOf(prodSubcats.size()));
        return true;
    }

    private void addDefaultProdSubcats () {
        // 1, "Colônia Kaiak 200ml"
        db.addProdSubcat(new ProdSubcat(db.selectProduto(1), db.selectSubcat(1)));
        db.addProdSubcat(new ProdSubcat(db.selectProduto(1), db.selectSubcat(7)));

        // 2, "Desodorante Kaiak 150ml"
        db.addProdSubcat(new ProdSubcat(db.selectProduto(2), db.selectSubcat(2)));
        db.addProdSubcat(new ProdSubcat(db.selectProduto(2), db.selectSubcat(7)));

        // 3, "Camiseta Athena Azul Nicoboco Masculina G
        db.addProdSubcat(new ProdSubcat(db.selectProduto(3), db.selectSubcat(5)));
        db.addProdSubcat(new ProdSubcat(db.selectProduto(3), db.selectSubcat(8)));

        // 4, "Camiseta Athena Preta Nicoboco Masculina P"
        db.addProdSubcat(new ProdSubcat(db.selectProduto(4), db.selectSubcat(5)));
        db.addProdSubcat(new ProdSubcat(db.selectProduto(4), db.selectSubcat(8)));

        // 5, "Batom Faces Vinho 10g"
        db.addProdSubcat(new ProdSubcat(db.selectProduto(5), db.selectSubcat(6)));

        // 6, "Batom Faces Rosa Claro 10g"
        db.addProdSubcat(new ProdSubcat(db.selectProduto(6), db.selectSubcat(6)));

        // 7, "Blusa Persephone Cinza Canguru Unissex G"
        db.addProdSubcat(new ProdSubcat(db.selectProduto(7), db.selectSubcat(3)));
        db.addProdSubcat(new ProdSubcat(db.selectProduto(7), db.selectSubcat(9)));

        // 8, "Calça Persephone Moletom Preta Unissex GG"
        db.addProdSubcat(new ProdSubcat(db.selectProduto(8), db.selectSubcat(4)));
        db.addProdSubcat(new ProdSubcat(db.selectProduto(8), db.selectSubcat(9)));
    }


}

