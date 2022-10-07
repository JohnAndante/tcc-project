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
        //gestureDetector = new GestureDetector(this, gestureListener);

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

}
