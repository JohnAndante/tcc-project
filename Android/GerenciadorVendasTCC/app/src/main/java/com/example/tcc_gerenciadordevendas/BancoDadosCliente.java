package com.example.tcc_gerenciadordevendas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BancoDadosCliente extends SQLiteOpenHelper {

    //Versão do banco
    private static final int DB_VERSION = 1;

    // Nome do banco
    private static final String DB_NAME = "tcc_db";

    // Tabela cliente para teste
    // Nome da tabela
    private static final String CLIENTE_TABLE       = "cliente_tb";

    // Colunas da tabela
    private static final String COLUMN_ID       = "id";
    private static final String COLUMN_NOME     = "nome";
    private static final String COLUMN_TELEFONE = "telefone";

    public BancoDadosCliente(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Preparando string de criação de tabela
        String CLIENTE_QUERY = "CREATE TABLE " + CLIENTE_TABLE + "("
                + COLUMN_ID         + " INTEGER PRIMARY KEY, "
                + COLUMN_NOME       + " TEXT, "
                + COLUMN_TELEFONE   + " TEXT)";

        // executando string
        db.execSQL(CLIENTE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // CRUD CLIENTE ////////////////////////////////////////////////////////////////////////////

    void addCliente (Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NOME, cliente.getNome());
        values.put(COLUMN_TELEFONE, cliente.getTelefone());

        db.insert(CLIENTE_TABLE, null, values);
        db.close();
    }

    void deleteCliente (Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CLIENTE_TABLE, COLUMN_ID + " = ? ", new String[] {
                String.valueOf(cliente.getId())
        });
    }

    void deleteClienteById (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CLIENTE_TABLE, COLUMN_ID + " = ? ", new String[] {
                String.valueOf(id)
        });
    }

    Cliente selectCliente (int codigo) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(CLIENTE_TABLE,
                new String[] {
                        COLUMN_ID, COLUMN_NOME, COLUMN_TELEFONE},
                        COLUMN_ID + " = ?",
                new String[] { String.valueOf(codigo) },
                null,
                null,
                null,
                null
                );

        if (cursor != null)
            cursor.moveToFirst();

        Cliente cliente1 = new Cliente(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2));

        return cliente1;
    }

    Cliente selectMaxCliente () {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CLIENTE_TABLE + " WHERE " + COLUMN_ID + " = (SELECT MAX(" + COLUMN_ID + ") " + "FROM " + CLIENTE_TABLE + ")", null);

        if (cursor != null)
            cursor.moveToFirst();

        Cliente cliente1 = new Cliente(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2));

        return cliente1;
    }

    void updateCliente (Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, cliente.getNome());
        values.put(COLUMN_TELEFONE, cliente.getTelefone());

        db.update(CLIENTE_TABLE, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(cliente.getId()) });
    }

    public List<Cliente> listAllClientes() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Cliente> listClientes = new ArrayList<Cliente>();

        String QUERY = "SELECT * FROM " + CLIENTE_TABLE;
        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst()) {
            do {
                Cliente cliente = new Cliente();
                cliente.setId(Integer.parseInt(c.getString(0)));
                cliente.setNome(c.getString(1));
                cliente.setTelefone(c.getString(2));

                listClientes.add(cliente);

            } while (c.moveToNext());
        }

        return listClientes;
    }


}


