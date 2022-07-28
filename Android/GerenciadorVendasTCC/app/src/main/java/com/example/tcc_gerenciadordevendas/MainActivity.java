package com.example.tcc_gerenciadordevendas;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linearFabMain;
    private FloatingActionButton fabMain,
                                 fabNovoPagamento,
                                 fabNovaVenda,
                                 fabNovoCliente;

    private Button  btCliente,
                    btVenda,
                    btPagamento,
                    btProduto;

    private Float translationY = 100f;
    private Boolean isMenuOpen = false;

    OvershootInterpolator interpolator = new OvershootInterpolator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFabMenu();
        initButtonsHub();

    }

    private void initButtonsHub(){
        btCliente   = findViewById(R.id.btCliente);
        btVenda     = findViewById(R.id.btVenda);
        btPagamento = findViewById(R.id.btPagamento);
        btProduto   = findViewById(R.id.btProduto);

        btCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListaClientes.class));
            }
        });
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

                startActivity(new Intent(MainActivity.this, ListaClientes.class));

                if (isMenuOpen) {
                    closeFabMenu();
                } else {
                    openFabMenu();
                }
                break;

        }
    }

}