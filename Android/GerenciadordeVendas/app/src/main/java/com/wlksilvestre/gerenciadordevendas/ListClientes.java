package com.wlksilvestre.gerenciadordevendas;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private EditText editBuscaCliente;
    private ImageButton imgbtIconeNovoCliente;
    private ImageView imgSearch;
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
    private boolean isBuscaOpen = false;

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
        initEditTexts();
        initImgs();

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

    private void initEditTexts () {
        editBuscaCliente = findViewById(R.id.editBuscarCliente);

        editBuscaCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                atualizaListaClientes(charSequence);
                if (!isBuscaOpen) {
                    isBuscaOpen = true;
                    imgSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_cancel_24));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initImgs () {
        imgSearch = findViewById(R.id.imgSearchClientes);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBuscaOpen)
                    editBuscaCliente.requestFocus();
                else {
                    imgSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_search_24_whit));
                    editBuscaCliente.setText("");
                    closeSoftKeyboard();
                    editBuscaCliente.clearFocus();
                }
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

        if (clienteTelefoneList != null && !clienteTelefoneList.isEmpty()) {
            listaDinamicaClienteTelefone.addAll(clienteTelefoneList);
        } else {
            Toast.makeText(getApplicationContext(), "Não há clientes cadastrados no app.", Toast.LENGTH_SHORT).show();
        }

        adapter = new AdapterCliente(this, 0, listaDinamicaClienteTelefone);

        listViewClientes = (ListView) findViewById(R.id.listVClientes);
        listViewClientes.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        listViewClientes.setAdapter(adapter);

        listViewClientes.setOnItemClickListener((adapterView, view, i, l) -> {
            try {
                ClienteTelefone ct = (ClienteTelefone) listViewClientes.getItemAtPosition(i);
                openClienteData(ct);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
        });

    }

    private void atualizaListaClientes(CharSequence _desc) {
        List<ClienteTelefone> clienteTelefoneList = db.listClientesAdapterByNomeOrdered(_desc.toString(), usuario.getUid());
        listaDinamicaClienteTelefone = new ArrayList<>();

        if (clienteTelefoneList != null && !clienteTelefoneList.isEmpty()) {
            listaDinamicaClienteTelefone.addAll(clienteTelefoneList);
        } else {
            Toast.makeText(getApplicationContext(), "Não há dados compatíveis para sua busca.", Toast.LENGTH_SHORT).show();
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
                    Log.e("ERROR", e.getMessage());
                }
            }
        });

        adapter.notifyDataSetChanged();
    }

    private void openClienteData(@NonNull ClienteTelefone ct) {

        Intent intent = new Intent(ListClientes.this, ViewCliente.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", ct.getCliente().getId());
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_CLIENTE);
    }

    private void closeSoftKeyboard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
