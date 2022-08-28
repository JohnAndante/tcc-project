package com.example.tcc_gerenciadordevendas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BancoDadosCliente extends SQLiteOpenHelper {

    //Versão do banco
    private static final int DB_VERSION = 1;

    // Nome do banco
    private static final String DB_NAME = "tcc_db";

    // Nome das tabelas
    private static final String CLIENTE_TABLE       = "cliente_tb";
    private static final String ENDERECO_TABLE      = "endereco_tb";
    private static final String TELEFONE_TABLE      = "telefone_tb";
    private static final String CIDADE_TABLE        = "cidade_tb";
    private static final String ESTADO_TABLE        = "estado_tb";

    // Colunas da tabela cliente
    private static final String CLIENTE_ID          = "id_cliente";
    private static final String CLIENTE_NOME        = "nome";
    private static final String CLIENTE_TELEFONE    = "telefone";

    // Colunas da tabela endereco
    private static final String ENDERECO_ID         = "id_endereco";
    private static final String ENDERECO_NUM        = "numero";
    private static final String ENDERECO_RUA        = "rua";
    private static final String ENDERECO_BAIRRO     = "bairro";
    private static final String ENDERECO_COMP       = "complemento";
    private static final String ENDERECO_REF        = "referencia";
    private static final String ENDERECO_CIDADE     = "id_cidade";
    private static final String ENDERECO_CLIENTE    = "id_cliente";

    // Colunas da tabela telefone
    private static final String TELEFONE_ID         = "id_telefone";
    private static final String TELEFONE_NUM        = "telefone";
    private static final String TELEFONE_CLIENTE    = "id_cliente";

    // Colunas da tabela cidade
    private static final String CIDADE_ID           = "id_cidade";
    private static final String CIDADE_NOME         = "nome";
    private static final String CIDADE_ESTADO       = "id_estado";

    // Colunas da cidade estado
    private static final String ESTADO_ID           = "id_estado";
    private static final String ESTADO_NOME         = "nome";
    private static final String ESTADO_UF           = "uf";

    // Strings para query de criação
    private String CLIENTE_QUERY    = "SELECT * FROM " + CLIENTE_TABLE;
    private String ENDERECO_QUERY   = "SELECT * FROM " + ENDERECO_TABLE;
    private String TELEFONE_QUERY   = "SELECT * FROM " + TELEFONE_TABLE;
    private String CIDADE_QUERY     = "SELECT * FROM " + CIDADE_TABLE;
    private String ESTADO_QUERY     = "SELECT * FROM " + ESTADO_TABLE;

    public BancoDadosCliente(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createTables();
        db.execSQL(CLIENTE_QUERY);
        db.execSQL(TELEFONE_QUERY);
        db.execSQL(ENDERECO_QUERY);
        db.execSQL(CIDADE_QUERY);
        db.execSQL(ESTADO_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createTables(){
        // Preparando strings de criação de tabelas
        CLIENTE_QUERY = "CREATE TABLE " + CLIENTE_TABLE + "("
                + CLIENTE_ID        + " INTEGER PRIMARY KEY, "
                + CLIENTE_NOME      + " TEXT, "
                + CLIENTE_TELEFONE  + " TEXT)";

        TELEFONE_QUERY = "CREATE TABLE " + TELEFONE_TABLE + "("
                + TELEFONE_ID       + " INTEGER PRIMARY KEY, "
                + TELEFONE_NUM      + " TEXT, "
                + TELEFONE_CLIENTE  + " INTEGER, "
                + "FOREIGN KEY ("   + TELEFONE_CLIENTE  + ") "
                + "REFERENCES "     + CLIENTE_TABLE     + " (" + TELEFONE_CLIENTE + "))";

        ESTADO_QUERY = "CREATE TABLE " + ESTADO_TABLE + "("
                + ESTADO_ID + " INTEGER PRIMARY KEY, "
                + ESTADO_NOME + " TEXT,"
                + ESTADO_UF + " TEXT)";

        CIDADE_QUERY = "CREATE TABLE " + CIDADE_TABLE + "("
                + CIDADE_ID         + " INTEGER PRIMARY KEY, "
                + CIDADE_NOME       + " TEXT, "
                + CIDADE_ESTADO     + " INTEGER, "
                + "FOREIGN KEY ("   + CIDADE_ESTADO     + ") "
                + "REFERENCES "     + ESTADO_TABLE      + " (" + CIDADE_ESTADO + "))";

        ENDERECO_QUERY = "CREATE TABLE " + ENDERECO_TABLE + "("
                + ENDERECO_ID       + " INTEGER PRIMARY KEY, "
                + ENDERECO_RUA      + " TEXT, " // Mudar parar logradouro
                + ENDERECO_NUM      + " TEXT, "
                + ENDERECO_BAIRRO   + " TEXT, "
                + ENDERECO_REF      + " TEXT, "
                + ENDERECO_COMP     + " TEXT, "
                + ENDERECO_CIDADE   + " INTEGER, "
                + ENDERECO_CLIENTE  + " INTEGER, "
                + "FOREIGN KEY ("   + ENDERECO_CLIENTE  + ") "
                + "REFERENCES "     + CLIENTE_TABLE     + " (" + ENDERECO_CLIENTE + "))";

        Log.i("DATABASE INFO", CLIENTE_QUERY);
        Log.i("DATABASE INFO", TELEFONE_QUERY);
        Log.i("DATABASE INFO", ENDERECO_QUERY);
        Log.i("DATABASE INFO", CIDADE_QUERY);
        Log.i("DATABASE INFO", ESTADO_QUERY);
    }

    // CRUD CLIENTE ////////////////////////////////////////////////////////////////////////////

    void addCliente (Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CLIENTE_NOME, cliente.getNome());
        values.put(CLIENTE_TELEFONE, cliente.getTelefone());

        db.insert(CLIENTE_TABLE, null, values);
        db.close();
    }

    void deleteCliente (Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CLIENTE_TABLE, CLIENTE_ID + " = ? ", new String[] {
                String.valueOf(cliente.getId())
        });
        db.close();
    }

    void deleteClienteById (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CLIENTE_TABLE, CLIENTE_ID + " = ? ", new String[] {
                String.valueOf(id)
        });
        db.close();
    }

    Cliente selectCliente (int codigo) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(CLIENTE_TABLE,
                new String[] {
                        CLIENTE_ID, CLIENTE_NOME, CLIENTE_TELEFONE},
                        CLIENTE_ID + " = ?",
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

        db.close();
        return cliente1;
    }

    Cliente selectMaxCliente () {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CLIENTE_TABLE + " WHERE " + CLIENTE_ID + " = (SELECT MAX(" + CLIENTE_ID + ") " + "FROM " + CLIENTE_TABLE + ")", null);

        if (cursor != null)
            cursor.moveToFirst();

        Cliente cliente1 = new Cliente(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2));

        db.close();
        return cliente1;
    }

    void updateCliente (Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CLIENTE_NOME, cliente.getNome());
        values.put(CLIENTE_TELEFONE, cliente.getTelefone());

        db.update(CLIENTE_TABLE, values, CLIENTE_ID + " = ?",
                new String[] { String.valueOf(cliente.getId()) });

        db.close();
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

        db.close();
        return listClientes;
    }

    // CRUD ENDEREÇO ////////////////////////////////////////////////////////////////////////////

    void addTelefone (Telefone telefone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TELEFONE_NUM, telefone.getNum());
        values.put(TELEFONE_CLIENTE, telefone.getCliente().getId());

        db.insert(TELEFONE_TABLE, null, values);
        db.close();
    }

    void deleteTelefone (Telefone telefone) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TELEFONE_TABLE, TELEFONE_ID + " = ? ", new String[]{
                String.valueOf(telefone.getId())
        });
        db.close();
    }

    void deleteTelefoneById (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TELEFONE_TABLE, TELEFONE_ID + " = ? ", new String[] {
                String.valueOf(id)
        });
        db.close();
    }

    void deleteTelefoneByCliente (Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TELEFONE_TABLE, TELEFONE_CLIENTE + " = ? ", new String[] {
                String.valueOf(cliente.getId())
        });
        db.close();
    }

    Telefone selectTelefone (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TELEFONE_TABLE,
            new String[] {
                    TELEFONE_ID, TELEFONE_NUM, TELEFONE_CLIENTE },
            TELEFONE_ID + " = ?",
            new String[] { String.valueOf(id) },
            null,
            null,
            null,
            null
        );

        if (cursor != null)
            cursor.moveToFirst();

        int _id = cursor.getInt(2);
        Cliente c = selectCliente(_id);

        Telefone telefone1 = new Telefone(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                c);

        db.close();
        return telefone1;
    }

    public Telefone selectTelefoneFirst(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY =  " SELECT" +
                " T." + TELEFONE_ID + ", T." + TELEFONE_NUM + ", C." + CLIENTE_ID +
                " FROM " + TELEFONE_TABLE + " T" +
                " INNER JOIN " + CLIENTE_TABLE + " C" +
                " ON T." + TELEFONE_CLIENTE + " = C." + CLIENTE_ID +
                " WHERE C." + CLIENTE_ID + " = " + String.valueOf(cliente.getId()) +
                " LIMIT 1";

        Cursor c = db.rawQuery(QUERY, null);

        int id = c.getInt(2);
        cliente = selectCliente(id);

        Telefone telefone = new Telefone();
        telefone.setId(Integer.parseInt(c.getString(0)));
        telefone.setNum(c.getString(1));
        telefone.setCliente(cliente);

        db.close();
        return telefone;
    }

    void updateTelefone (Telefone telefone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TELEFONE_NUM, telefone.getNum());
        values.put(TELEFONE_CLIENTE, telefone.getCliente().getId());

        db.update(TELEFONE_TABLE, values, TELEFONE_ID + " = ? ",
                new String[] {
                        String.valueOf(telefone.getId())
                });
        db.close();
    }

    public List<Telefone> listAllTelefones() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Telefone> listTelefones = new ArrayList<Telefone>();

        String QUERY = "SELECT * FROM " + TELEFONE_TABLE;
        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst()) {
            do {

                int id = c.getInt(2);
                Cliente cliente = selectCliente(id);

                Telefone telefone = new Telefone();
                telefone.setId(c.getInt(0));
                telefone.setNum(c.getString(1));
                telefone.setCliente(cliente);

                listTelefones.add(telefone);
            } while (c.moveToNext());
        }
        db.close();
        return listTelefones;
    }

    public List<Telefone> listTelefoneFromCliente() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Telefone> listTelefones = new ArrayList<Telefone>();

        String QUERY =  " SELECT" +
                " T." + TELEFONE_ID + ", T." + TELEFONE_NUM + ", C." + CLIENTE_ID +
                " FROM " + TELEFONE_TABLE + " T" +
                " INNER JOIN " + CLIENTE_TABLE + " C" +
                " ON T." + TELEFONE_CLIENTE + " = C." + CLIENTE_ID;

        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst()) {
            do {

                int id = c.getInt(2);
                Cliente cliente = selectCliente(id);

                Telefone telefone = new Telefone();
                telefone.setId(Integer.parseInt(c.getString(0)));
                telefone.setNum(c.getString(1));
                telefone.setCliente(cliente);

                listTelefones.add(telefone);
            } while (c.moveToNext());
        }
        db.close();
        return listTelefones;
    }

    // CRUD ESTADO //////////////////////////////////////////////////////////////////////////////

    public void addEstado (Estado estado) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ESTADO_NOME, estado.getNome());
        values.put(ESTADO_UF, estado.getUf());

        db.insert(ESTADO_TABLE, null, values);
        db.close();
    }

    public void deleteEstado (Estado estado) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ESTADO_TABLE, ESTADO_ID + " = ? ", new String[]{
                String.valueOf(estado.getId())
        });
        db.close();
    }

    public void deleteEstadoById (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ESTADO_TABLE, ESTADO_ID + " = ? ", new String[] {
                String.valueOf(id)
        });
        db.close();
    }

    public Estado selectEstado (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(ESTADO_TABLE,
                new String[] {
                        ESTADO_ID, ESTADO_NOME, ESTADO_UF },
                        ESTADO_ID + " = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );

        if (cursor != null)
            cursor.moveToFirst();

        Estado estado = new Estado (
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2)
        );

        db.close();
        return estado;
    }

    public void updateEstado (Estado estado) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ESTADO_NOME, estado.getNome());
        values.put(ESTADO_UF, estado.getUf());

        db.update(ESTADO_TABLE, values, ESTADO_ID + " = ? ",
                new String[] {
                        String.valueOf(estado.getId())
                });
        db.close();
    }

    public  List<Estado> listAllEstados() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Estado> listEstados = new ArrayList<Estado>();

        String QUERY = "SELECT * FROM " + ESTADO_TABLE;
        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst()) {
            do {
                Estado estado = new Estado();
                estado.setId(c.getInt(0));
                estado.setNome(c.getString(1));
                estado.setUf(c.getString(2));

                listEstados.add(estado);
            } while (c.moveToNext());
        }

        db.close();
        return listEstados;
    }

    // CRUD ENDEREÇO ////////////////////////////////////////////////////////////////////////////

    void addEndereco () {
    }
}


