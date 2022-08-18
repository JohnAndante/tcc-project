package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private ArrayList<Cliente> listaDinamicaClientes;
    private ArrayList<String> arrayList;

    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVO_CLIENTE = 101;
    public static final int ALTERAR_CLIENTE = 102;

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

        List<Cliente> clientes = db.listAllClientes();
        listaDinamicaClientes = new ArrayList<Cliente>();

        ListView listViewClientesTeste;

        if (!clientes.isEmpty()) {
            for (Cliente c : clientes) {
                Log.d("Lista", "\nID: " + c.getId()
                        + "\nNome: " + c.getNome()
                        + "\nTelefone: " + c.getTelefone());

                listaDinamicaClientes.add(c);

            }
        }

        adapter = new AdapterCliente(this, 0, listaDinamicaClientes);

        listViewClientes = (ListView) findViewById(R.id.listVClientes);
        Log.d("wtf", "\n \nlistview: " + listViewClientes);
        listViewClientes.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}
