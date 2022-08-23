package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linearFabMain;
    private FloatingActionButton fabMain,
                                 fabNovoPagamento,
                                 fabNovaVenda,
                                 fabNovoCliente;

    private Button btCliente;

    private Float translationY = 100f;
    private Boolean isMenuOpen = false;

    BancoDadosCliente db = new BancoDadosCliente(this);

    OvershootInterpolator interpolator = new OvershootInterpolator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFabMenu();
        initButtonsHub();


        /* TESTE DO CRUD */

        // INSERT OK
        // db.addCliente(new Cliente("Angelo Corsa","4499969696"));
        // db.addCliente(new Cliente("Mario Bro","4499969696"));
        // db.addCliente(new Cliente("Bro mario","4499969696"));
        // Toast.makeText(MainActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();


        // DELETE
        // Cliente cliente = new Cliente();
        // cliente.setId(3);
        // db.deleteCliente(cliente);

        // Toast.makeText(MainActivity.this, "Apagado com sucesso", Toast.LENGTH_SHORT).show();


        // SELECT
        // Cliente cliente = db.selectCliente(4);
        //
        // Log.d("Cliente Selecionado", "Codigo: " + cliente.getId());
        // Log.d("Cliente Selecionado", "Nome: " + cliente.getNome());
        // Log.d("Cliente Selecionado", "Telefone: " + cliente.getTelefone());
        //
        // Toast.makeText(MainActivity.this, "Selecionado com sucesso", Toast.LENGTH_SHORT).show();

        // UPDATE
        // Cliente cliente = new Cliente();
        // cliente.setId(4);
        // cliente.setNome("Alemão Caolho Monobola");
        // cliente.setTelefone("444444444");
        //
        // db.updateCliente(cliente);
        //
        // Toast.makeText(MainActivity.this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();

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

    private void initButtonsHub(){
        btCliente = findViewById(R.id.btCliente);

        btCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListClientes.class));
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