package com.example.tcc_gerenciadordevendas;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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

public class ListClientes extends AppCompatActivity {

    private Button btClienteVoltar;
    private Button btClienteAdicionar;
    private ImageButton imgbtIconeNovoCliente;
    private LinearLayout llAdicionarCliente;
    private ListView listViewClientes;
    private AdapterCliente adapter;
    private GestureDetector gestureDetector;
    private ArrayList<Cliente> listaDinamicaClientes;
    private ArrayList<String> arrayList;

    private static final int LIMITE_SWIPE = 100;
    private static final int LIMITE_VELOCIDADE = 100;

    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVO_CLIENTE = 101;
    public static final int ALTERAR_CLIENTE = 102;

    private int viewCounter = 0;

    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        // Método do Swipe
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diferencaX = e2.getX() - e1.getX();
            if(Math.abs(diferencaX) > LIMITE_SWIPE && Math.abs(velocityX) > LIMITE_VELOCIDADE){
                if(diferencaX > 0){
                    Log.i("MOVIMENTO", "Movimento para a direita");
                }
                else{
                    Log.i("MOVIMENTO", "Movimento para a esquerda");
                }
            }
        return true;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clients_zero);

        initButtonsHub();


        List<Cliente> clientes = db.listAllClientes();
        listaDinamicaClientes = new ArrayList<Cliente>();
        gestureDetector = new GestureDetector(this, gestureListener);

        if (!clientes.isEmpty()) {
            for (Cliente c : clientes) {
                listaDinamicaClientes.add(c);
            }
        }

        adapter = new AdapterCliente(this, 0, listaDinamicaClientes);

        listViewClientes = (ListView) findViewById(R.id.listVClientes);
        listViewClientes.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        listViewClientes.setAdapter(adapter);
        listViewClientes.setOnTouchListener(touchListener);

        listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //openClienteData(i);
                viewCounter = viewCounter + 1000;
                Log.d("WTF", "\n \n \n \n \n Entrou no onItemClick");
                Log.d("WTF", " " + viewCounter);
            }
        });

        imgbtIconeNovoCliente = (ImageButton) findViewById(R.id.imgbtIconeNovoCliente);
        imgbtIconeNovoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCliente();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Alterando dados
        if ((requestCode == ALTERAR_CLIENTE) && (resultCode == RESULT_OK)) {

            int id          = data.getIntExtra("posicao", 0);
            String nome     = data.getStringExtra("nome");
            String telefone = data.getStringExtra("telefone");

            Cliente cliente = new Cliente(nome, telefone);
            db.updateCliente(cliente);

            Cliente clienteMax = db.selectMaxCliente();
            listaDinamicaClientes.set(clienteMax.getId(), clienteMax);

            adapter.notifyDataSetChanged();

        }

        //Criando novos dados
        if ((requestCode == NOVO_CLIENTE) && (resultCode == RESULT_OK)) {

            String nome = data.getStringExtra("nome");
            String telefone = data.getStringExtra("telefone");

            Cliente cliente = new Cliente(nome, telefone);
            db.addCliente(cliente);
            db.close();

            Cliente clienteMax = new Cliente();
            try {
                clienteMax = db.selectMaxCliente();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            db.close();
            listaDinamicaClientes.add(clienteMax);

            adapter.notifyDataSetChanged();

        }
    }

    private void initButtonsHub(){
        btClienteVoltar = findViewById(R.id.btClienteVoltar);

        /*
        btClienteVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListClientes.this, MainActivity.class));
            }
        });

         */
    }

    private void addNewCliente() {
        Intent intent = new Intent(getApplication(), AddCliente.class);
        startActivityForResult(intent, NOVO_CLIENTE);
    }

    private void listClientes(){

    }

    private void openClienteData(int posicao) {

        if (posicao < 0) {
            posicao = listaDinamicaClientes.size() - 1;
        }

        int finalPosicao = posicao;

        Intent intent = new Intent(ListClientes.this, viewCliente.class);
        Bundle bundle = new Bundle();

        bundle.putInt("posicao", finalPosicao);
        intent.putExtras(bundle);

        startActivityForResult(intent, ALTERAR_CLIENTE);
    }

    private void tryOnClickCard() {
        // Não funcionou
        setContentView(R.layout.card_cliente);

        LinearLayout llViewCliente = (LinearLayout) findViewById(R.id.llCardCliente);
        llViewCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("WTF", "\n \n \n \n \n Entro no segundo");
            }
        });

    }
}
