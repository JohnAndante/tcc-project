package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linearFabMain;
    private FloatingActionButton fabMain,
                                 fabNovoPagamento,
                                 fabNovaVenda,
                                 fabNovoCliente;

    private Button btCliente;

    private Float translationY = 100f;
    private Boolean isMenuOpen = false;

    public final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    BancoDadosCliente db = new BancoDadosCliente(this);

    OvershootInterpolator interpolator = new OvershootInterpolator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFabMenu();
        initButtonsHub();

        Log.e("INFO DEVICE SIZE", String.valueOf(deviceHeight) + " x " + String.valueOf(deviceWidth));

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
                    db.addAllCidades();
                    List<Cidade> cidades = db.listAllCidades();
                    Log.i("INFO ONCLICK MAIN", String.valueOf(cidades.size()));
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

    // INSERT DE DADOS TESTE ////////////////////////////////////////////////////////////////////

    void insertTestData () {

        Cliente c = new Cliente();

        c = new Cliente("João Custódio", "44 9 9956-5032");
        db.addCliente(c);
        db.addTelefone(new Telefone("(44) 9 9956-5032", c));

        c = new Cliente("André Marques", "42 9 8645-9833");
        db.addCliente(c);
        db.addTelefone(new Telefone("(42) 9 8645-9833", c));

        c = new Cliente("Lurdes Pereira", "44 9 9953-5324");
        db.addCliente(c);
        db.addTelefone(new Telefone("(44) 9 9953-5324", c));

        c = new Cliente("Maria Antonieta", "44 9 9123-5436");
        db.addCliente(c);
        db.addTelefone(new Telefone("(44) 9 9123-5436", c));

    }


}