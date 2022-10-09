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

public class ListVendas extends AppCompatActivity {

    private Button btVendaVoltar;
    private Button btVendaAdicionar;
    private ImageButton imgbtNovaVenda;
    private LinearLayout llAdicionarVenda;
    private ListView listViewVendas;
    private AdapterVenda adapter;
    private GestureDetector gestureDetector;
    private ArrayList<Venda> listaDinamicaVendas;
    private ArrayList<String> arraylist;

    private static final int LIMITE_SWIPE = 100;
    private static final int LIMITE_VELOCIDADE = 100;

    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVA_VENDA = 4000;
    public static final int ALTERAR_VENDA = 4100;
    public static final int CONSULTAR_VENDA = 4200;
    public static final int RESULT_ALT_VENDA = 4300;

    //private int viewCounter = 0;


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


    protected void onCreate (Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        setContentView(R.layout.activity_list_vendas);

        initButtonsHub();
        listVendas();

        imgbtNovaVenda = findViewById(R.id.imgbtIconeNovaVenda);
        imgbtNovaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPgto();
            }
        });

        llAdicionarVenda = findViewById(R.id.llAdicionarVenda);
        llAdicionarVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPgto();
            }
        });

        addDefaultData();
        //adjustView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = getIntent();

        //-- Criando novos dados
        if (requestCode == NOVA_VENDA && resultCode == RESULT_OK) {

        }

        //-- Alterando dados existentes
        if (requestCode == CONSULTAR_VENDA && resultCode == RESULT_ALT_VENDA) {

        }
    }

    private void initButtonsHub () {
        btVendaVoltar = findViewById(R.id.btVendaVoltar);

        btVendaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListVendas.this, MainActivity.class));
            }
        });
    }

    private void addNewPgto () {
        Intent intent = new Intent(getApplication(), AddVenda.class);
        startActivityForResult(intent, NOVA_VENDA);
    }

    private void listVendas () {

        List<Venda> vendas = db.listAllVendas();
        listaDinamicaVendas = new ArrayList<Venda>();
        gestureDetector = new GestureDetector(this, gestureListener);

        if (!vendas.isEmpty()) {
            for (Venda v : vendas)
                listaDinamicaVendas.add(v);
        } else {
            Toast.makeText(getApplicationContext(), "Não há vendas registradas no app", Toast.LENGTH_LONG);
        }

        adapter = new AdapterVenda(this, 0, listaDinamicaVendas);

        listViewVendas = findViewById(R.id.listVVendas);
        listViewVendas.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        listViewVendas.setAdapter(adapter);
        listViewVendas.setOnTouchListener(touchListener);

        listViewVendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Venda v = (Venda) listViewVendas.getItemAtPosition(i);
                    openVendaData(v, i);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }
            }
        });
    }

    private void openVendaData (Venda v, int position) {

        Intent intent = new Intent(ListVendas.this, ViewVenda.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", v.getId());
        bundle.putInt("posicao", position);
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_VENDA);
    }

    // Funções de teste

    private void addDefaultData () {
        if (!checkClientes())
            addDefaultClientes();
        if (!checkFormaPgtos())
            addDefaultFormaPgtos();
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
        if (!checkVendas()) {
            addDefaultVendas();
            addDefaultProdVenda();
        }
    }

    private boolean checkClientes () {
        // Confere se a quantia de clientes registrados é o desejado para testes
        // No momento teremos 2 clientes
        int correto = 2;
        List<Cliente> clientes = db.listAllClientes();
        Log.e("INFO SIZE CLIENTES", String.valueOf(clientes.size()));

        if (clientes.size() < correto)
            return false;
        else {
            Log.e("INFO SIZE CLIENTES", String.valueOf(clientes.size()));
            for (Cliente c : clientes)
                Log.e("INFO CLIENTE ID + NOME", c.getId() + " " + c.getNome());
        }
        return true;
    }

    private void addDefaultClientes () {
        // Adiciona os clientes usados em teste
        db.addCliente(new Cliente(901, "Clieiton costa", "(44) 9 9951-0032"));
        db.addCliente(new Cliente(902, "Maria dos Penhados", "(44) 9 8456-8774"));
    }

    private boolean checkFormaPgtos () {
        // Confere se a quantia de forma de pagamento registrados é o desejado para testes
        // No momento teremos 3 formas de pagamento
        int correto = 3;
        List<FormaPgto> formaPgtos = db.listFormaPgto();
        Log.e("INFO SIZE FORMA PGTOS", String.valueOf(formaPgtos.size()));

        if (formaPgtos.size() < correto)
            return false;
        else
            Log.e("INFO SIZE FORMA PGTOS", String.valueOf(formaPgtos.size()));
        return true;
    }

    private void addDefaultFormaPgtos () {
        // Adiciona as formas de pagamento utilizadas em teste
        db.addFormaPgto(new FormaPgto(801, "Dinheiro", 0, 0));
        db.addFormaPgto(new FormaPgto(802, "Cartão de Crédito", 0, 12));
    }

    // Produtos
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

    // Vendas
    private boolean checkVendas () {
        // Confere se a quantia de vendas registradas é a desejada para testes
        // No momento teremos 2 vendas
        int correto = 2;
        List<Venda> vendas = db.listAllVendas();
        Log.e("INFO SIZE VENDAS", String.valueOf(vendas.size()));

        if (vendas.size() < correto)
            return false;
        else {
            Log.e("INFO SIZE VENDAS", String.valueOf(vendas.size()));
            for (Venda v : vendas)
                Log.e("INFO VENDAS ID", v.getId() + " - " + v.getCliente().getNome() + " - " + v.getValor());
        }
        return true;
    }

    private void addDefaultVendas () {
        // adicionar a venda
        db.addVenda(new Venda(1, "01/10/2022 08:20:00", 139.80, db.selectCliente(1), null ));
        db.addVenda(new Venda(2, "02/10/2022 10:45:00", 139.80, db.selectCliente(2), null ));
    }

    private void addDefaultProdVenda () {
        db.addProdVenda(new ProdVenda(db.selectVenda(1), db.selectProduto(1), 1, 99.9));
        db.addProdVenda(new ProdVenda(db.selectVenda(1), db.selectProduto(2), 1, 39.9));

        db.addProdVenda(new ProdVenda(db.selectVenda(2), db.selectProduto(3), 2, 69.9));
    }

}
