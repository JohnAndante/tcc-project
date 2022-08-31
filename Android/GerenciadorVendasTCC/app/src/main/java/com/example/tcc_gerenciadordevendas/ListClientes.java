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
    public static final int CONSULTAR_CLIENTE = 103;
    public static final int RESULT_ALT_CLIENTE = 202;

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
        listClientes();

        imgbtIconeNovoCliente = (ImageButton) findViewById(R.id.imgbtIconeNovoCliente);
        imgbtIconeNovoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCliente();
            }
        });

        llAdicionarCliente = (LinearLayout) findViewById(R.id.llAdicionarCliente);
        llAdicionarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCliente();
            }
        });

        addDefaultValues();
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

        if ((requestCode == CONSULTAR_CLIENTE) && (resultCode == RESULT_ALT_CLIENTE)) {
            listClientes();
            adapter.notifyDataSetChanged();
        }
    }

    private void initButtonsHub(){
        btClienteVoltar = findViewById(R.id.btClienteVoltar);

        btClienteVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListClientes.this, MainActivity.class));
            }
        });


    }

    private void addNewCliente() {
        Intent intent = new Intent(getApplication(), AddCliente.class);
        startActivityForResult(intent, NOVO_CLIENTE);
    }

    private void listClientes(){

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
                try {
                    Cliente c = (Cliente) listViewClientes.getItemAtPosition(i);
                    openClienteData(c);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", e.getMessage().toString());
                }
            }
        });

    }

    private void openClienteData(Cliente c) {

        Intent intent = new Intent(ListClientes.this, viewCliente.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", c.getId());
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_CLIENTE);
    }

    private void addDefaultValues(){
        // Verificando e adicionando estados
        List<Estado> estados = db.listAllEstados();
        Log.i("INFO ESTADOS LISTCLIENTES ONCREATE", String.valueOf(estados.size()));
        if (estados.isEmpty()) {
            db.addAllEstados();

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

            estados = db.listAllEstados();
            Log.i("INFO ESTADOS LISTCLIENTES ONCREATE PÒS DATA", String.valueOf(estados.size()));
        }
    }
}
