package com.wlksilvestre.gerenciadordevendas;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private TextView textTitulo;

    private Float translationY = 100f;
    private Boolean isMenuOpen = false;

    public final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    private final BancoDadosCliente db = new BancoDadosCliente(this);

    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Usuario usuario;


    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.e("INFO USER", currentUser.getEmail());
            try {
                usuario = db.selectUsuarioByUID(currentUser.getUid());
            } catch (Exception e) {
                Log.e("INFO ERROR GET USER", e.getMessage());
            }
        }

        initFabMenu();
        initButtons();
        initButtonsOnClick();
        initTexts();
        initCustomUI();

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

    private void initButtons () {

        btCliente = findViewById(R.id.btCliente);
        btProduto = findViewById(R.id.btProduto);
        btVendas = findViewById(R.id.btVendas);
        btPgtos = findViewById(R.id.btPgtos);
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

}