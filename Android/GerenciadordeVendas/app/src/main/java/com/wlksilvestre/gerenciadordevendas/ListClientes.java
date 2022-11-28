package com.wlksilvestre.gerenciadordevendas;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListClientes extends AppCompatActivity {

    private Button btClienteVoltar;
    private Button btClienteAdicionar;
    private ImageButton imgbtIconeNovoCliente;
    private ConstraintLayout clAdicionarCliente;
    private ListView listViewClientes;
    private AdapterCliente adapter;
    private ArrayList<Cliente> listaDinamicaClientes;
    private ArrayList<ClienteTelefone> listaDinamicaClienteTelefone;
    private ArrayList<String> arrayList;

    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVO_CLIENTE = 101;
    public static final int ALTERAR_CLIENTE = 102;
    public static final int CONSULTAR_CLIENTE = 103;
    public static final int RESULT_ALT_CLIENTE = 202;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Usuario usuario;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clients_zero);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.e("INFO USER", currentUser.getEmail());
            try {
                usuario = db.selectUsuarioByUID(currentUser.getUid());
                Log.d("INFO UID USUARIO", usuario.getUid());
            } catch (Exception e) {
                Log.e("INFO ERROR GET USER", e.getMessage());
            }
        }


        initButtonsHub();
        listClientes();

        imgbtIconeNovoCliente = (ImageButton) findViewById(R.id.imgbtIconeNovoCliente);
        imgbtIconeNovoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCliente();
            }
        });

        clAdicionarCliente = (ConstraintLayout) findViewById(R.id.clAdicionarCliente);
        clAdicionarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCliente();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //-- Criando novos dados
        if ((requestCode == NOVO_CLIENTE) && (resultCode == RESULT_OK)) {

            Cliente clienteMax = new Cliente();
            Telefone telefoneMax = new Telefone();

            try {
                clienteMax = db.selectMaxCliente(usuario.getUid());
                telefoneMax = db.selectTelefoneFirst(clienteMax);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            db.close();
            listaDinamicaClienteTelefone.add(new ClienteTelefone(clienteMax, telefoneMax));
            adapter.notifyDataSetChanged();

        } else

            //-- Alterando dados existentes
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
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private void addNewCliente() {
        Intent intent = new Intent(getApplication(), AddCliente.class);
        startActivityForResult(intent, NOVO_CLIENTE);
    }

    private void listClientes(){
        List <ClienteTelefone> clienteTelefoneList = db.listClientesAdapterOrdered(usuario.getUid());
        listaDinamicaClienteTelefone = new ArrayList<>();

        if (clienteTelefoneList != null) {
            listaDinamicaClienteTelefone.addAll(clienteTelefoneList);
        }


        adapter = new AdapterCliente(this, 0, listaDinamicaClienteTelefone);

        listViewClientes = (ListView) findViewById(R.id.listVClientes);
        listViewClientes.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        listViewClientes.setAdapter(adapter);

        listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    ClienteTelefone ct = (ClienteTelefone) listViewClientes.getItemAtPosition(i);
                    openClienteData(ct);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }
            }
        });

    }

    private void openClienteData(@NonNull ClienteTelefone ct) {

        Intent intent = new Intent(ListClientes.this, ViewCliente.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", ct.getCliente().getId());
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_CLIENTE);
    }

}
