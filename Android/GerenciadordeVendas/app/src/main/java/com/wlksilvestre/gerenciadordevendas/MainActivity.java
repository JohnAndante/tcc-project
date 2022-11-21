package com.wlksilvestre.gerenciadordevendas;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linearFabMain;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabNovoPagamento;
    private FloatingActionButton fabNovaVenda;
    private FloatingActionButton fabNovoCliente;
    private Button btCliente;
    private Button btProduto;
    private Button btVendas;
    private Button btPgtos;
    private Button btSairApp;
    private TextView textTitulo;
    private TextView textDialog;
    private TextView textQtdClientes;
    private TextView textQtdProdutos;
    private TextView textQtdVendas;
    private TextView textQtdPagamentos;
    private ConstraintLayout clClientes;
    private ConstraintLayout clProdutos;
    private ConstraintLayout clVendas;
    private ConstraintLayout clPagamentos;
    private Dialog loadingDialog;

    private Float translationY = 100f;
    private Boolean isMenuOpen = false;

    public final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    private final BancoDadosCliente db = new BancoDadosCliente(this);

    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fireDB;
    private DocumentReference docUsuarios;
    private Usuario usuario;

    SharedPreferences prefs = null;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireDB = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireDB = FirebaseFirestore.getInstance();

        usuario = new Usuario();

        if (currentUser != null) {
            usuario = db.selectUsuarioByUID(currentUser.getUid());
        } else {
            mAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, StartScreen.class);
            startActivity(intent);
            finish();
        }


        initFabMenu();
        initDialogCfg();
        initButtons();
        initTexts();
        initLayouts();
        initButtonsOnClick();
        initLayoutsOnClick();
        initCustomUI();

        loadingDialog.show();
        try {
            textDialog.setText("Verificando dados - Formas de Pagamento");
            if (!checkFormaPgtos())
                addDefaultFormaPgtos();
        } catch (Exception e) {
            Log.e("INFO ONCREATE - Formas de Pagamento", e.getMessage());
            loadingDialog.dismiss();
        }
        loadingDialog.dismiss();

        loadingDialog.show();
        try {
            textDialog.setText("Verificando dados - Categorias");
            if (checkCategorias())
                addDefaultCategorias();
        } catch (Exception e) {
            Log.e("INFO ONCREATE - Categorias", e.getMessage());
            loadingDialog.dismiss();
        }
        loadingDialog.dismiss();

        loadingDialog.show();
        try {
            textDialog.setText("Verificando dados - Subcategorias");
            if (checkSubcats())
                addDefaultSubcats();
        } catch (Exception e) {
            Log.e("INFO ONCREATE - Subcategorias", e.getMessage());
            loadingDialog.dismiss();
        }
        loadingDialog.dismiss();

        loadingDialog.show();
        try {
            textDialog.setText("Verificando dados - Marcas");
            if (checkMarcas())
                addDefaultMarcas();
        } catch (Exception e) {
            Log.e("INFO ONCREATE - Marcas", e.getMessage());
            loadingDialog.dismiss();
        }
        loadingDialog.dismiss();

        loadingDialog.show();
        try {
            textDialog.setText("Verificando dados - Linhas");
            if (checkLinhas())
                addDefaultLinhas();
        } catch (Exception e) {
            Log.e("INFO ONCREATE - Linhas", e.getMessage());
            loadingDialog.dismiss();
        }
        loadingDialog.dismiss();

        loadingDialog.show();
        try {
            textDialog.setText("Verificando dados - Estados");
            if (checkEstados())
                addDefaultEstados();
        } catch (Exception e) {
            Log.e("INFO ONCREATE - Estados", e.getMessage());
            loadingDialog.dismiss();
        }
        loadingDialog.dismiss();

        loadingDialog.show();
        try {
            textDialog.setText("Verificando dados - Cidades");
            if (checkCidades())
                addDefaultCidades();
        } catch (Exception e) {
            Log.e("INFO ONCREATE - Cidades", e.getMessage());
            loadingDialog.dismiss();
        }
        loadingDialog.dismiss();

    }

    private void initCustomUI () {
        String turno = DateCustomText.getCurrentTurno();
        if (usuario != null) {
            String nomeDiv[] = usuario.getNome().split(" ", 2);
            textTitulo.setText(turno + ", " + nomeDiv[0]);
        } else {
            textTitulo.setText(turno + "!");
        }
    }

    private void initTexts () {
        textTitulo = findViewById(R.id.textTituloMain);
        textQtdClientes = findViewById(R.id.textQtdClientes);
        textQtdProdutos = findViewById(R.id.textQtdProdutos);
        textQtdVendas = findViewById(R.id.textQtdVendas);
        textQtdPagamentos = findViewById(R.id.textQtdPagamentos);
    }

    private void initLayouts () {
        clClientes      = findViewById(R.id.clClientes);
        clProdutos      = findViewById(R.id.clProdutos);
        clVendas        = findViewById(R.id.clVendas);
        clPagamentos    = findViewById(R.id.clPagamentos);
    }

    private void initLayoutsOnClick () {
        clClientes.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ListClientes.class));
        });

        clProdutos.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ListProdutos.class));
        });

        clVendas.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ListVendas.class));
        });

        clPagamentos.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ListPgtos.class));
        });
    }

    private void initButtons () {

        btCliente = findViewById(R.id.btCliente);
        btProduto = findViewById(R.id.btProduto);
        btVendas = findViewById(R.id.btVendas);
        btPgtos = findViewById(R.id.btPgtos);
        btSairApp = findViewById(R.id.btSairApp);
    }

    private void initButtonsOnClick () {

        btCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListClientes.class));
            }
        });

        btProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListProdutos.class));
            }
        });

        btVendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListVendas.class));
            }
        });

        btPgtos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, StartScreen.class);
                startActivity(intent);
                finish();
            }
        });

        btSairApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

    }

    private void initDialogCfg () {
        loadingDialog = new Dialog(MainActivity.this);
        loadingDialog.setContentView(R.layout.spinner_loading_user_reg);
        textDialog = loadingDialog.getWindow().findViewById(R.id.textView3);
        textDialog.setText("Verificando informações");
        loadingDialog.getWindow().setLayout((int)(deviceWidth * 0.75), (int) (deviceHeight * 0.75));
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    private void initFabMenu(){
        //Setando as variáveis
        fabMain             = findViewById(R.id.floatingMain);
        fabNovoPagamento    = findViewById(R.id.floatingNovoPagamento);
        fabNovaVenda        = findViewById(R.id.floatingNovaVenda);
        fabNovoCliente      = findViewById(R.id.floatingNovoCliente);
        linearFabMain       = findViewById(R.id.linearFabMain);

        //Definindo a transparência dos botões filhos
        fabNovoPagamento.setAlpha(0f);
        fabNovaVenda.setAlpha(0f);
        fabNovoCliente.setAlpha(0f);

        //Setando o Translation
        fabNovoPagamento.setTranslationY(translationY);
        fabNovaVenda.setTranslationY(translationY);
        fabNovoCliente.setTranslationY(translationY);

        //Adicionando o OnClickListener
        fabMain.setOnClickListener(this);
        fabNovoPagamento.setOnClickListener(this);
        fabNovaVenda.setOnClickListener(this);
        fabNovoCliente.setOnClickListener(this);
    }

    private void openFabMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotationBy(45f).setDuration(300).start();

        fabNovoPagamento.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabNovaVenda    .animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabNovoCliente  .animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }

    private void closeFabMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabNovoPagamento.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabNovaVenda    .animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabNovoCliente  .animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.floatingMain:
                if (isMenuOpen) {
                    closeFabMenu();
                } else {
                    openFabMenu();
                }
                break;

            case R.id.floatingNovoPagamento:
                if (isMenuOpen) {
                    closeFabMenu();
                } else {
                    openFabMenu();
                }
                break;

            case R.id.floatingNovaVenda:
                if (isMenuOpen) {
                    closeFabMenu();
                } else {
                    openFabMenu();
                }
                break;

            case R.id.floatingNovoCliente:

                if (isMenuOpen) {
                    startActivity(new Intent(MainActivity.this, AddCliente.class));
                    closeFabMenu();
                } else {
                    openFabMenu();
                }
                break;

        }
    }

    private boolean checkFormaPgtos () {
        List<FormaPgto> formaPgtos = db.listFormaPgto();
        return formaPgtos.size() == 0;
    }

    private boolean checkCategorias () {
        List<Categoria> categorias = db.listCategorias();
        return categorias.size() == 0;
    }

    private boolean checkSubcats () {
        List<Subcat> subcats = db.listSubcats();
        return subcats.size() == 0;
    }

    private boolean checkMarcas () {
        List<Marca> marcas = db.listMarcas();
        return marcas.size() == 0;
    }

    private boolean checkLinhas () {
        List<Linha> linhas = db.listLinhas();
        return linhas.size() == 0;
    }

    private boolean checkEstados () {
        List<Estado> estados = db.listAllEstados();
        return estados.size() == 0;
    }

    private boolean checkCidades () {
        List<Cidade> cidades = db.listAllCidades();
        return cidades.size() == 0;
    }

    private void addDefaultFormaPgtos () {
        db.addFormaPgto(new FormaPgto("Dinheiro",           0,  0));
        db.addFormaPgto(new FormaPgto("Cartão de Crédito",  0,  12));
        db.addFormaPgto(new FormaPgto("Cartão de Débito",   0,  0));
        db.addFormaPgto(new FormaPgto("Pix",                0,  0));
    }

    private void addDefaultCategorias () {
        db.addCategoria(new Categoria("Perfumaria",        null));
        db.addCategoria(new Categoria("Maquiagem",         null));
        db.addCategoria(new Categoria("Corpo e banho",     null));
        db.addCategoria(new Categoria("Higiene e Cuidado", null));
        db.addCategoria(new Categoria("Cabelos",           null));
        db.addCategoria(new Categoria("Rosto",             null));
        db.addCategoria(new Categoria("Brinquedos",        null));
        db.addCategoria(new Categoria("Embalagens",        null));
        db.addCategoria(new Categoria("Vestuário",         null));
        db.addCategoria(new Categoria("Roupa Íntima",      null));
        db.addCategoria(new Categoria("Produção Própria",  null));
    }

    private void addDefaultSubcats () {
        //Perfumaria
        db.addSubcat(new Subcat("Colônia",          db.selectCategoria(1)));
        db.addSubcat(new Subcat("Desodorante",      db.selectCategoria(1)));
        db.addSubcat(new Subcat("Deo Parfum",       db.selectCategoria(1)));
        db.addSubcat(new Subcat("Masculino",        db.selectCategoria(1)));
        db.addSubcat(new Subcat("Feminino",         db.selectCategoria(1)));
        db.addSubcat(new Subcat("Unissex",          db.selectCategoria(1)));
        db.addSubcat(new Subcat("Infantil",         db.selectCategoria(1)));

        //Maquiagem
        db.addSubcat(new Subcat("Batom",           db.selectCategoria(2)));
        db.addSubcat(new Subcat("Base",            db.selectCategoria(2)));
        db.addSubcat(new Subcat("Rímel",           db.selectCategoria(2)));
        db.addSubcat(new Subcat("Removedor",       db.selectCategoria(2)));
        db.addSubcat(new Subcat("Delineador",      db.selectCategoria(2)));
        db.addSubcat(new Subcat("Iluminador",      db.selectCategoria(2)));

        //Corpo e banho
        db.addSubcat(new Subcat("Sabonete",        db.selectCategoria(3)));
        db.addSubcat(new Subcat("Sabonete Barra",  db.selectCategoria(3)));
        db.addSubcat(new Subcat("Sabonete Líquido",db.selectCategoria(3)));
        db.addSubcat(new Subcat("Esponja",         db.selectCategoria(3)));
        db.addSubcat(new Subcat("Masculino",       db.selectCategoria(3)));
        db.addSubcat(new Subcat("Feminino",        db.selectCategoria(3)));
        db.addSubcat(new Subcat("Unissex",         db.selectCategoria(3)));
        db.addSubcat(new Subcat("Infantil",        db.selectCategoria(3)));

        //Higiene e Cuidado
        db.addSubcat(new Subcat("Creme",               db.selectCategoria(4)));
        db.addSubcat(new Subcat("Creme Facial",        db.selectCategoria(4)));
        db.addSubcat(new Subcat("Creme Corporal",      db.selectCategoria(4)));
        db.addSubcat(new Subcat("Creme Esfoliante",    db.selectCategoria(4)));
        db.addSubcat(new Subcat("Creme Depilatório",   db.selectCategoria(4)));
        db.addSubcat(new Subcat("Esponja",             db.selectCategoria(4)));
        db.addSubcat(new Subcat("Cera",                db.selectCategoria(4)));
        db.addSubcat(new Subcat("Cera Líquida",        db.selectCategoria(4)));
        db.addSubcat(new Subcat("Cera em Folha",       db.selectCategoria(4)));
        db.addSubcat(new Subcat("Cera em Barra",       db.selectCategoria(4)));
        db.addSubcat(new Subcat("Masculino",           db.selectCategoria(4)));
        db.addSubcat(new Subcat("Feminino",            db.selectCategoria(4)));
        db.addSubcat(new Subcat("Unissex",             db.selectCategoria(4)));
        db.addSubcat(new Subcat("Infantil",            db.selectCategoria(4)));

        //Cabelos
        db.addSubcat(new Subcat("Shampoo",             db.selectCategoria(5)));
        db.addSubcat(new Subcat("Condicionador",       db.selectCategoria(5)));
        db.addSubcat(new Subcat("Máscara",             db.selectCategoria(5)));
        db.addSubcat(new Subcat("Anticaspa",           db.selectCategoria(5)));
        db.addSubcat(new Subcat("Reparador",           db.selectCategoria(5)));
        db.addSubcat(new Subcat("Loiro",               db.selectCategoria(5)));
        db.addSubcat(new Subcat("Moreno",              db.selectCategoria(5)));
        db.addSubcat(new Subcat("Ruivo",               db.selectCategoria(5)));
        db.addSubcat(new Subcat("Branco",              db.selectCategoria(5)));
        db.addSubcat(new Subcat("Masculino",           db.selectCategoria(5)));
        db.addSubcat(new Subcat("Feminino",            db.selectCategoria(5)));
        db.addSubcat(new Subcat("Unissex",             db.selectCategoria(5)));
        db.addSubcat(new Subcat("Infantil",            db.selectCategoria(5)));

        //Rosto
        db.addSubcat(new Subcat("Creme",               db.selectCategoria(6)));
        db.addSubcat(new Subcat("Creme Facial",        db.selectCategoria(6)));
        db.addSubcat(new Subcat("Esponja",             db.selectCategoria(6)));
        db.addSubcat(new Subcat("Sabonete",            db.selectCategoria(6)));
        db.addSubcat(new Subcat("Sabonete Barra",      db.selectCategoria(6)));
        db.addSubcat(new Subcat("Sabonete Líquido",    db.selectCategoria(6)));
        db.addSubcat(new Subcat("Cravos",              db.selectCategoria(6)));
        db.addSubcat(new Subcat("Espinhas",            db.selectCategoria(6)));
        db.addSubcat(new Subcat("Masculino",           db.selectCategoria(6)));
        db.addSubcat(new Subcat("Feminino",            db.selectCategoria(6)));
        db.addSubcat(new Subcat("Unissex",             db.selectCategoria(6)));
        db.addSubcat(new Subcat("Infantil",            db.selectCategoria(6)));

        //Brinquedos
        db.addSubcat(new Subcat("Carro",               db.selectCategoria(7)));
        db.addSubcat(new Subcat("Caminhão",            db.selectCategoria(7)));
        db.addSubcat(new Subcat("Boneca",              db.selectCategoria(7)));
        db.addSubcat(new Subcat("Bloco desmontável",   db.selectCategoria(7)));
        db.addSubcat(new Subcat("Cubo mágico",         db.selectCategoria(7)));
        db.addSubcat(new Subcat("Bola de futebol",     db.selectCategoria(7)));
        db.addSubcat(new Subcat("Masculino",           db.selectCategoria(7)));
        db.addSubcat(new Subcat("Feminino",            db.selectCategoria(7)));
        db.addSubcat(new Subcat("Unissex",             db.selectCategoria(7)));

        //Embalagens
        db.addSubcat(new Subcat("Sacola P",            db.selectCategoria(8)));
        db.addSubcat(new Subcat("Sacola M",            db.selectCategoria(8)));
        db.addSubcat(new Subcat("Sacola G",            db.selectCategoria(8)));
        db.addSubcat(new Subcat("Decorado",            db.selectCategoria(8)));
        db.addSubcat(new Subcat("Seda",                db.selectCategoria(8)));
        db.addSubcat(new Subcat("Faixa",               db.selectCategoria(8)));
        db.addSubcat(new Subcat("Presente",            db.selectCategoria(8)));
        db.addSubcat(new Subcat("Dia dos Pais",        db.selectCategoria(8)));
        db.addSubcat(new Subcat("Dia das Mães",        db.selectCategoria(8)));
        db.addSubcat(new Subcat("Masculino",           db.selectCategoria(8)));
        db.addSubcat(new Subcat("Feminino",            db.selectCategoria(8)));
        db.addSubcat(new Subcat("Unissex",             db.selectCategoria(8)));
        db.addSubcat(new Subcat("Infantil",            db.selectCategoria(8)));

        //Vestuário
        db.addSubcat(new Subcat("Calça",               db.selectCategoria(9)));
        db.addSubcat(new Subcat("Shorts",              db.selectCategoria(9)));
        db.addSubcat(new Subcat("Camiseta",            db.selectCategoria(9)));
        db.addSubcat(new Subcat("Camisa",              db.selectCategoria(9)));
        db.addSubcat(new Subcat("Social",              db.selectCategoria(9)));
        db.addSubcat(new Subcat("Esportivo",           db.selectCategoria(9)));
        db.addSubcat(new Subcat("Casual",              db.selectCategoria(9)));
        db.addSubcat(new Subcat("Meia",                db.selectCategoria(9)));
        db.addSubcat(new Subcat("Blusa",               db.selectCategoria(9)));
        db.addSubcat(new Subcat("Bordado",             db.selectCategoria(9)));
        db.addSubcat(new Subcat("Nylon",               db.selectCategoria(9)));
        db.addSubcat(new Subcat("Algodão",             db.selectCategoria(9)));
        db.addSubcat(new Subcat("Masculino",           db.selectCategoria(9)));
        db.addSubcat(new Subcat("Feminino",            db.selectCategoria(9)));
        db.addSubcat(new Subcat("Unissex",             db.selectCategoria(9)));
        db.addSubcat(new Subcat("Infantil",            db.selectCategoria(9)));

        //Roupa Íntima
        db.addSubcat(new Subcat("Cueca",               db.selectCategoria(10)));
        db.addSubcat(new Subcat("Calcinha",            db.selectCategoria(10)));
        db.addSubcat(new Subcat("Sutiâ",               db.selectCategoria(10)));
        db.addSubcat(new Subcat("Bordado",             db.selectCategoria(10)));
        db.addSubcat(new Subcat("Nylon",               db.selectCategoria(10)));
        db.addSubcat(new Subcat("Algodão",             db.selectCategoria(10)));
        db.addSubcat(new Subcat("Masculino",           db.selectCategoria(10)));
        db.addSubcat(new Subcat("Feminino",            db.selectCategoria(10)));
        db.addSubcat(new Subcat("Unissex",             db.selectCategoria(10)));
        db.addSubcat(new Subcat("Infantil",            db.selectCategoria(10)));

        //Produção Própria
        db.addSubcat(new Subcat("Masculino",           db.selectCategoria(11)));
        db.addSubcat(new Subcat("Feminino",            db.selectCategoria(11)));
        db.addSubcat(new Subcat("Unissex",             db.selectCategoria(11)));
        db.addSubcat(new Subcat("Infantil",            db.selectCategoria(11)));

    }

    private void addDefaultMarcas () {
        db.addMarca(new Marca("Natura"));
        db.addMarca(new Marca("Boticário"));
        db.addMarca(new Marca("Oakley"));
        db.addMarca(new Marca("Adidas"));
        db.addMarca(new Marca("Shein"));
        db.addMarca(new Marca("Vivara"));
        db.addMarca(new Marca("Lolja"));
        db.addMarca(new Marca("GAP"));
        db.addMarca(new Marca("Cherry"));
    }

    private void addDefaultLinhas () {
        // Natura
        db.addLinha(new Linha("Kaiak",     db.selectMarca(1)));
        db.addLinha(new Linha("Essencial", db.selectMarca(1)));
        db.addLinha(new Linha("Faces",     db.selectMarca(1)));
        db.addLinha(new Linha("Homem",     db.selectMarca(1)));
        db.addLinha(new Linha("Biografia", db.selectMarca(1)));
        db.addLinha(new Linha("Ekos",      db.selectMarca(1)));
        db.addLinha(new Linha("Kaiak",     db.selectMarca(1)));
        db.addLinha(new Linha("Tododia",   db.selectMarca(1)));
        db.addLinha(new Linha("Sou",       db.selectMarca(1)));

        // Boticário
        db.addLinha(new Linha("Acordes",   db.selectMarca(2)));
        db.addLinha(new Linha("Malbec",   db.selectMarca(2)));
        db.addLinha(new Linha("Capricho",   db.selectMarca(2)));
        db.addLinha(new Linha("Egeo",   db.selectMarca(2)));
        db.addLinha(new Linha("Lily",   db.selectMarca(2)));
        db.addLinha(new Linha("Portinari",   db.selectMarca(2)));
        db.addLinha(new Linha("Quasar",   db.selectMarca(2)));
    }

    private void addDefaultEstados () {
        db.addEstado(new Estado(1, "Acre", "AC"));
        db.addEstado(new Estado(2, "Alagoas", "AL"));
        db.addEstado(new Estado(3, "Amazonas", "AM"));
        db.addEstado(new Estado(4, "Amapá", "AP"));
        db.addEstado(new Estado(5, "Bahia", "BA"));
        db.addEstado(new Estado(6, "Ceará", "CE"));
        db.addEstado(new Estado(7, "Distrito Federal", "DF"));
        db.addEstado(new Estado(8, "Espírito Santo", "ES"));
        db.addEstado(new Estado(9, "Goiás", "GO"));
        db.addEstado(new Estado(10, "Maranhão", "MA"));
        db.addEstado(new Estado(11, "Minas Gerais", "MG"));
        db.addEstado(new Estado(12, "Mato Grosso do Sul", "MS"));
        db.addEstado(new Estado(13, "Mato Grosso", "MT"));
        db.addEstado(new Estado(14, "Pará", "PA"));
        db.addEstado(new Estado(15, "Paraíba", "PB"));
        db.addEstado(new Estado(16, "Pernambuco", "PE"));
        db.addEstado(new Estado(17, "Piauí", "PI"));
        db.addEstado(new Estado(18, "Paraná", "PR"));
        db.addEstado(new Estado(19, "Rio de Janeiro", "RJ"));
        db.addEstado(new Estado(20, "Rio Grande do Norte", "RN"));
        db.addEstado(new Estado(21, "Rondônia", "RO"));
        db.addEstado(new Estado(22, "Roraima", "RR"));
        db.addEstado(new Estado(23, "Rio Grande do Sul", "RS"));
        db.addEstado(new Estado(24, "Santa Catarina", "SC"));
        db.addEstado(new Estado(25, "Sergipe", "SE"));
        db.addEstado(new Estado(26, "São Paulo", "SP"));
        db.addEstado(new Estado(27, "Tocantins", "TO"));
    }

    private void addDefaultCidades () {

        // Adicionando cidades do estado de Paraná - ID 18

        try {
            //Log.i("INFO CIDADES TRY CIDADES", "INICIANDO INSERT Nº " + i);

            db.addCidade(new Cidade(2784, "Abatiá", db.selectEstado(18)));
            db.addCidade(new Cidade(2785, "Adrianópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2786, "Agudos do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2787, "Almirante Tamandaré", db.selectEstado(18)));
            db.addCidade(new Cidade(2809, "Assis Chateaubriand", db.selectEstado(18)));
            db.addCidade(new Cidade(2810, "Astorga", db.selectEstado(18)));
            db.addCidade(new Cidade(2811, "Atalaia", db.selectEstado(18)));
            db.addCidade(new Cidade(2812, "Balsa Nova", db.selectEstado(18)));
            db.addCidade(new Cidade(2813, "Bandeirantes", db.selectEstado(18)));
            db.addCidade(new Cidade(2814, "Barbosa Ferraz", db.selectEstado(18)));
            db.addCidade(new Cidade(2815, "Barra do Jacaré", db.selectEstado(18)));
            db.addCidade(new Cidade(2816, "Barracão", db.selectEstado(18)));
            db.addCidade(new Cidade(2817, "Bela Vista da Caroba", db.selectEstado(18)));
            db.addCidade(new Cidade(2818, "Bela Vista do Paraíso", db.selectEstado(18)));
            db.addCidade(new Cidade(2819, "Bituruna", db.selectEstado(18)));
            db.addCidade(new Cidade(2820, "Boa Esperança", db.selectEstado(18)));
            db.addCidade(new Cidade(2821, "Boa Esperança do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(2822, "Boa Ventura de São Roque", db.selectEstado(18)));
            db.addCidade(new Cidade(2823, "Boa Vista da Aparecida", db.selectEstado(18)));
            db.addCidade(new Cidade(2824, "Bocaiúva do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2825, "Bom Jesus do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2826, "Bom Sucesso", db.selectEstado(18)));
            db.addCidade(new Cidade(2827, "Bom Sucesso do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2828, "Borrazópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2829, "Braganey", db.selectEstado(18)));
            db.addCidade(new Cidade(2830, "Brasilândia do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2831, "Cafeara", db.selectEstado(18)));
            db.addCidade(new Cidade(2832, "Cafelândia", db.selectEstado(18)));
            db.addCidade(new Cidade(2833, "Cafezal do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2834, "Califórnia", db.selectEstado(18)));
            db.addCidade(new Cidade(2835, "Cambará", db.selectEstado(18)));
            db.addCidade(new Cidade(2836, "Cambé", db.selectEstado(18)));
            db.addCidade(new Cidade(2837, "Cambira", db.selectEstado(18)));
            db.addCidade(new Cidade(2838, "Campina da Lagoa", db.selectEstado(18)));
            db.addCidade(new Cidade(2839, "Campina do Simão", db.selectEstado(18)));
            db.addCidade(new Cidade(2840, "Campina Grande do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2841, "Campo Bonito", db.selectEstado(18)));
            db.addCidade(new Cidade(2842, "Campo do Tenente", db.selectEstado(18)));
            db.addCidade(new Cidade(2843, "Campo Largo", db.selectEstado(18)));
            db.addCidade(new Cidade(2844, "Campo Magro", db.selectEstado(18)));
            db.addCidade(new Cidade(2845, "Campo Mourão", db.selectEstado(18)));
            db.addCidade(new Cidade(2846, "Cândido de Abreu", db.selectEstado(18)));
            db.addCidade(new Cidade(2847, "Candói", db.selectEstado(18)));
            db.addCidade(new Cidade(2848, "Cantagalo", db.selectEstado(18)));
            db.addCidade(new Cidade(2849, "Capanema", db.selectEstado(18)));
            db.addCidade(new Cidade(2850, "Capitão Leônidas Marques", db.selectEstado(18)));
            db.addCidade(new Cidade(2851, "Carambeí", db.selectEstado(18)));
            db.addCidade(new Cidade(2852, "Carlópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2853, "Cascavel", db.selectEstado(18)));
            db.addCidade(new Cidade(2854, "Castro", db.selectEstado(18)));
            db.addCidade(new Cidade(2855, "Catanduvas", db.selectEstado(18)));
            db.addCidade(new Cidade(2856, "Centenário do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2857, "Cerro Azul", db.selectEstado(18)));
            db.addCidade(new Cidade(2858, "Céu Azul", db.selectEstado(18)));
            db.addCidade(new Cidade(2859, "Chopinzinho", db.selectEstado(18)));
            db.addCidade(new Cidade(2860, "Cianorte", db.selectEstado(18)));
            db.addCidade(new Cidade(2861, "Cidade Gaúcha", db.selectEstado(18)));
            db.addCidade(new Cidade(2862, "Clevelândia", db.selectEstado(18)));
            db.addCidade(new Cidade(2863, "Colombo", db.selectEstado(18)));
            db.addCidade(new Cidade(2864, "Colorado", db.selectEstado(18)));
            db.addCidade(new Cidade(2865, "Congonhinhas", db.selectEstado(18)));
            db.addCidade(new Cidade(2866, "Conselheiro Mairinck", db.selectEstado(18)));
            db.addCidade(new Cidade(2867, "Contenda", db.selectEstado(18)));
            db.addCidade(new Cidade(2868, "Corbélia", db.selectEstado(18)));
            db.addCidade(new Cidade(2869, "Cornélio Procópio", db.selectEstado(18)));
            db.addCidade(new Cidade(2870, "Coronel Domingos Soares", db.selectEstado(18)));
            db.addCidade(new Cidade(2871, "Coronel Vivida", db.selectEstado(18)));
            db.addCidade(new Cidade(2872, "Corumbataí do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2873, "Cruz Machado", db.selectEstado(18)));
            db.addCidade(new Cidade(2874, "Cruzeiro do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(2875, "Cruzeiro do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(2876, "Cruzeiro do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2877, "Cruzmaltina", db.selectEstado(18)));
            db.addCidade(new Cidade(2878, "Curitiba", db.selectEstado(18)));
            db.addCidade(new Cidade(2879, "Curiúva", db.selectEstado(18)));
            db.addCidade(new Cidade(2880, "Diamante d`Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(2881, "Diamante do Norte", db.selectEstado(18)));
            db.addCidade(new Cidade(2882, "Diamante do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2883, "Dois Vizinhos", db.selectEstado(18)));
            db.addCidade(new Cidade(2884, "Douradina", db.selectEstado(18)));
            db.addCidade(new Cidade(2885, "Doutor Camargo", db.selectEstado(18)));
            db.addCidade(new Cidade(2886, "Doutor Ulysses", db.selectEstado(18)));
            db.addCidade(new Cidade(2887, "Enéas Marques", db.selectEstado(18)));
            db.addCidade(new Cidade(2888, "Engenheiro Beltrão", db.selectEstado(18)));
            db.addCidade(new Cidade(2889, "Entre Rios do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(2890, "Esperança Nova", db.selectEstado(18)));
            db.addCidade(new Cidade(2891, "Espigão Alto do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(2892, "Farol", db.selectEstado(18)));
            db.addCidade(new Cidade(2893, "Faxinal", db.selectEstado(18)));
            db.addCidade(new Cidade(2894, "Fazenda Rio Grande", db.selectEstado(18)));
            db.addCidade(new Cidade(2895, "Fênix", db.selectEstado(18)));
            db.addCidade(new Cidade(2896, "Fernandes Pinheiro", db.selectEstado(18)));
            db.addCidade(new Cidade(2897, "Figueira", db.selectEstado(18)));
            db.addCidade(new Cidade(2898, "Flor da Serra do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2899, "Floraí", db.selectEstado(18)));
            db.addCidade(new Cidade(2900, "Floresta", db.selectEstado(18)));
            db.addCidade(new Cidade(2901, "Florestópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2902, "Flórida", db.selectEstado(18)));
            db.addCidade(new Cidade(2903, "Formosa do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(2904, "Foz do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(2905, "Foz do Jordão", db.selectEstado(18)));
            db.addCidade(new Cidade(2906, "Francisco Alves", db.selectEstado(18)));
            db.addCidade(new Cidade(2907, "Francisco Beltrão", db.selectEstado(18)));
            db.addCidade(new Cidade(2908, "General Carneiro", db.selectEstado(18)));
            db.addCidade(new Cidade(2909, "Godoy Moreira", db.selectEstado(18)));
            db.addCidade(new Cidade(2910, "Goioerê", db.selectEstado(18)));
            db.addCidade(new Cidade(2911, "Goioxim", db.selectEstado(18)));
            db.addCidade(new Cidade(2912, "Grandes Rios", db.selectEstado(18)));
            db.addCidade(new Cidade(2913, "Guaíra", db.selectEstado(18)));
            db.addCidade(new Cidade(2914, "Guairaçá", db.selectEstado(18)));
            db.addCidade(new Cidade(2915, "Guamiranga", db.selectEstado(18)));
            db.addCidade(new Cidade(2916, "Guapirama", db.selectEstado(18)));
            db.addCidade(new Cidade(2917, "Guaporema", db.selectEstado(18)));
            db.addCidade(new Cidade(2918, "Guaraci", db.selectEstado(18)));
            db.addCidade(new Cidade(2919, "Guaraniaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(2920, "Guarapuava", db.selectEstado(18)));
            db.addCidade(new Cidade(2921, "Guaraqueçaba", db.selectEstado(18)));
            db.addCidade(new Cidade(2922, "Guaratuba", db.selectEstado(18)));
            db.addCidade(new Cidade(2923, "Honório Serpa", db.selectEstado(18)));
            db.addCidade(new Cidade(2924, "Ibaiti", db.selectEstado(18)));
            db.addCidade(new Cidade(2925, "Ibema", db.selectEstado(18)));
            db.addCidade(new Cidade(2926, "Ibiporã", db.selectEstado(18)));
            db.addCidade(new Cidade(2927, "Icaraíma", db.selectEstado(18)));
            db.addCidade(new Cidade(2928, "Iguaraçu", db.selectEstado(18)));
            db.addCidade(new Cidade(2929, "Iguatu", db.selectEstado(18)));
            db.addCidade(new Cidade(2930, "Imbaú", db.selectEstado(18)));
            db.addCidade(new Cidade(2931, "Imbituva", db.selectEstado(18)));
            db.addCidade(new Cidade(2932, "Inácio Martins", db.selectEstado(18)));
            db.addCidade(new Cidade(2933, "Inajá", db.selectEstado(18)));
            db.addCidade(new Cidade(2934, "Indianópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2935, "Ipiranga", db.selectEstado(18)));
            db.addCidade(new Cidade(2936, "Iporã", db.selectEstado(18)));
            db.addCidade(new Cidade(2937, "Iracema do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(2938, "Irati", db.selectEstado(18)));
            db.addCidade(new Cidade(2939, "Iretama", db.selectEstado(18)));
            db.addCidade(new Cidade(2940, "Itaguajé", db.selectEstado(18)));
            db.addCidade(new Cidade(2941, "Itaipulândia", db.selectEstado(18)));
            db.addCidade(new Cidade(2942, "Itambaracá", db.selectEstado(18)));
            db.addCidade(new Cidade(2943, "Itambé", db.selectEstado(18)));
            db.addCidade(new Cidade(2944, "Itapejara d`Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(2945, "Itaperuçu", db.selectEstado(18)));
            db.addCidade(new Cidade(2946, "Itaúna do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2947, "Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(2948, "Ivaiporã", db.selectEstado(18)));
            db.addCidade(new Cidade(2949, "Ivaté", db.selectEstado(18)));
            db.addCidade(new Cidade(2950, "Ivatuba", db.selectEstado(18)));
            db.addCidade(new Cidade(2951, "Jaboti", db.selectEstado(18)));
            db.addCidade(new Cidade(2952, "Jacarezinho", db.selectEstado(18)));
            db.addCidade(new Cidade(2953, "Jaguapitã", db.selectEstado(18)));
            db.addCidade(new Cidade(2954, "Jaguariaíva", db.selectEstado(18)));
            db.addCidade(new Cidade(2955, "Jandaia do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2956, "Janiópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2957, "Japira", db.selectEstado(18)));
            db.addCidade(new Cidade(2958, "Japurá", db.selectEstado(18)));
            db.addCidade(new Cidade(2959, "Jardim Alegre", db.selectEstado(18)));
            db.addCidade(new Cidade(2960, "Jardim Olinda", db.selectEstado(18)));
            db.addCidade(new Cidade(2961, "Jataizinho", db.selectEstado(18)));
            db.addCidade(new Cidade(2962, "Jesuítas", db.selectEstado(18)));
            db.addCidade(new Cidade(2963, "Joaquim Távora", db.selectEstado(18)));
            db.addCidade(new Cidade(2964, "Jundiaí do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2965, "Juranda", db.selectEstado(18)));
            db.addCidade(new Cidade(2966, "Jussara", db.selectEstado(18)));
            db.addCidade(new Cidade(2967, "Kaloré", db.selectEstado(18)));
            db.addCidade(new Cidade(2968, "Lapa", db.selectEstado(18)));
            db.addCidade(new Cidade(2969, "Laranjal", db.selectEstado(18)));
            db.addCidade(new Cidade(2970, "Laranjeiras do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2971, "Leópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2972, "Lidianópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2973, "Lindoeste", db.selectEstado(18)));
            db.addCidade(new Cidade(2974, "Loanda", db.selectEstado(18)));
            db.addCidade(new Cidade(2975, "Lobato", db.selectEstado(18)));
            db.addCidade(new Cidade(2976, "Londrina", db.selectEstado(18)));
            db.addCidade(new Cidade(2977, "Luiziana", db.selectEstado(18)));
            db.addCidade(new Cidade(2978, "Lunardelli", db.selectEstado(18)));
            db.addCidade(new Cidade(2979, "Lupionópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2980, "Mallet", db.selectEstado(18)));
            db.addCidade(new Cidade(2981, "Mamborê", db.selectEstado(18)));
            db.addCidade(new Cidade(2982, "Mandaguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(2983, "Mandaguari", db.selectEstado(18)));
            db.addCidade(new Cidade(2984, "Mandirituba", db.selectEstado(18)));
            db.addCidade(new Cidade(2985, "Manfrinópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2986, "Mangueirinha", db.selectEstado(18)));
            db.addCidade(new Cidade(2987, "Manoel Ribas", db.selectEstado(18)));
            db.addCidade(new Cidade(2988, "Marechal Cândido Rondon", db.selectEstado(18)));
            db.addCidade(new Cidade(2989, "Maria Helena", db.selectEstado(18)));
            db.addCidade(new Cidade(2990, "Marialva", db.selectEstado(18)));
            db.addCidade(new Cidade(2991, "Marilândia do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(2992, "Marilena", db.selectEstado(18)));
            db.addCidade(new Cidade(2993, "Mariluz", db.selectEstado(18)));
            db.addCidade(new Cidade(2994, "Maringá", db.selectEstado(18)));
            db.addCidade(new Cidade(2995, "Mariópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(2996, "Maripá", db.selectEstado(18)));
            db.addCidade(new Cidade(2997, "Marmeleiro", db.selectEstado(18)));
            db.addCidade(new Cidade(2998, "Marquinho", db.selectEstado(18)));
            db.addCidade(new Cidade(2999, "Marumbi", db.selectEstado(18)));
            db.addCidade(new Cidade(3000, "Matelândia", db.selectEstado(18)));
            db.addCidade(new Cidade(3001, "Matinhos", db.selectEstado(18)));
            db.addCidade(new Cidade(3002, "Mato Rico", db.selectEstado(18)));
            db.addCidade(new Cidade(3003, "Mauá da Serra", db.selectEstado(18)));
            db.addCidade(new Cidade(3004, "Medianeira", db.selectEstado(18)));
            db.addCidade(new Cidade(3005, "Mercedes", db.selectEstado(18)));
            db.addCidade(new Cidade(3006, "Mirador", db.selectEstado(18)));
            db.addCidade(new Cidade(3007, "Miraselva", db.selectEstado(18)));
            db.addCidade(new Cidade(3008, "Missal", db.selectEstado(18)));
            db.addCidade(new Cidade(3009, "Moreira Sales", db.selectEstado(18)));
            db.addCidade(new Cidade(3010, "Morretes", db.selectEstado(18)));
            db.addCidade(new Cidade(3011, "Munhoz de Melo", db.selectEstado(18)));
            db.addCidade(new Cidade(3012, "Nossa Senhora das Graças", db.selectEstado(18)));
            db.addCidade(new Cidade(3013, "Nova Aliança do Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3014, "Nova América da Colina", db.selectEstado(18)));
            db.addCidade(new Cidade(3015, "Nova Aurora", db.selectEstado(18)));
            db.addCidade(new Cidade(3016, "Nova Cantu", db.selectEstado(18)));
            db.addCidade(new Cidade(3017, "Nova Esperança", db.selectEstado(18)));
            db.addCidade(new Cidade(3018, "Nova Esperança do Sudoeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3019, "Nova Fátima", db.selectEstado(18)));
            db.addCidade(new Cidade(3020, "Nova Laranjeiras", db.selectEstado(18)));
            db.addCidade(new Cidade(3021, "Nova Londrina", db.selectEstado(18)));
            db.addCidade(new Cidade(3022, "Nova Olímpia", db.selectEstado(18)));
            db.addCidade(new Cidade(3023, "Nova Prata do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(3024, "Nova Santa Bárbara", db.selectEstado(18)));
            db.addCidade(new Cidade(3025, "Nova Santa Rosa", db.selectEstado(18)));
            db.addCidade(new Cidade(3026, "Nova Tebas", db.selectEstado(18)));
            db.addCidade(new Cidade(3027, "Novo Itacolomi", db.selectEstado(18)));
            db.addCidade(new Cidade(3028, "Ortigueira", db.selectEstado(18)));
            db.addCidade(new Cidade(3029, "Ourizona", db.selectEstado(18)));
            db.addCidade(new Cidade(3030, "Ouro Verde do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3031, "Paiçandu", db.selectEstado(18)));
            db.addCidade(new Cidade(3032, "Palmas", db.selectEstado(18)));
            db.addCidade(new Cidade(3033, "Palmeira", db.selectEstado(18)));
            db.addCidade(new Cidade(3034, "Palmital", db.selectEstado(18)));
            db.addCidade(new Cidade(3035, "Palotina", db.selectEstado(18)));
            db.addCidade(new Cidade(3036, "Paraíso do Norte", db.selectEstado(18)));
            db.addCidade(new Cidade(3037, "Paranacity", db.selectEstado(18)));
            db.addCidade(new Cidade(3038, "Paranaguá", db.selectEstado(18)));
            db.addCidade(new Cidade(3039, "Paranapoema", db.selectEstado(18)));
            db.addCidade(new Cidade(3040, "Paranavaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3041, "Pato Bragado", db.selectEstado(18)));
            db.addCidade(new Cidade(3042, "Pato Branco", db.selectEstado(18)));
            db.addCidade(new Cidade(3043, "Paula Freitas", db.selectEstado(18)));
            db.addCidade(new Cidade(3044, "Paulo Frontin", db.selectEstado(18)));
            db.addCidade(new Cidade(3045, "Peabiru", db.selectEstado(18)));
            db.addCidade(new Cidade(3046, "Perobal", db.selectEstado(18)));
            db.addCidade(new Cidade(3047, "Pérola", db.selectEstado(18)));
            db.addCidade(new Cidade(3048, "Pérola d`Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3049, "Piên", db.selectEstado(18)));
            db.addCidade(new Cidade(3050, "Pinhais", db.selectEstado(18)));
            db.addCidade(new Cidade(3051, "Pinhal de São Bento", db.selectEstado(18)));
            db.addCidade(new Cidade(3052, "Pinhalão", db.selectEstado(18)));
            db.addCidade(new Cidade(3053, "Pinhão", db.selectEstado(18)));
            db.addCidade(new Cidade(3054, "Piraí do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(3055, "Piraquara", db.selectEstado(18)));
            db.addCidade(new Cidade(3056, "Pitanga", db.selectEstado(18)));
            db.addCidade(new Cidade(3057, "Pitangueiras", db.selectEstado(18)));
            db.addCidade(new Cidade(3058, "Planaltina do Paraná", db.selectEstado(18)));
            db.addCidade(new Cidade(3059, "Planalto", db.selectEstado(18)));
            db.addCidade(new Cidade(3060, "Ponta Grossa", db.selectEstado(18)));
            db.addCidade(new Cidade(3061, "Pontal do Paraná", db.selectEstado(18)));
            db.addCidade(new Cidade(3062, "Porecatu", db.selectEstado(18)));
            db.addCidade(new Cidade(3063, "Porto Amazonas", db.selectEstado(18)));
            db.addCidade(new Cidade(3064, "Porto Barreiro", db.selectEstado(18)));
            db.addCidade(new Cidade(3065, "Porto Rico", db.selectEstado(18)));
            db.addCidade(new Cidade(3066, "Porto Vitória", db.selectEstado(18)));
            db.addCidade(new Cidade(3067, "Prado Ferreira", db.selectEstado(18)));
            db.addCidade(new Cidade(3068, "Pranchita", db.selectEstado(18)));
            db.addCidade(new Cidade(3069, "Presidente Castelo Branco", db.selectEstado(18)));
            db.addCidade(new Cidade(3070, "Primeiro de Maio", db.selectEstado(18)));
            db.addCidade(new Cidade(3071, "Prudentópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(3072, "Quarto Centenário", db.selectEstado(18)));
            db.addCidade(new Cidade(3073, "Quatiguá", db.selectEstado(18)));
            db.addCidade(new Cidade(3074, "Quatro Barras", db.selectEstado(18)));
            db.addCidade(new Cidade(3075, "Quatro Pontes", db.selectEstado(18)));
            db.addCidade(new Cidade(3076, "Quedas do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(3077, "Querência do Norte", db.selectEstado(18)));
            db.addCidade(new Cidade(3078, "Quinta do Sol", db.selectEstado(18)));
            db.addCidade(new Cidade(3079, "Quitandinha", db.selectEstado(18)));
            db.addCidade(new Cidade(3080, "Ramilândia", db.selectEstado(18)));
            db.addCidade(new Cidade(3081, "Rancho Alegre", db.selectEstado(18)));
            db.addCidade(new Cidade(3082, "Rancho Alegre d`Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3083, "Realeza", db.selectEstado(18)));
            db.addCidade(new Cidade(3084, "Rebouças", db.selectEstado(18)));
            db.addCidade(new Cidade(3085, "Renascença", db.selectEstado(18)));
            db.addCidade(new Cidade(3086, "Reserva", db.selectEstado(18)));
            db.addCidade(new Cidade(3087, "Reserva do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(3088, "Ribeirão Claro", db.selectEstado(18)));
            db.addCidade(new Cidade(3089, "Ribeirão do Pinhal", db.selectEstado(18)));
            db.addCidade(new Cidade(3090, "Rio Azul", db.selectEstado(18)));
            db.addCidade(new Cidade(3091, "Rio Bom", db.selectEstado(18)));
            db.addCidade(new Cidade(3092, "Rio Bonito do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(3093, "Rio Branco do Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3094, "Rio Branco do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(3095, "Rio Negro", db.selectEstado(18)));
            db.addCidade(new Cidade(3096, "Rolândia", db.selectEstado(18)));
            db.addCidade(new Cidade(3097, "Roncador", db.selectEstado(18)));
            db.addCidade(new Cidade(3098, "Rondon", db.selectEstado(18)));
            db.addCidade(new Cidade(3099, "Rosário do Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3100, "Sabáudia", db.selectEstado(18)));
            db.addCidade(new Cidade(3101, "Salgado Filho", db.selectEstado(18)));
            db.addCidade(new Cidade(3102, "Salto do Itararé", db.selectEstado(18)));
            db.addCidade(new Cidade(3103, "Salto do Lontra", db.selectEstado(18)));
            db.addCidade(new Cidade(3104, "Santa Amélia", db.selectEstado(18)));
            db.addCidade(new Cidade(3105, "Santa Cecília do Pavão", db.selectEstado(18)));
            db.addCidade(new Cidade(3106, "Santa Cruz de Monte Castelo", db.selectEstado(18)));
            db.addCidade(new Cidade(3107, "Santa Fé", db.selectEstado(18)));
            db.addCidade(new Cidade(3108, "Santa Helena", db.selectEstado(18)));
            db.addCidade(new Cidade(3109, "Santa Inês", db.selectEstado(18)));
            db.addCidade(new Cidade(3110, "Santa Isabel do Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3111, "Santa Izabel do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3112, "Santa Lúcia", db.selectEstado(18)));
            db.addCidade(new Cidade(3113, "Santa Maria do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3114, "Santa Mariana", db.selectEstado(18)));
            db.addCidade(new Cidade(3115, "Santa Mônica", db.selectEstado(18)));
            db.addCidade(new Cidade(3116, "Santa Tereza do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3117, "Santa Terezinha de Itaipu", db.selectEstado(18)));
            db.addCidade(new Cidade(3118, "Santana do Itararé", db.selectEstado(18)));
            db.addCidade(new Cidade(3119, "Santo Antônio da Platina", db.selectEstado(18)));
            db.addCidade(new Cidade(3120, "Santo Antônio do Caiuá", db.selectEstado(18)));
            db.addCidade(new Cidade(3121, "Santo Antônio do Paraíso", db.selectEstado(18)));
            db.addCidade(new Cidade(3122, "Santo Antônio do Sudoeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3123, "Santo Inácio", db.selectEstado(18)));
            db.addCidade(new Cidade(3124, "São Carlos do Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3125, "São Jerônimo da Serra", db.selectEstado(18)));
            db.addCidade(new Cidade(3126, "São João", db.selectEstado(18)));
            db.addCidade(new Cidade(3127, "São João do Caiuá", db.selectEstado(18)));
            db.addCidade(new Cidade(3128, "São João do Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3129, "São João do Triunfo", db.selectEstado(18)));
            db.addCidade(new Cidade(3130, "São Jorge d`Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3131, "São Jorge do Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3132, "São Jorge do Patrocínio", db.selectEstado(18)));
            db.addCidade(new Cidade(3133, "São José da Boa Vista", db.selectEstado(18)));
            db.addCidade(new Cidade(3134, "São José das Palmeiras", db.selectEstado(18)));
            db.addCidade(new Cidade(3135, "São José dos Pinhais", db.selectEstado(18)));
            db.addCidade(new Cidade(3136, "São Manoel do Paraná", db.selectEstado(18)));
            db.addCidade(new Cidade(3137, "São Mateus do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(3138, "São Miguel do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(3139, "São Pedro do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(3140, "São Pedro do Ivaí", db.selectEstado(18)));
            db.addCidade(new Cidade(3141, "São Pedro do Paraná", db.selectEstado(18)));
            db.addCidade(new Cidade(3142, "São Sebastião da Amoreira", db.selectEstado(18)));
            db.addCidade(new Cidade(3143, "São Tomé", db.selectEstado(18)));
            db.addCidade(new Cidade(3144, "Sapopema", db.selectEstado(18)));
            db.addCidade(new Cidade(3145, "Sarandi", db.selectEstado(18)));
            db.addCidade(new Cidade(3146, "Saudade do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(3147, "Sengés", db.selectEstado(18)));
            db.addCidade(new Cidade(3148, "Serranópolis do Iguaçu", db.selectEstado(18)));
            db.addCidade(new Cidade(3149, "Sertaneja", db.selectEstado(18)));
            db.addCidade(new Cidade(3150, "Sertanópolis", db.selectEstado(18)));
            db.addCidade(new Cidade(3151, "Siqueira Campos", db.selectEstado(18)));
            db.addCidade(new Cidade(3152, "Sulina", db.selectEstado(18)));
            db.addCidade(new Cidade(3153, "Tamarana", db.selectEstado(18)));
            db.addCidade(new Cidade(3154, "Tamboara", db.selectEstado(18)));
            db.addCidade(new Cidade(3155, "Tapejara", db.selectEstado(18)));
            db.addCidade(new Cidade(3156, "Tapira", db.selectEstado(18)));
            db.addCidade(new Cidade(3157, "Teixeira Soares", db.selectEstado(18)));
            db.addCidade(new Cidade(3158, "Telêmaco Borba", db.selectEstado(18)));
            db.addCidade(new Cidade(3159, "Terra Boa", db.selectEstado(18)));
            db.addCidade(new Cidade(3160, "Terra Rica", db.selectEstado(18)));
            db.addCidade(new Cidade(3161, "Terra Roxa", db.selectEstado(18)));
            db.addCidade(new Cidade(3162, "Tibagi", db.selectEstado(18)));
            db.addCidade(new Cidade(3163, "Tijucas do Sul", db.selectEstado(18)));
            db.addCidade(new Cidade(3164, "Toledo", db.selectEstado(18)));
            db.addCidade(new Cidade(3165, "Tomazina", db.selectEstado(18)));
            db.addCidade(new Cidade(3166, "Três Barras do Paraná", db.selectEstado(18)));
            db.addCidade(new Cidade(3167, "Tunas do Paraná", db.selectEstado(18)));
            db.addCidade(new Cidade(3168, "Tuneiras do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3169, "Tupãssi", db.selectEstado(18)));
            db.addCidade(new Cidade(3170, "Turvo", db.selectEstado(18)));
            db.addCidade(new Cidade(3171, "Ubiratã", db.selectEstado(18)));
            db.addCidade(new Cidade(3172, "Umuarama", db.selectEstado(18)));
            db.addCidade(new Cidade(3173, "União da Vitória", db.selectEstado(18)));
            db.addCidade(new Cidade(3174, "Uniflor", db.selectEstado(18)));
            db.addCidade(new Cidade(3175, "Uraí", db.selectEstado(18)));
            db.addCidade(new Cidade(3176, "Ventania", db.selectEstado(18)));
            db.addCidade(new Cidade(3177, "Vera Cruz do Oeste", db.selectEstado(18)));
            db.addCidade(new Cidade(3178, "Verê", db.selectEstado(18)));
            db.addCidade(new Cidade(3179, "Virmond", db.selectEstado(18)));
            db.addCidade(new Cidade(3180, "Vitorino", db.selectEstado(18)));
            db.addCidade(new Cidade(3181, "Wenceslau Braz", db.selectEstado(18)));
            db.addCidade(new Cidade(3182, "Xambrê", db.selectEstado(18)));
        } catch (Exception e) {
            //Log.e("INFO INSERT CIDADES Nº " + i, e.getMessage());
        }

    }

}