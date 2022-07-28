package com.example.tcc_gerenciadordevendas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaClientes extends AppCompatActivity {

    private Button btClienteVoltar;
    private Button btClienteAdicionar;
    private ListView listView;
    private ClienteAdapter adapter;
    private ArrayList<Cliente> listaDinamicaClientes;
    private SQLiteDatabase db;

    public static final int NOVO_CLIENTE = 101;
    public static final int ALTERAR_CLIENTE = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);

        initButtonsHub();
        initSQLdb();

        listaDinamicaClientes = new ArrayList<Cliente>();
        listView = (ListView) findViewById(R.id.listViewClientes);
        listView.setAdapter(adapter);

        btClienteAdicionar = (Button) findViewById(R.id.btClienteNovo);
        btClienteAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novoCliente();
            }
        });
    }

    private void initButtonsHub(){
        btClienteVoltar = findViewById(R.id.btClienteVoltar);

        btClienteVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaClientes.this, MainActivity.class));
            }
        });
    }

    @SuppressLint("Range")
    private void initSQLdb(){
        db = openOrCreateDatabase("GerenciadorVendas", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS CLIENTE (" +
                        "ID_CLIENTE INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NOME VARCHAR(60), " +
                        "TELEFONE VARCHAR(11))");

        Cursor cursor = db.rawQuery("SELECT * FROM CLIENTE", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                listaDinamicaClientes.add(new Cliente(
                    cursor.getInt(cursor.getColumnIndex("ID_CLIENTE")),
                    cursor.getString(cursor.getColumnIndex("NOME")),
                    cursor.getString(cursor.getColumnIndex("TELEFONE"))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void novoCliente() {
        Intent intent = new Intent(getApplication(), AdicionarCliente.class);
        startActivityForResult(intent, NOVO_CLIENTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Alterando dados
        if ((requestCode == ALTERAR_CLIENTE) && (resultCode == RESULT_OK)) {
            ContentValues c1 = new ContentValues();

            int id = listaDinamicaClientes.get(data.getIntExtra("posicao", 0)).getId_cliente();
            c1.put("NOME", data.getStringExtra("nome"));
            c1.put("TELEFONE", data.getStringExtra("telefone"));

            db.update("CLIENTE", c1, "ID = ?", new String[]{String.valueOf(id)});

            listaDinamicaClientes.set(data.getIntExtra("posicao", 0),
                    new Cliente(id,
                            data.getStringExtra("nome"),
                            data.getStringExtra("telefone")));

            adapter.notifyDataSetChanged();
        }

        //Criando novos dados
        if ((requestCode == NOVO_CLIENTE)&&(resultCode == RESULT_OK)){
            ContentValues c1 = new ContentValues();

            c1.put("NOME", data.getStringExtra("nome"));
            c1.put("TELEFONE", data.getStringExtra("telefone"));

            db.insert("CLIENTE", null, c1);
            int idx = 0;

            Cursor cursor = db.rawQuery("SELECT MAX(ID_CLIENTE) IDMAIOR FROM CLIENTE", null);
            cursor.moveToFirst();
            idx = cursor.getInt(cursor.getColumnIndex("IDMAIOR"));

            listaDinamicaClientes.add(new Cliente(idx,
                                                data.getStringExtra("nome"),
                                                data.getStringExtra("telefone")));

            adapter.notifyDataSetChanged();
        }
    }
}