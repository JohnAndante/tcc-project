package com.wlksilvestre.gerenciadordevendas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

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
    private static final String PRODUTO_TABLE       = "produto_tb";
    private static final String MARCA_TABLE         = "marca_tb";
    private static final String LINHA_TABLE         = "linha_tb";
    private static final String CATEGORIA_TABLE     = "categoria_tb";
    private static final String SUBCAT_TABLE        = "subcat_tb";
    private static final String PROD_SUBCAT_TABLE   = "prod_subcat_tb";
    private static final String FORMA_PGTO_TABLE    = "forma_pgto_tb";
    private static final String VENDA_TABLE         = "venda_tb";
    private static final String PROD_VENDA_TABLE    = "prod_venda_tb";
    private static final String PGTO_TABLE          = "pgto_tb";
    private static final String USER_TABLE          = "user_tb";

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

    // Colunas da tabela marca
    private static final String MARCA_ID            = "id_marca";
    private static final String MARCA_DESC          = "descricao";

    // Colunas da tabela linha
    private static final String LINHA_ID            = "id_linha";
    private static final String LINHA_DESC          = "descricao";
    private static final String LINHA_MARCA         = "id_marca";

    // Colunas da tabela categoria
    private static final String CATEGORIA_ID        = "id_categoria";
    private static final String CATEGORIA_DESC      = "descricao";
    private static final String CATEGORIA_COR       = "cor";

    // Colunas da tabela subcategoria
    private static final String SUBCAT_ID           = "id_subcat";
    private static final String SUBCAT_DESC         = "descricao";
    private static final String SUBCAT_CATEGORIA    = "id_categoria";

    // Colunas da tabela produto
    private static final String PRODUTO_ID          = "id_produto";
    private static final String PRODUTO_DESC        = "descricao";
    private static final String PRODUTO_VALOR       = "valor";
    private static final String PRODUTO_LINHA       = "id_linha";

    // Colunas da tabela produto x subcategoria
    private static final String PROD_SUBCAT_PROD    = "id_produto";
    private static final String PROD_SUBCAT_SUBCAT  = "id_subcat";

    // Colunas da tabela forma de pagamento
    private static final String FORMA_PGTO_ID       = "id_forma_pgto";
    private static final String FORMA_PGTO_DESC     = "descricao";
    private static final String FORMA_PGTO_PRAZO    = "prazo";
    private static final String FORMA_PGTO_PARC     = "parcelavel";

    // Colunas da tabela Venda
    private static final String VENDA_ID            = "id_venda";
    private static final String VENDA_DATA          = "data_venda";
    private static final String VENDA_VALOR         = "valor_total";
    private static final String VENDA_CLIENTE       = "id_cliente";
    private static final String VENDA_PGTO          = "id_pgto";

    // Colunas da tabela Produto x venda
    private static final String PROD_VENDA_VENDA    = "id_venda";
    private static final String PROD_VENDA_PROD     = "id_prod";
    private static final String PROD_VENDA_QTD      = "qtd_prod";
    private static final String PROD_VENDA_VALOR    = "valor_prod";

    // Colunas da tabela Pagamento
    private static final String PGTO_ID             = "id_pagamento";
    private static final String PGTO_CLIENTE        = "id_cliente";
    private static final String PGTO_FORMA_PGTO     = "id_forma_pgto";
    private static final String PGTO_VALOR          = "valor_pgto";
    private static final String PGTO_DATA           = "data_pgto";
    private static final String PGTO_JUROS          = "juros";
    private static final String PGTO_PARC           = "parcelas";

    // Colunas da tabela Usuario
    private static final String USER_ID             = "id_usuario";
    private static final String USER_NOME           = "nome_usuario";
    private static final String USER_EMAIL          = "email_usuario";
    private static final String USER_TELEFONE       = "telefone_usuario";
    private static final String USER_UID            = "uid_usuario";

    // Strings para query de criação
    private String CLIENTE_QUERY        = "SELECT * FROM " + CLIENTE_TABLE;
    private String ENDERECO_QUERY       = "SELECT * FROM " + ENDERECO_TABLE;
    private String TELEFONE_QUERY       = "SELECT * FROM " + TELEFONE_TABLE;
    private String CIDADE_QUERY         = "SELECT * FROM " + CIDADE_TABLE;
    private String ESTADO_QUERY         = "SELECT * FROM " + ESTADO_TABLE;
    private String MARCA_QUERY          = "SELECT * FROM " + MARCA_TABLE;
    private String LINHA_QUERY          = "SELECT * FROM " + LINHA_TABLE;
    private String CATEGORIA_QUERY      = "SELECT * FROM " + CATEGORIA_TABLE;
    private String SUBCAT_QUERY         = "SELECT * FROM " + SUBCAT_TABLE;
    private String PRODUTO_QUERY        = "SELECT * FROM " + PRODUTO_TABLE;
    private String PROD_SUBCAT_QUERY    = "SELECT * FROM " + PROD_SUBCAT_TABLE;
    private String FORMA_PGTO_QUERY     = "SELECT * FROM " + FORMA_PGTO_TABLE;
    private String VENDA_QUERY          = "SELECT * FROM " + VENDA_TABLE;
    private String PROD_VENDA_QUERY     = "SELECT * FROM " + PROD_VENDA_TABLE;
    private String PGTO_QUERY           = "SELECT * FROM " + PGTO_TABLE;
    private String USER_QUERY           = "SELECT * FROM " + USER_TABLE;

    DocumentReference dr;

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
        db.execSQL(MARCA_QUERY);
        db.execSQL(LINHA_QUERY);
        db.execSQL(CATEGORIA_QUERY);
        db.execSQL(SUBCAT_QUERY);
        db.execSQL(PRODUTO_QUERY);
        db.execSQL(PROD_SUBCAT_QUERY);
        db.execSQL(FORMA_PGTO_QUERY);
        db.execSQL(VENDA_QUERY);
        db.execSQL(PROD_VENDA_QUERY);
        db.execSQL(PGTO_QUERY);
        db.execSQL(USER_QUERY);

        /*
        ini_addDefaultEstados(db);
        ini_addDefaultCidades(db);
        ini_addDefaultFormaPgto(db);
        ini_addDefaultCategorias(db);
        ini_addDefaultMarcas(db);
        ini_addDefaultLinhas(db);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createTables(){
        // Preparando strings de criação de tabelas
        CLIENTE_QUERY       = "CREATE TABLE " + CLIENTE_TABLE + "("
                + CLIENTE_ID        + " INTEGER PRIMARY KEY, "
                + CLIENTE_NOME      + " TEXT, "
                + CLIENTE_TELEFONE  + " TEXT)";

        TELEFONE_QUERY      = "CREATE TABLE " + TELEFONE_TABLE + "("
                + TELEFONE_ID       + " INTEGER PRIMARY KEY, "
                + TELEFONE_NUM      + " TEXT, "
                + TELEFONE_CLIENTE  + " INTEGER, "
                + "FOREIGN KEY ("   + TELEFONE_CLIENTE  + ") "
                + "REFERENCES "     + CLIENTE_TABLE     + " (" + TELEFONE_CLIENTE   + "))";

        ESTADO_QUERY        = "CREATE TABLE " + ESTADO_TABLE + "("
                + ESTADO_ID + " INTEGER PRIMARY KEY, "
                + ESTADO_NOME + " TEXT,"
                + ESTADO_UF + " TEXT)";

        CIDADE_QUERY        = "CREATE TABLE " + CIDADE_TABLE + "("
                + CIDADE_ID         + " INTEGER PRIMARY KEY, "
                + CIDADE_NOME       + " TEXT, "
                + CIDADE_ESTADO     + " INTEGER, "
                + "FOREIGN KEY ("   + CIDADE_ESTADO     + ") "
                + "REFERENCES "     + ESTADO_TABLE      + " (" + CIDADE_ESTADO      + "))";

        ENDERECO_QUERY      = "CREATE TABLE " + ENDERECO_TABLE + "("
                + ENDERECO_ID       + " INTEGER PRIMARY KEY, "
                + ENDERECO_RUA      + " TEXT, " // Mudar parar logradouro
                + ENDERECO_NUM      + " TEXT, "
                + ENDERECO_BAIRRO   + " TEXT, "
                + ENDERECO_REF      + " TEXT, "
                + ENDERECO_COMP     + " TEXT, "
                + ENDERECO_CIDADE   + " INTEGER, "
                + ENDERECO_CLIENTE  + " INTEGER, "
                + "FOREIGN KEY ("   + ENDERECO_CLIENTE  + ") "
                + "REFERENCES "     + CLIENTE_TABLE     + " (" + ENDERECO_CLIENTE   + "))";

        MARCA_QUERY         = "CREATE TABLE " + MARCA_TABLE + "( "
                + MARCA_ID          + " INTEGER PRIMARY KEY, "
                + MARCA_DESC        + " TEXT )";

        LINHA_QUERY         = "CREATE TABLE " + LINHA_TABLE + "( "
                + LINHA_ID          + " INTEGER PRIMARY KEY, "
                + LINHA_DESC        + " TEXT,  "
                + LINHA_MARCA       + " INTEGER, "
                + "FOREIGN KEY ("   + LINHA_MARCA       + ") "
                + "REFERENCES "     + MARCA_TABLE       + " (" + LINHA_MARCA        + "))";

        CATEGORIA_QUERY     = "CREATE TABLE " + CATEGORIA_TABLE + "( "
                + CATEGORIA_ID      + " INTEGER PRIMARY KEY, "
                + CATEGORIA_DESC    + " TEXT, "
                + CATEGORIA_COR     + " TEXT )";

        SUBCAT_QUERY        = "CREATE TABLE " + SUBCAT_TABLE + "( "
                + SUBCAT_ID         + " INTEGER PRIMARY KEY, "
                + SUBCAT_DESC       + " TEXT, "
                + SUBCAT_CATEGORIA  + " INTEGER, "
                + "FOREIGN KEY ("   + SUBCAT_CATEGORIA  + ") "
                + "REFERENCES "     + CATEGORIA_TABLE   + " (" + SUBCAT_CATEGORIA   + "))";

        PRODUTO_QUERY       = "CREATE TABLE " + PRODUTO_TABLE + "( "
                + PRODUTO_ID        + " INTEGER PRIMARY KEY, "
                + PRODUTO_DESC      + " TEXT, "
                + PRODUTO_VALOR     + " DECIMAL(8, 2), "
                + PRODUTO_LINHA     + " INTEGER, "
                + "FOREIGN KEY ("   + PRODUTO_LINHA     + ") "
                + "REFERENCES "     + LINHA_TABLE       + " (" + PRODUTO_LINHA      + "))";

        PROD_SUBCAT_QUERY   = "CREATE TABLE " + PROD_SUBCAT_TABLE + "( "
                + PROD_SUBCAT_PROD   + " INTEGER, "
                + PROD_SUBCAT_SUBCAT + " INTEGER, "
                + "FOREIGN KEY ("    + PROD_SUBCAT_PROD   + ") "
                + "REFERENCES "      + PRODUTO_TABLE      + " (" + PROD_SUBCAT_PROD   + "), "
                + "FOREIGN KEY ("    + PROD_SUBCAT_SUBCAT + ") "
                + "REFERENCES "      + SUBCAT_TABLE       + " (" + PROD_SUBCAT_SUBCAT + "))";

        FORMA_PGTO_QUERY    = "CREATE TABLE " + FORMA_PGTO_TABLE + "( "
                + FORMA_PGTO_ID      + " INTEGER PRIMARY KEY, "
                + FORMA_PGTO_DESC    + " TEXT, "
                + FORMA_PGTO_PRAZO   + " INTEGER, "
                + FORMA_PGTO_PARC    + " INTEGER )";

        VENDA_QUERY         = "CREATE TABLE " + VENDA_TABLE + "( "
                + VENDA_ID           + " INTEGER PRIMARY KEY, "
                + VENDA_DATA         + " TEXT, "
                + VENDA_VALOR        + " DECIMAL(8, 2), "
                + VENDA_CLIENTE      + " INTEGER, "
                + VENDA_PGTO         + " INTEGER, "
                + "FOREIGN KEY ("    + VENDA_CLIENTE    + ") "
                + "REFERENCES "      + CLIENTE_TABLE    + " (" + VENDA_CLIENTE      + "), "
                + "FOREIGN KEY ("    + VENDA_PGTO + ") "
                + "REFERENCES "      + FORMA_PGTO_TABLE + " (" + VENDA_PGTO   + "))";

        PROD_VENDA_QUERY    = "CREATE TABLE " + PROD_VENDA_TABLE + "( "
                + PROD_VENDA_VENDA   + " INTEGER, "
                + PROD_VENDA_PROD    + " INTEGER, "
                + PROD_VENDA_QTD     + " INTEGER, "
                + PROD_VENDA_VALOR   + " DECIMAL(8, 2), "
                + "FOREIGN KEY ("    + PROD_VENDA_VENDA + ") "
                + "REFERENCES "      + VENDA_TABLE      + " (" + PROD_VENDA_VENDA   + "), "
                + "FOREIGN KEY ("    + PROD_VENDA_PROD  + ") "
                + "REFERENCES "      + PRODUTO_TABLE    + " (" + PROD_VENDA_PROD    + "))";

        PGTO_QUERY          = "CREATE TABLE " + PGTO_TABLE + "( "
                + PGTO_ID            + " INTEGER PRIMARY KEY, "
                + PGTO_CLIENTE       + " INTEGER, "
                + PGTO_FORMA_PGTO    + " INTEGER, "
                + PGTO_VALOR         + " DECIMAL(8, 2), "
                + PGTO_DATA          + " TEXT,"
                + PGTO_JUROS         + " DECIMAL(8, 2), "
                + PGTO_PARC          + " INTEGER, "
                + "FOREIGN KEY ("    + PGTO_CLIENTE     + ") "
                + "REFERENCES "      + CLIENTE_TABLE    + " (" + PGTO_CLIENTE       + "), "
                + "FOREIGN KEY ("    + PGTO_FORMA_PGTO  + ") "
                + "REFERENCES "      + FORMA_PGTO_TABLE + " (" + PGTO_FORMA_PGTO    + "))";

        USER_QUERY          = "CREATE TABLE " + USER_TABLE + "( "
                + USER_ID            + " INTEGER PRIMARY KEY, "
                + USER_NOME          + " TEXT, "
                + USER_EMAIL         + " TEXT, "
                + USER_TELEFONE      + " TEXT, "
                + USER_UID           + " TEXT )";

    }

    // CRUD CLIENTE ////////////////////////////////////////////////////////////////////////////

    void addCliente (@NonNull Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (cliente.getId() >= 0)
            values.put(CLIENTE_ID, cliente.getId());
        values.put(CLIENTE_NOME, cliente.getNome());
        values.put(CLIENTE_TELEFONE, cliente.getTelefone());

        db.insert(CLIENTE_TABLE, null, values);
        db.close();
    }

    void deleteCliente (@NonNull Cliente cliente) {
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

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Cliente cliente1 = new Cliente(
                cursor.getInt(0),
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
        else
            return null;

        Cliente cliente1 = new Cliente(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2));

        db.close();
        return cliente1;
    }

    void updateCliente (@NonNull Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CLIENTE_NOME, cliente.getNome());
        values.put(CLIENTE_TELEFONE, cliente.getTelefone());

        db.update(CLIENTE_TABLE, values, CLIENTE_ID + " = ?",
                new String[] { String.valueOf(cliente.getId()) });

        db.close();
    }

    public List<Cliente> listAllClientes () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Cliente> listClientes = new ArrayList<>();

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

    public List<Cliente> listClientesOrdered () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Cliente> listClientes = new ArrayList<>();

        String QUERY = "SELECT * FROM " + CLIENTE_TABLE +
                " ORDER BY " + CLIENTE_NOME;
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

    public List<Cliente> listClientesByNome (String _nome) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Cliente> listClientes = new ArrayList<>();

        String QUERY = "SELECT * FROM " + CLIENTE_TABLE +
                " WHERE " + CLIENTE_NOME + " LIKE '%" + _nome + "%'" +
                " ORDER BY " + CLIENTE_NOME;

        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst()) {
            do {
                Cliente cliente = new Cliente();
                cliente.setId(Integer.parseInt(c.getString(0)));
                cliente.setNome(c.getString(1));
                cliente.setTelefone(c.getString(2));

                listClientes.add(cliente);

            } while (c.moveToNext());

            db.close();
            return listClientes;
        } else {
            db.close();
            return null;
        }
    }

    public int selectCountClientes () {
        SQLiteDatabase db = this.getWritableDatabase();

        int count = 0;

        String QUERY = "SELECT COUNT(*) FROM " + CLIENTE_TABLE;
        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst())
            count =  c.getInt(0);

        return count;
    }

    // CRUD ENDEREÇO ////////////////////////////////////////////////////////////////////////////

    void addTelefone (@NonNull Telefone telefone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TELEFONE_NUM, telefone.getNum());
        values.put(TELEFONE_CLIENTE, telefone.getCliente().getId());

        db.insert(TELEFONE_TABLE, null, values);
        db.close();
    }

    void deleteTelefone (@NonNull Telefone telefone) {
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

    void deleteTelefoneByCliente (@NonNull Cliente cliente) {
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

    public Telefone selectTelefoneFirst(@NonNull Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        Telefone telefone = new Telefone();

        String QUERY =  " SELECT" +
                " T."           + TELEFONE_ID + ", T." + TELEFONE_NUM + ", C." + CLIENTE_ID +
                " FROM "        + TELEFONE_TABLE    + " T" +
                " INNER JOIN "  + CLIENTE_TABLE     + " C" +
                " ON T."        + TELEFONE_CLIENTE  + " = C." + CLIENTE_ID +
                " WHERE C."     + CLIENTE_ID        + " = " + cliente.getId() +
                " LIMIT 1";


        Cursor c = db.rawQuery(QUERY, null);

        c.moveToFirst();

        if (c.getCount() > 0) {
            telefone.setId(Integer.parseInt(c.getString(0)));
            telefone.setNum(c.getString(1));
            telefone.setCliente(cliente);
        } else {
            telefone = null;
        }

        db.close();
        return telefone;
    }

    void updateTelefone (@NonNull Telefone telefone) {
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

    public void addEstado (@NonNull Estado estado) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ESTADO_NOME, estado.getNome());
        values.put(ESTADO_UF, estado.getUf());

        db.insert(ESTADO_TABLE, null, values);
        db.close();
    }

    public void deleteEstado (@NonNull Estado estado) {
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

        Estado estado = new Estado();

        if (cursor != null) {
            cursor.moveToFirst();

            estado = new Estado(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
        } else {
            estado = null;
        }

        db.close();
        return estado;
    }

    public void updateEstado (@NonNull Estado estado) {
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

    public List<Estado> listAllEstados() {
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

        c.close();
        db.close();
        return listEstados;
    }

    public List<Estado> listAllEstadosOrdered () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Estado> listEstados = new ArrayList<Estado>();

        String QUERY = "SELECT * FROM " + ESTADO_TABLE + " ORDER BY " + ESTADO_NOME;
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

        c.close();
        db.close();
        return listEstados;
    }

    public List<Estado> listAllEstadosByName (String nome) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Estado> listEstados = new ArrayList<Estado>();

        String QUERY = "SELECT * FROM " + ESTADO_TABLE +
                " WHERE " + ESTADO_NOME + " LIKE '%" + nome + "%'" +
                " ORDER BY " + ESTADO_NOME;

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

        c.close();
        db.close();
        return listEstados;
    }

    // CRUD CIDADE //////////////////////////////////////////////////////////////////////////////

    public void addCidade (@NonNull Cidade cidade) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CIDADE_NOME, cidade.getNome());
        values.put(CIDADE_ESTADO, cidade.getEstado().getId());

        db.insert(CIDADE_TABLE, null, values);
        db.close();
    }

    public void deleteCidade (@NonNull Cidade cidade) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CIDADE_TABLE, CIDADE_ID + " = ? ", new String[]{
                String.valueOf(cidade.getId())
        });
        db.close();
    }

    public void deleteCidadeById (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CIDADE_TABLE, CIDADE_ID + " = ? ", new String[] {
                String.valueOf(id)
        });
        db.close();
    }

    public Cidade selectCidade (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(CIDADE_TABLE,
                new String[] {
                        CIDADE_ID, CIDADE_NOME, CIDADE_ESTADO },
                CIDADE_ID + " = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );

        Cidade cidade = new Cidade();
        Estado estado = new Estado();

        if (cursor != null) {
            cursor.moveToFirst();

            cidade = new Cidade(
                    cursor.getInt(0),
                    cursor.getString(1),
                    estado = selectEstado(cursor.getInt(2))
            );
        } else {
            cidade = null;
        }

        db.close();
        return cidade;
    }

    public List<Cidade> listAllCidades () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Cidade> listCidades = new ArrayList<Cidade>();

        String QUERY = "SELECT * FROM " + CIDADE_TABLE;
        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst()) {
            do {
                Cidade cidade = new Cidade();
                Estado estado = new Estado();

                estado = selectEstado(c.getInt(2));

                cidade.setId(c.getInt(0));
                cidade.setNome(c.getString(1));
                cidade.setEstado(estado);

                listCidades.add(cidade);
            } while (c.moveToNext());
        }

        db.close();
        return listCidades;
    }

    public List<Cidade> listAllCidadesByEstado (@NonNull Estado estado) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Cidade> listCidades = new ArrayList<>();

        // Corrigir em um outro momento
        String QUERY =  " SELECT" +
                " C." + CIDADE_ID + ", C." + CIDADE_NOME + ", E." + ESTADO_ID +
                " FROM " + CIDADE_TABLE + " C" +
                " INNER JOIN " + ESTADO_TABLE + " E" +
                " ON C." + CIDADE_ESTADO + " = E." + ESTADO_ID +
                " WHERE E." + ESTADO_ID + " = " + estado.getId();

        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst()) {
            do {
                Cidade cidade = new Cidade();

                cidade.setId(c.getInt(0));
                cidade.setNome(c.getString(1));
                cidade.setEstado(estado);

                listCidades.add(cidade);
            } while (c.moveToNext());
        }

        db.close();
        return listCidades;
    }

    public List<Cidade> listAllCidadesByEstadoAndNome (@NonNull Estado estado, String nome) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Cidade> listCidades = new ArrayList<>();

        // Corrigir em um outro momento
        String QUERY =  " SELECT" +
                " C." + CIDADE_ID + ", C." + CIDADE_NOME + ", E." + ESTADO_ID +
                " FROM " + CIDADE_TABLE + " C" +
                " INNER JOIN " + ESTADO_TABLE + " E" +
                " ON C." + CIDADE_ESTADO + " = E." + ESTADO_ID +
                " WHERE E." + ESTADO_ID + " = " + estado.getId() +
                " AND C." + CIDADE_NOME + " LIKE '%" + nome + "%'";

        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst()) {
            do {
                Cidade cidade = new Cidade();

                cidade.setId(c.getInt(0));
                cidade.setNome(c.getString(1));
                cidade.setEstado(estado);

                listCidades.add(cidade);
            } while (c.moveToNext());
        }

        db.close();
        return listCidades;
    }

    public void updateCidade (@NonNull Cidade cidade) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CIDADE_NOME, cidade.getNome());
        values.put(CIDADE_ESTADO, cidade.getEstado().getId());

        db.update(CIDADE_TABLE, values, CIDADE_ID + " = ? ",
                new String[] {
                        String.valueOf(cidade.getId())
                });
        db.close();
    }


    // CRUD ENDEREÇO ////////////////////////////////////////////////////////////////////////////

    public void addEndereco (@NonNull Endereco endereco) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ENDERECO_RUA, endereco.getRua());
        values.put(ENDERECO_NUM, endereco.getNum());
        values.put(ENDERECO_COMP, endereco.getComp());
        values.put(ENDERECO_BAIRRO, endereco.getBairro());
        values.put(ENDERECO_CIDADE, endereco.getCidade().getId());
        values.put(ENDERECO_CLIENTE, endereco.getCliente().getId());

        db.insert(ENDERECO_TABLE, null, values);
    }

    public void deleteEndereco (@NonNull Endereco endereco) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ENDERECO_TABLE, ENDERECO_ID + " = ? ", new String[]{
                String.valueOf(endereco.getId())
        });
        db.close();
    }

    public void deleteEnderecoById (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ENDERECO_TABLE, ENDERECO_ID + " = ? ", new String[]{
                String.valueOf(id)
        });
        db.close();
    }

    public Endereco selectEndereco (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(ENDERECO_TABLE,
                new String[] {
                        ENDERECO_ID, ENDERECO_NUM, ENDERECO_RUA, ENDERECO_BAIRRO, ENDERECO_COMP,
                        ENDERECO_REF, ENDERECO_CIDADE, ENDERECO_CLIENTE },
                ENDERECO_ID + " = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );

        Endereco endereco = new Endereco();

        if (cursor != null) {
            cursor.moveToFirst();

            endereco = new Endereco(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    selectCidade(cursor.getInt(6)),
                    selectCliente(cursor.getInt(7))

            );
        } else {
            endereco = null;
        }

        cursor.close();
        db.close();
        return endereco;
    }

    public Endereco selectEnderecoByCliente (@NonNull Cliente _cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = " SELECT " +
                " E." + ENDERECO_ID + ", E." + ENDERECO_NUM + ", E." + ENDERECO_RUA +
                ", E." + ENDERECO_BAIRRO + ", E." + ENDERECO_COMP + ", E." + ENDERECO_REF +
                ", E." + ENDERECO_CIDADE + ", E." + ENDERECO_CLIENTE +
                " FROM " + ENDERECO_TABLE + " E" +
                " WHERE E." + ENDERECO_CLIENTE + " = " + _cliente.getId();

        Cursor c = db.rawQuery(QUERY, null);

        Endereco endereco = new Endereco();

        if (c.moveToFirst()) {
            endereco = new Endereco(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    selectCidade(c.getInt(6)),
                    selectCliente(c.getInt(7)));

            Log.e("INFO DB SELECT ENDERECO", String.valueOf(endereco.getId()) + " " + endereco.getCliente().getNome());
        } else {
            c.close();
            db.close();
            return null;
        }

        c.close();
        db.close();
        return endereco;
    }

    public void updateEndereco (@NonNull Endereco endereco) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ENDERECO_RUA, endereco.getRua());
        values.put(ENDERECO_NUM, endereco.getNum());
        values.put(ENDERECO_COMP, endereco.getComp());
        values.put(ENDERECO_BAIRRO, endereco.getBairro());
        values.put(ENDERECO_CIDADE, endereco.getCidade().getId());
        values.put(ENDERECO_CLIENTE, endereco.getCliente().getId());

        db.update(ENDERECO_TABLE, values, ENDERECO_ID + " = ? ",
                new String[] {
                        String.valueOf(endereco.getId())
                });
        db.close();
    }

    // CRUD MARCA ///////////////////////////////////////////////////////////////////////////////

    public void addMarca (@NonNull Marca marca) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MARCA_DESC, marca.getDescricao());

        db.insert(MARCA_TABLE, null, values);
        db.close();
    }

    public void deleteMarca (@NonNull Marca marca) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MARCA_TABLE, MARCA_ID + " = ? ", new String[]{
                String.valueOf(marca.getId())
        });
        db.close();
    }

    public void deleteMarcaById (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MARCA_TABLE, MARCA_ID + " = ? ", new String[] {
                String.valueOf(_id)
        });
        db.close();
    }

    public Marca selectMarca (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(MARCA_TABLE,
                new String[] {
                        MARCA_ID, MARCA_DESC },
                MARCA_ID + " = ?",
                new String[] { String.valueOf(_id) },
                null,
                null,
                null,
                null
        );

        Marca marca = new Marca();

        if (cursor != null) {
            cursor.moveToFirst();

            marca = new Marca(
                    cursor.getInt(0),
                    cursor.getString(1)
            );
        } else {
            marca = null;
        }

        cursor.close();
        db.close();
        return marca;
    }

    public void updateMarca (@NonNull Marca marca) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MARCA_DESC, marca.getDescricao());

        db.update(MARCA_TABLE, values, MARCA_ID + " = ? ",
                new String[] {
                        String.valueOf(marca.getId())
                });
        db.close();
    }

    public List<Marca> listMarcas () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Marca> marcas = new ArrayList<Marca>();

        String QUERY = "SELECT * FROM " + MARCA_TABLE;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Marca marca = new Marca();

                marca.setId(cursor.getInt(0));
                marca.setDescricao(cursor.getString(1));

                marcas.add(marca);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return marcas;
    }

    public List<Marca> listMarcasOrdered () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Marca> marcas = new ArrayList<Marca>();

        String QUERY = "SELECT * FROM " + MARCA_TABLE +
                " ORDER BY " + MARCA_DESC;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Marca marca = new Marca();

                marca.setId(cursor.getInt(0));
                marca.setDescricao(cursor.getString(1));

                marcas.add(marca);
            } while (cursor.moveToNext());
        }

        db.close();
        return marcas;
    }

    public List<Marca> listMarcasByDesc (String _descricao) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Marca> marcas = new ArrayList<Marca>();

        String QUERY = "SELECT * FROM " + MARCA_TABLE +
                " WHERE " + MARCA_DESC + " LIKE '%" + _descricao + "%'" +
                " ORDER BY " + MARCA_DESC;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Marca marca = new Marca();

                marca.setId(cursor.getInt(0));
                marca.setDescricao(cursor.getString(1));

                marcas.add(marca);
            } while (cursor.moveToNext());
        } else {
            db.close();
            return null;
        }

        db.close();
        return marcas;
    }

    // CRUD LINHA ///////////////////////////////////////////////////////////////////////////////

    public void addLinha (@NonNull Linha linha) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(LINHA_DESC, linha.getDescricao());
        values.put(LINHA_MARCA, linha.getMarca().getId());

        db.insert(LINHA_TABLE, null, values);
        db.close();
    }

    public void deleteLinha (@NonNull Linha linha) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(LINHA_TABLE, LINHA_ID + " = ? ", new String[]{
                String.valueOf(linha.getId())
        });
        db.close();
    }

    public void deleteLinhaById (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(LINHA_TABLE, LINHA_ID + " = ? ", new String[] {
                String.valueOf(_id)
        });
        db.close();
    }

    public Linha selectLinha (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(LINHA_TABLE,
                new String[] {
                        LINHA_ID, LINHA_DESC, LINHA_MARCA },
                LINHA_ID + " = ?",
                new String[] { String.valueOf(_id) },
                null,
                null,
                null,
                null
        );

        Linha linha = new Linha();
        Marca marca = new Marca();

        if (cursor != null) {
            cursor.moveToFirst();

            linha = new Linha(
                    cursor.getInt(0),
                    cursor.getString(1),
                    marca = selectMarca(cursor.getInt(2))
            );
        } else {
            linha = null;
        }

        cursor.close();
        db.close();
        return linha;
    }

    public void updateLinha (@NonNull Linha linha) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LINHA_DESC, linha.getDescricao());
        values.put(LINHA_MARCA, linha.getMarca().getId());

        db.update(LINHA_TABLE, values, LINHA_ID + " = ? ",
                new String[] {
                        String.valueOf(linha.getId())
                });
        db.close();
    }

    public List<Linha> listLinhas () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Linha> linhas = new ArrayList<Linha>();

        String QUERY = "SELECT * FROM " + LINHA_TABLE;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Linha linha = new Linha();
                Marca marca = new Marca();

                linha.setId(cursor.getInt(0));
                linha.setDescricao(cursor.getString(1));
                linha.setMarca(marca = selectMarca(cursor.getInt(2)));

                linhas.add(linha);
            } while (cursor.moveToNext());
        }

        db.close();
        return linhas;
    }

    public List<Linha> listLinhasByMarca (@NonNull Marca marca) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Linha> linhas = new ArrayList<Linha>();

        String QUERY = " SELECT " +
                " L." + LINHA_ID + ", L." + LINHA_DESC + " L." + LINHA_MARCA +
                " FROM " + LINHA_TABLE + " L" +
                " WHERE L." + LINHA_MARCA + " = " + marca.getId();

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Linha linha = new Linha();

                linha.setId(cursor.getInt(0));
                linha.setDescricao(cursor.getString(1));
                linha.setMarca(marca);

                linhas.add(linha);
            } while (cursor.moveToNext());
        }

        db.close();
        return linhas;
    }

    public List<Linha> listLinhasByMarcaOrdered (@NonNull Marca marca) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Linha> linhas = new ArrayList<Linha>();

        String QUERY = " SELECT" +
                " L." + LINHA_ID + ", L." + LINHA_DESC + ", L." + LINHA_MARCA +
                " FROM " + LINHA_TABLE + " L" +
                " WHERE L." + LINHA_MARCA + " = " + marca.getId() +
                " ORDER BY L." + LINHA_DESC;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Linha linha = new Linha();

                linha.setId(cursor.getInt(0));
                linha.setDescricao(cursor.getString(1));
                linha.setMarca(marca);

                linhas.add(linha);
            } while (cursor.moveToNext());
        }

        db.close();
        return linhas;
    }

    public List<Linha> listLinhasByMarcaDesc (@NonNull Marca marca, String _descricao) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Linha> linhas = new ArrayList<Linha>();

        String QUERY = " SELECT" +
                " L." + LINHA_ID + ", L." + LINHA_DESC + ", L." + LINHA_MARCA +
                " FROM " + LINHA_TABLE + " L" +
                " WHERE L." + LINHA_MARCA + " = " + marca.getId() +
                " AND L." + LINHA_DESC + " LIKE '%" + _descricao + "%'" +
                " ORDER BY L." + LINHA_DESC;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Linha linha = new Linha();

                linha.setId(cursor.getInt(0));
                linha.setDescricao(cursor.getString(1));
                linha.setMarca(marca);

                linhas.add(linha);
            } while (cursor.moveToNext());
        } else {
            db.close();
            return null;
        }

        db.close();
        return linhas;
    }

    // CRUD CATEGORIA ///////////////////////////////////////////////////////////////////////////

    public void addCategoria (@NonNull Categoria categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CATEGORIA_DESC, categoria.getDescricao());
        values.put(CATEGORIA_COR, categoria.getCor());

        db.insert(CATEGORIA_TABLE, null, values);
        db.close();
    }

    public void deleteCategoria (@NonNull Categoria categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CATEGORIA_TABLE, CATEGORIA_ID + " = ? ", new String[]{
                String.valueOf(categoria.getId())
        });
        db.close();
    }

    public void deleteCategoriaById (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CATEGORIA_TABLE, CATEGORIA_ID + " = ? ", new String[] {
                String.valueOf(_id)
        });
        db.close();
    }

    public Categoria selectCategoria (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(CATEGORIA_TABLE,
                new String[] {
                        CATEGORIA_ID, CATEGORIA_DESC, CATEGORIA_COR },
                CATEGORIA_ID + " = ?",
                new String[] { String.valueOf(_id) },
                null,
                null,
                null,
                null
        );

        Categoria categoria = new Categoria();

        if (cursor != null) {
            cursor.moveToFirst();

            categoria = new Categoria(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
        } else {
            categoria = null;
        }

        cursor.close();
        db.close();
        return categoria;
    }

    public void updateCategoria (@NonNull Categoria categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORIA_DESC, categoria.getDescricao());
        values.put(CATEGORIA_COR, categoria.getCor());

        db.update(CATEGORIA_TABLE, values, CATEGORIA_ID + " = ? ",
                new String[] {
                        String.valueOf(categoria.getId())
                });
        db.close();
    }

    public List<Categoria> listCategorias () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Categoria> categorias = new ArrayList<Categoria>();

        String QUERY = "SELECT * FROM " + CATEGORIA_TABLE;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();

                categoria.setId(cursor.getInt(0));
                categoria.setDescricao(cursor.getString(1));
                categoria.setCor(cursor.getString(2));

                categorias.add(categoria);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categorias;
    }

    public List<Categoria> listCategoriasOrdered () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Categoria> categorias = new ArrayList<Categoria>();

        String QUERY = "SELECT * FROM " + CATEGORIA_TABLE +
                " ORDER BY " + CATEGORIA_DESC;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();

                categoria.setId(cursor.getInt(0));
                categoria.setDescricao(cursor.getString(1));
                categoria.setCor(cursor.getString(2));

                categorias.add(categoria);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categorias;
    }

    public List<Categoria> listCategoriasByDesc (String _descricao) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Categoria> categorias = new ArrayList<Categoria>();

        String QUERY = "SELECT * FROM " + CATEGORIA_TABLE +
                " WHERE " + CATEGORIA_DESC + " LIKE '%" + _descricao + "%'" +
                " ORDER BY " + CATEGORIA_DESC;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();

                categoria.setId(cursor.getInt(0));
                categoria.setDescricao(cursor.getString(1));
                categoria.setCor(cursor.getString(2));

                categorias.add(categoria);
            } while (cursor.moveToNext());
        } else {
            db.close();
            return null;
        }

        cursor.close();
        db.close();
        return categorias;
    }

    // CRUD SUBCATEGORIA ////////////////////////////////////////////////////////////////////////

    public void addSubcat (@NonNull Subcat subcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SUBCAT_DESC, subcat.getDescricao());
        values.put(SUBCAT_CATEGORIA, subcat.getCategoria().getId());

        db.insert(SUBCAT_TABLE, null, values);
        db.close();
    }

    public void deleteSubcat (@NonNull Subcat subcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SUBCAT_TABLE, SUBCAT_ID + " = ? ", new String[]{
                String.valueOf(subcat.getId())
        });
        db.close();
    }

    public void deleteSubcatById (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SUBCAT_TABLE, SUBCAT_ID + " = ? ", new String[] {
                String.valueOf(_id)
        });
        db.close();
    }

    public Subcat selectSubcat (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(SUBCAT_TABLE,
                new String[] {
                        SUBCAT_ID, SUBCAT_DESC, SUBCAT_CATEGORIA },
                SUBCAT_ID + " = ?",
                new String[] { String.valueOf(_id) },
                null,
                null,
                null,
                null
        );

        Subcat subcat = new Subcat();
        Categoria categoria = new Categoria();

        if (cursor != null) {
            cursor.moveToFirst();

            subcat = new Subcat(
                    cursor.getInt(0),
                    cursor.getString(1),
                    categoria = selectCategoria(cursor.getInt(2))
            );
        } else {
            subcat = null;
        }

        cursor.close();
        db.close();
        return subcat;
    }

    public Subcat selectMaxSubcat () {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SUBCAT_TABLE +
                " WHERE " + SUBCAT_ID +
                " = (SELECT MAX(" + SUBCAT_ID + ") " +
                "FROM " + SUBCAT_TABLE +
                ")", null);

        if (cursor != null)
            cursor.moveToFirst();

        Subcat sc = new Subcat(
                cursor.getInt(0),
                cursor.getString(1),
                selectCategoria(cursor.getInt(2))
        );

        db.close();

        return sc;

    }

    public void updateSubcat (@NonNull Subcat subcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUBCAT_DESC, subcat.getDescricao());
        values.put(SUBCAT_CATEGORIA, subcat.getCategoria().getId());

        db.update(SUBCAT_TABLE, values, SUBCAT_ID + " = ? ",
                new String[] {
                        String.valueOf(subcat.getId())
                });
        db.close();
    }

    public List<Subcat> listSubcats () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Subcat> subcats = new ArrayList<Subcat>();

        String QUERY = "SELECT * FROM " + SUBCAT_TABLE;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Subcat subcat = new Subcat();
                Categoria categoria = new Categoria();

                subcat.setId(cursor.getInt(0));
                subcat.setDescricao(cursor.getString(1));
                subcat.setCategoria(categoria = selectCategoria(cursor.getInt(2)));

                subcats.add(subcat);
            } while (cursor.moveToNext());
        }

        db.close();
        return subcats;
    }

    public List<Subcat> listSubcatsByCategoria (@NonNull Categoria _categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Subcat> subcats = new ArrayList<Subcat>();

        String QUERY = " SELECT " +
                " S." + SUBCAT_ID + ", S." + SUBCAT_DESC + " S." + SUBCAT_CATEGORIA +
                " FROM " + SUBCAT_TABLE + " S" +
                " WHERE S." + SUBCAT_CATEGORIA + " = " + _categoria.getId();

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Subcat subcat = new Subcat();

                subcat.setId(cursor.getInt(0));
                subcat.setDescricao(cursor.getString(1));
                subcat.setCategoria(_categoria);

                subcats.add(subcat);
            } while (cursor.moveToNext());
        }

        db.close();
        return subcats;
    }

    public List<Subcat> listSubcatsByCategoriaOrdered (@NonNull Categoria _categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Subcat> subcats = new ArrayList<Subcat>();

        String QUERY = " SELECT " +
                " S." + SUBCAT_ID + ", S." + SUBCAT_DESC + ", S." + SUBCAT_CATEGORIA +
                " FROM " + SUBCAT_TABLE + " S" +
                " WHERE S." + SUBCAT_CATEGORIA + " = " + _categoria.getId() +
                " ORDER BY S." + SUBCAT_DESC;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Subcat subcat = new Subcat();

                subcat.setId(cursor.getInt(0));
                subcat.setDescricao(cursor.getString(1));
                subcat.setCategoria(_categoria);

                subcats.add(subcat);
            } while (cursor.moveToNext());
        }

        db.close();
        return subcats;
    }

    public List<Subcat> listSubcatsByCategoriaDesc (@NonNull Categoria _categoria, String _descricao) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Subcat> subcats = new ArrayList<Subcat>();

        String QUERY = " SELECT" +
                " S." + SUBCAT_ID + ", S." + SUBCAT_DESC + ", S." + SUBCAT_CATEGORIA +
                " FROM " + SUBCAT_TABLE + " S" +
                " WHERE S." + SUBCAT_CATEGORIA + " = " + _categoria.getId() +
                " AND S." + SUBCAT_DESC + " LIKE '%" + _descricao + "%'" +
                " ORDER BY S." + SUBCAT_DESC;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Subcat subcat = new Subcat();

                subcat.setId(cursor.getInt(0));
                subcat.setDescricao(cursor.getString(1));
                subcat.setCategoria(_categoria);

                subcats.add(subcat);
            } while (cursor.moveToNext());
        } else {
            db.close();
            return null;
        }

        db.close();
        return subcats;
    }

    public boolean checkSubcatOnProdSubcats (@NonNull Produto _produto, @NonNull Subcat _subcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = " SELECT * " +
                " FROM " + PROD_SUBCAT_TABLE + " P " +
                " WHERE P." + PROD_SUBCAT_PROD + " = " + _produto.getId() +
                " AND P." + PROD_SUBCAT_SUBCAT + " = " + _subcat.getId();

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
        // SELECT * FROM PRODSUBCATS P WHERE PRODUTO = PRODUTO AND SUBCAT = SUBCAT
    }

    // CRUD PRODUTO /////////////////////////////////////////////////////////////////////////////

    public void addProduto (@NonNull Produto _produto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PRODUTO_DESC, _produto.getDescricao());
        values.put(PRODUTO_VALOR, _produto.getValor());
        values.put(PRODUTO_LINHA, _produto.getLinha().getId());

        db.insert(PRODUTO_TABLE, null, values);
    }

    public void deleteProduto (@NonNull Produto _produto) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PRODUTO_TABLE, PRODUTO_ID + " = ? ", new String[]{
                String.valueOf(_produto.getId())
        });
        db.close();
    }

    public void deleteProdutoById (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PRODUTO_TABLE, PRODUTO_ID + " = ? ", new String[]{
                String.valueOf(_id)
        });
        db.close();
    }

    public Produto selectProduto (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(PRODUTO_TABLE,
                new String[] {
                        PRODUTO_ID, PRODUTO_DESC, PRODUTO_VALOR, PRODUTO_LINHA },
                PRODUTO_ID + " = ?",
                new String[] { String.valueOf(_id) },
                null,
                null,
                null,
                null
        );

        Produto produto = new Produto();
        Linha linha = new Linha();

        if (cursor != null) {
            cursor.moveToFirst();

            produto = new Produto(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    linha = selectLinha(cursor.getInt(3))
            );
        } else {
            produto = null;
        }

        cursor.close();
        db.close();
        return produto;
    }

    public Produto selectMaxProduto () {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + PRODUTO_TABLE +
                " WHERE " + PRODUTO_ID +
                " = (SELECT MAX(" + PRODUTO_ID + ") " +
                "FROM " + PRODUTO_TABLE +
                ")", null);

        if (cursor != null)
            cursor.moveToFirst();

        Produto produto = new Produto(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                selectLinha(cursor.getInt(3))
        );

        db.close();

        return produto;
    }

    public void updateProduto (@NonNull Produto produto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUTO_DESC, produto.getDescricao());
        values.put(PRODUTO_VALOR, produto.getValor());
        values.put(PRODUTO_LINHA, produto.getLinha().getId());

        db.update(PRODUTO_TABLE, values, PRODUTO_ID + " = ? ",
                new String[] {
                        String.valueOf(produto.getId())
                });
        db.close();
    }

    public List<Produto> listAllProdutos () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Produto> produtos = new ArrayList<Produto>();

        String QUERY = "SELECT * FROM " + PRODUTO_TABLE;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto();

                produto.setId(cursor.getInt(0));
                produto.setDescricao(cursor.getString(1));
                produto.setValor(cursor.getDouble(2));
                produto.setLinha(selectLinha(cursor.getInt(3)));

                produtos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return produtos;
    }

    public List<Produto> listAllProdutosOrdered () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Produto> produtos = new ArrayList<Produto>();

        String QUERY = "SELECT * FROM " + PRODUTO_TABLE
                    + " ORDER BY " + PRODUTO_DESC;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto();

                produto.setId(cursor.getInt(0));
                produto.setDescricao(cursor.getString(1));
                produto.setValor(cursor.getDouble(2));
                produto.setLinha(selectLinha(cursor.getInt(3)));

                produtos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return produtos;
    }

    public List<Produto> listAllProdutosByDesc (String _descricao) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Produto> produtos = new ArrayList<Produto>();

        String QUERY = " SELECT " +
                " P." + PRODUTO_ID + ", P." + PRODUTO_DESC +
                ", P." + PRODUTO_VALOR + ", P." + PRODUTO_LINHA +
                " FROM " + PRODUTO_TABLE + " P" +
                " WHERE P." + PRODUTO_DESC + " LIKE '%" + _descricao + "%'" +
                " ORDER BY P." + PRODUTO_DESC;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto();

                produto.setId(cursor.getInt(0));
                produto.setDescricao(cursor.getString(1));
                produto.setValor(cursor.getDouble(2));
                produto.setLinha(selectLinha(cursor.getInt(3)));

                produtos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return produtos;
    }

    public List<Produto> listProdutosFromCategoria (Categoria _categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Produto> produtos = new ArrayList<Produto>();

        String QUERY = "SELECT P." + PRODUTO_ID + ", P." + PRODUTO_DESC +
                ", P." + PRODUTO_VALOR + ", P." + PRODUTO_LINHA +
                " FROM " + PRODUTO_TABLE + " P" +
                " JOIN " + PROD_SUBCAT_TABLE + " PS" +
                " ON P." + PRODUTO_ID + " == PS." + PROD_SUBCAT_PROD +
                " JOIN " + SUBCAT_TABLE + " S" +
                " ON S." + SUBCAT_ID + " == PS." + PROD_SUBCAT_SUBCAT +
                " WHERE S." + SUBCAT_CATEGORIA + " == " + _categoria;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto();

                produto.setId(cursor.getInt(0));
                produto.setDescricao(cursor.getString(1));
                produto.setValor(cursor.getDouble(2));
                produto.setLinha(selectLinha(cursor.getInt(3)));

                produtos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return produtos;
    }

    public List<Produto> listProdutosFromSubcat (Subcat _subcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Produto> produtos = new ArrayList<Produto>();

        String QUERY = "SELECT P." + PRODUTO_ID + ", P." + PRODUTO_DESC +
                ", P." + PRODUTO_VALOR + ", P." + PRODUTO_LINHA +
                " FROM " + PRODUTO_TABLE + " P" +
                " JOIN " + PROD_SUBCAT_TABLE + " PS" +
                " ON P." + PRODUTO_ID + " == PS." + PROD_SUBCAT_PROD +
                " WHERE PS." + PROD_SUBCAT_SUBCAT + " == " + _subcat;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto();

                produto.setId(cursor.getInt(0));
                produto.setDescricao(cursor.getString(1));
                produto.setValor(cursor.getDouble(2));
                produto.setLinha(selectLinha(cursor.getInt(3)));

                produtos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return produtos;
    }

    public List<Produto> listProdutosByMarca (Marca _marca) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Produto> produtos = new ArrayList<Produto>();

        String QUERY = "SELECT P." + PRODUTO_ID + ", P." + PRODUTO_DESC +
                ", P." + PRODUTO_VALOR + ", P." + PRODUTO_LINHA +
                " FROM " + PRODUTO_TABLE + " P" +
                " JOIN " + LINHA_TABLE + " L" +
                " ON P." + PRODUTO_LINHA + " == L." + LINHA_ID +
                " WHERE L." + LINHA_MARCA + " == " + _marca;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto();

                produto.setId(cursor.getInt(0));
                produto.setDescricao(cursor.getString(1));
                produto.setValor(cursor.getDouble(2));
                produto.setLinha(selectLinha(cursor.getInt(3)));

                produtos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return produtos;
    }

    public List<Produto> listProdutosByLinha (Linha _linha) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Produto> produtos = new ArrayList<Produto>();

        String QUERY = "SELECT P." + PRODUTO_ID + ", P." + PRODUTO_DESC +
                ", P." + PRODUTO_VALOR + ", P." + PRODUTO_LINHA +
                " FROM " + PRODUTO_TABLE + " P" +
                " WHERE P." + PRODUTO_LINHA + " == " + _linha;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto();

                produto.setId(cursor.getInt(0));
                produto.setDescricao(cursor.getString(1));
                produto.setValor(cursor.getDouble(2));
                produto.setLinha(selectLinha(cursor.getInt(3)));

                produtos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return produtos;
    }

    public int selectCountProdutos () {
        SQLiteDatabase db = this.getWritableDatabase();

        int count = 0;

        String QUERY = "SELECT COUNT(*) FROM " + PRODUTO_TABLE;
        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst())
            count =  c.getInt(0);

        return count;
    }

    // CRUD PROD_SUBCAT /////////////////////////////////////////////////////////////////////////

    public void addProdSubcat (@NonNull ProdSubcat prodSubcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PROD_SUBCAT_PROD, prodSubcat.getProduto().getId());
        values.put(PROD_SUBCAT_SUBCAT, prodSubcat.getSubcat().getId());


        db.insert(PROD_SUBCAT_TABLE, null, values);
    }

    public void deleteProdSubcat (@NonNull ProdSubcat prodSubcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = (" DELETE FROM " + PROD_SUBCAT_TABLE +
                " WHERE "   + PROD_SUBCAT_PROD      + " == " + prodSubcat.getProduto().getId() +
                " AND "     + PROD_SUBCAT_SUBCAT    + " == " + prodSubcat.getSubcat().getId());

        db.rawQuery(QUERY, null);
        db.close();
    }

    public ProdSubcat selectFirstProdSubcatByProd (@NonNull Produto produto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ProdSubcat prodSubcat = new ProdSubcat();

        String QUERY = "SELECT * FROM " + PROD_SUBCAT_TABLE +
                " WHERE " + PROD_SUBCAT_PROD + " == " + produto.getId();

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            prodSubcat = new ProdSubcat();
            Subcat subcat = selectSubcat(cursor.getInt(1));

            prodSubcat.setProduto(produto);
            prodSubcat.setSubcat(subcat);

            return prodSubcat;
        } else {
            return null;
        }
    }

    public List<ProdSubcat> listAllProdSubcats () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<ProdSubcat> prodSubcats = new ArrayList<ProdSubcat>();

        String QUERY = "SELECT * FROM " + PROD_SUBCAT_TABLE;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                ProdSubcat prodSubcat = new ProdSubcat();
                Produto produto = selectProduto(cursor.getInt(0));
                Subcat subcat = selectSubcat(cursor.getInt(1));

                prodSubcat.setProduto(produto);
                prodSubcat.setSubcat(subcat);

                prodSubcats.add(prodSubcat);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return prodSubcats;
    }

    public List<ProdSubcat> listProdSubcatsByProduto (@NonNull Produto _produto) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<ProdSubcat> prodSubcats = new ArrayList<ProdSubcat>();

        String QUERY = "SELECT * FROM " + PROD_SUBCAT_TABLE +
                " WHERE " + PROD_SUBCAT_PROD + " == " + _produto.getId();
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                ProdSubcat prodSubcat = new ProdSubcat();
                Subcat subcat = selectSubcat(cursor.getInt(1));

                prodSubcat.setProduto(_produto);
                prodSubcat.setSubcat(subcat);

                prodSubcats.add(prodSubcat);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return prodSubcats;
    }

    public List<ProdSubcat> ListProdSubcatsBySubcats (@NonNull Subcat _subcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<ProdSubcat> prodSubcats = new ArrayList<ProdSubcat>();

        String QUERY = "SELECT * FROM " + PROD_SUBCAT_TABLE +
                " WHERE " + PROD_SUBCAT_SUBCAT + " == " + _subcat.getId();
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                ProdSubcat prodSubcat = new ProdSubcat();
                Produto produto = selectProduto(cursor.getInt(0));

                prodSubcat.setProduto(produto);
                prodSubcat.setSubcat(_subcat);

                prodSubcats.add(prodSubcat);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return prodSubcats;
    }

    // CRUD FORMA_PGTO //////////////////////////////////////////////////////////////////////////

    public void addFormaPgto (@NonNull FormaPgto _formaPgto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FORMA_PGTO_DESC, _formaPgto.getDescricao());
        values.put(FORMA_PGTO_PRAZO, _formaPgto.getPrazo());
        values.put(FORMA_PGTO_PARC, _formaPgto.getParcelavel());

        db.insert(FORMA_PGTO_TABLE, null, values);
        db.close();
    }

    public void deleteFormaPgto (@NonNull FormaPgto _formaPgto) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(FORMA_PGTO_TABLE, FORMA_PGTO_ID + " = ? ", new String[]{
                String.valueOf(_formaPgto.getId())
        });
        db.close();
    }

    public void deleteFormaPgtoById (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(FORMA_PGTO_TABLE, FORMA_PGTO_ID + " = ? ", new String[] {
                String.valueOf(_id)
        });
        db.close();
    }

    public FormaPgto selectFormaPgto (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(FORMA_PGTO_TABLE,
                new String[] {
                        FORMA_PGTO_ID, FORMA_PGTO_DESC, FORMA_PGTO_PRAZO, FORMA_PGTO_PARC },
                FORMA_PGTO_ID + " = ?",
                new String[] { String.valueOf(_id) },
                null,
                null,
                null,
                null
        );

        FormaPgto formaPgto = new FormaPgto();

        if (cursor != null) {
            cursor.moveToFirst();

            formaPgto = new FormaPgto(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3)
            );
        } else {
            formaPgto = null;
        }

        cursor.close();
        db.close();
        return formaPgto;
    }

    public FormaPgto selectMaxFormaPgto () {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + FORMA_PGTO_TABLE +
                " WHERE " + FORMA_PGTO_ID +
                " = (SELECT MAX(" + FORMA_PGTO_ID + ") " +
                "FROM " + FORMA_PGTO_TABLE +
                ")", null);

        if (cursor != null)
            cursor.moveToFirst();

        FormaPgto fp = new FormaPgto(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3)
        );

        db.close();

        return fp;
    }

    public void updateFormaPgto (@NonNull FormaPgto _formaPgto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FORMA_PGTO_DESC, _formaPgto.getDescricao());
        values.put(FORMA_PGTO_PRAZO, _formaPgto.getPrazo());
        values.put(FORMA_PGTO_PARC, _formaPgto.getParcelavel());


        db.update(FORMA_PGTO_TABLE, values, FORMA_PGTO_ID + " = ? ",
                new String[] {
                        String.valueOf(_formaPgto.getId())
                });
        db.close();
    }

    public List<FormaPgto> listFormaPgto () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<FormaPgto> formaPgtos = new ArrayList<FormaPgto>();

        String QUERY = "SELECT * FROM " + FORMA_PGTO_TABLE;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                FormaPgto formaPgto = new FormaPgto();

                formaPgto.setId(cursor.getInt(0));
                formaPgto.setDescricao(cursor.getString(1));
                formaPgto.setPrazo(cursor.getInt(2));
                formaPgto.setParcelavel(cursor.getInt(3));

                formaPgtos.add(formaPgto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return formaPgtos;
    }

    public List<FormaPgto> listFormaPgtoOrdered () {
        SQLiteDatabase db = this.getWritableDatabase();

        List<FormaPgto> formaPgtos = new ArrayList<FormaPgto>();

        String QUERY = "SELECT * FROM " + FORMA_PGTO_TABLE +
                " ORDER BY " + FORMA_PGTO_DESC;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                FormaPgto formaPgto = new FormaPgto();

                formaPgto.setId(cursor.getInt(0));
                formaPgto.setDescricao(cursor.getString(1));
                formaPgto.setPrazo(cursor.getInt(2));
                formaPgto.setParcelavel(cursor.getInt(3));

                formaPgtos.add(formaPgto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return formaPgtos;
    }

    public List<FormaPgto> listFormaPgtoByDesc (String _descricao) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<FormaPgto> formaPgtos = new ArrayList<FormaPgto>();

        String QUERY = " SELECT " +
                " F." + FORMA_PGTO_ID + ", F." + FORMA_PGTO_DESC +
                ", F." + FORMA_PGTO_PRAZO + ", F." + FORMA_PGTO_PARC +
                " FROM " + FORMA_PGTO_TABLE + " F" +
                " WHERE P." + FORMA_PGTO_DESC + " LIKE '%" + _descricao + "%'" +
                " ORDER BY P." + FORMA_PGTO_DESC;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                FormaPgto formaPgto = new FormaPgto();

                formaPgto.setId(cursor.getInt(0));
                formaPgto.setDescricao(cursor.getString(1));
                formaPgto.setPrazo(cursor.getInt(2));
                formaPgto.setParcelavel(cursor.getInt(3));

                formaPgtos.add(formaPgto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return formaPgtos;
    }

    // CRUD VENDA ///////////////////////////////////////////////////////////////////////////////

    public void addVenda (@NonNull Venda _venda) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (_venda.getId() >= 0) {
            Log.e("INFO ADD VENDA", "MAIOR OU IGUAL A ZERO [[[ " + String.valueOf(_venda.getId()) + " ]]]");
            values.put(VENDA_ID, _venda.getId());
        } else {
            Log.e("INFO ADD VENDA", "NÃO ENTROU NO IF");
        }

        values.put(VENDA_DATA, _venda.getData());
        values.put(VENDA_VALOR, _venda.getValor());
        values.put(VENDA_CLIENTE, _venda.getCliente().getId());
        if (_venda.getPgto() != null)
            values.put(VENDA_PGTO, _venda.getPgto().getId());
        else
            values.put(VENDA_PGTO, -1);
        db.insert(VENDA_TABLE, null, values);
        db.close();
    }

    public void deleteVenda (@NonNull Venda _venda) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(VENDA_TABLE, VENDA_ID + " = ? ", new String[]{
                String.valueOf(_venda.getId())
        });
        db.close();
    }

    public void deleteVendaById (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(VENDA_TABLE, VENDA_ID + " = ? ", new String[] {
                String.valueOf(_id)
        });
        db.close();
    }

    public Venda selectVenda (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(VENDA_TABLE,
                new String[] {
                        VENDA_ID, VENDA_DATA, VENDA_VALOR, VENDA_CLIENTE, VENDA_PGTO },
                VENDA_ID + " = ?",
                new String[] { String.valueOf(_id) },
                null,
                null,
                null,
                null
        );

        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        Cliente c = selectCliente(cursor.getInt(3));
        Pgto p = new Pgto();
        if (cursor.getInt(4) >= 0)
            p = selectPgto(cursor.getInt(4));
        else
            p = null;

        Venda venda = new Venda(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                c,
                p
        );

        db.close();
        return venda;
    }

    public Venda selectMaxVenda () {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + VENDA_TABLE +
                " WHERE " + VENDA_ID +
                " = (SELECT MAX(" + VENDA_ID + ") " +
                "FROM " + VENDA_TABLE +
                ")", null);

        if (cursor != null)
            cursor.moveToFirst();

        Cliente c = selectCliente(cursor.getInt(3));
        Pgto p = new Pgto();
        if (cursor.getInt(4) >= 0)
            p = selectPgto(cursor.getInt(4));
        else
            p = null;

        Venda venda = new Venda(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                c,
                p
        );

        db.close();
        return venda;
    }

    public void updateVenda (@NonNull Venda _venda) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(VENDA_DATA, _venda.getData());
        values.put(VENDA_VALOR, _venda.getValor());
        values.put(VENDA_CLIENTE, _venda.getCliente().getId());
        values.put(VENDA_PGTO, _venda.getPgto().getId());

        db.update(VENDA_TABLE, values, VENDA_ID + " = ? ",
                new String[] {
                        String.valueOf(_venda.getId())
                });

        db.close();
    }

    public List<Venda> listAllVendas() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Venda> vendas = new ArrayList<Venda>();

        String QUERY = "SELECT * FROM " + VENDA_TABLE;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Cliente c = selectCliente(cursor.getInt(3));
                Pgto p = new Pgto();
                if (cursor.getInt(4) > 0)
                    p = selectPgto(cursor.getInt(4));
                else
                    p = null;
                Venda venda = new Venda(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        c,
                        p
                );

                vendas.add(venda);
            } while (cursor.moveToNext());
        }

        db.close();

        return vendas;
    }

    public List<Venda> listAllVendasByCliente (@NonNull Cliente _cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Venda> vendas = new ArrayList<Venda>();

        String QUERY = "SELECT " +
                " V." + VENDA_ID           + ", V." + VENDA_DATA +
                ", V." + VENDA_VALOR        + ", V." + VENDA_CLIENTE +
                ", V." + VENDA_PGTO         +
                " FROM " + VENDA_TABLE + " V" +
                " WHERE V." + VENDA_CLIENTE + " == " + _cliente.getId();

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Cliente c = selectCliente(cursor.getInt(3));
                Pgto p = selectPgto(cursor.getInt(4));

                Venda venda = new Venda(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        c,
                        p
                );

                vendas.add(venda);
            } while (cursor.moveToNext());
        }

        db.close();

        return vendas;
    }

    public List<Venda> listAllVendasByDate (String _date) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Venda> vendas = new ArrayList<Venda>();

        String QUERY = "SELECT " +
                " V." + VENDA_ID            + ", V." + VENDA_DATA +
                ", V." + VENDA_VALOR        + ", V." + VENDA_CLIENTE +
                ", V." + VENDA_PGTO         +
                " FROM " + VENDA_TABLE + " V" +
                " ORDER BY " + VENDA_DATA;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Cliente c = selectCliente(cursor.getInt(3));
                Pgto p = selectPgto(cursor.getInt(4));

                Venda venda = new Venda(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        c,
                        p
                );

                vendas.add(venda);
            } while (cursor.moveToNext());
        }

        db.close();

        return vendas;
    }

    public int selectCountVendas () {
        SQLiteDatabase db = this.getWritableDatabase();

        int count = 0;

        String QUERY = "SELECT COUNT(*) FROM " + VENDA_TABLE;
        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst())
            count =  c.getInt(0);

        return count;
    }

    // CRUD PRODUTO X VENDA /////////////////////////////////////////////////////////////////////

    public void addProdVenda (@NonNull ProdVenda _prodVenda) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PROD_VENDA_PROD, _prodVenda.getProduto().getId());
        values.put(PROD_VENDA_VENDA, _prodVenda.getVenda().getId());
        values.put(PROD_VENDA_QTD, _prodVenda.getQtd());
        values.put(PROD_VENDA_VALOR, _prodVenda.getValor_unit());

        db.insert(PROD_VENDA_TABLE, null, values);
    }

    public void deleteProdVenda (@NonNull ProdVenda _prodVenda) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = (" DELETE FROM " + PROD_VENDA_TABLE +
                " WHERE "   + PROD_VENDA_PROD      + " == " + _prodVenda.getProduto().getId() +
                " AND "     + PROD_VENDA_VENDA    + " == " + _prodVenda.getVenda().getId());

        db.rawQuery(QUERY, null);
        db.close();
    }

    public List<ProdVenda> listProdVendaByVenda (@NonNull Venda _venda) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<ProdVenda> prodVendas = new ArrayList<>();

        String QUERY = "SELECT * FROM " + PROD_VENDA_TABLE +
                " WHERE " + PROD_VENDA_VENDA + " == " + _venda.getId();
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Venda venda = selectVenda(cursor.getInt(0));
                Produto produto = selectProduto(cursor.getInt(1));

                ProdVenda prodVenda = new ProdVenda(
                        venda,
                        produto,
                        cursor.getInt(2),
                        cursor.getDouble(3)
                );

                prodVendas.add(prodVenda);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return prodVendas;
    }

    public int qtdProdVendaByVenda (@NonNull Venda _venda) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = " SELECT COUNT(*) FROM " + PROD_VENDA_TABLE +
                " WHERE " + PROD_VENDA_VENDA + " == " + _venda.getId();
        Cursor cursor = db.rawQuery(QUERY, null);

        int qtd;
        if (cursor.moveToFirst()) {
            qtd = cursor.getInt(0);
        } else {
            qtd = 0;
        }

        return qtd;
    }

    // CRUD PAGAMENTO ///////////////////////////////////////////////////////////////////////////

    public void addPgto (@NonNull Pgto _pgto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PGTO_CLIENTE, _pgto.getCliente().getId());
        values.put(PGTO_FORMA_PGTO, _pgto.getFormaPgto().getId());
        values.put(PGTO_VALOR, _pgto.getValor());
        values.put(PGTO_DATA, _pgto.getData());
        values.put(PGTO_JUROS, _pgto.getJuros());
        values.put(PGTO_PARC, _pgto.getParcelas());

        db.insert(PGTO_TABLE, null, values);
        db.close();
    }

    public void deletePgto (@NonNull Pgto _pgto) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = (" DELETE FROM " + PGTO_TABLE +
                " WHERE "   + PGTO_ID      + " == " + _pgto.getId()
        );

        db.rawQuery(QUERY, null);
        db.close();
    }

    public void deletePgtoById (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = (" DELETE FROM " + PGTO_TABLE +
                " WHERE "   + PGTO_ID      + " == " + _id
        );

        db.rawQuery(QUERY, null);
        db.close();
    }

    public Pgto selectPgto (int _id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query( PGTO_TABLE,
                new String[] {
                        PGTO_ID, PGTO_CLIENTE, PGTO_FORMA_PGTO,
                        PGTO_VALOR, PGTO_DATA, PGTO_JUROS,
                        PGTO_PARC},
                PGTO_ID + " = ?",
                new String[] { String.valueOf(_id) },
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Cliente c = selectCliente(cursor.getInt(1));
        FormaPgto fp = selectFormaPgto(cursor.getInt(2));

        Pgto pgto = new Pgto (
                cursor.getInt(0),
                c,
                fp,
                cursor.getDouble(3),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getInt(6)
        );

        db.close();
        return pgto;
    }

    public Pgto selectMaxPgto () {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + PGTO_TABLE +
                " WHERE " + PGTO_ID +
                " = (SELECT MAX(" + PGTO_ID + ") " +
                "FROM " + PGTO_TABLE +
                ")", null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Cliente c = selectCliente(cursor.getInt(1));
        FormaPgto fp = selectFormaPgto(cursor.getInt(2));

        Pgto pgto = new Pgto (
                cursor.getInt(0),
                c,
                fp,
                cursor.getDouble(3),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getInt(6)
        );

        db.close();
        return pgto;
    }

    public void updatePgto (@NonNull Pgto _pgto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PGTO_CLIENTE, _pgto.getCliente().getId());
        values.put(PGTO_FORMA_PGTO, _pgto.getFormaPgto().getId());
        values.put(PGTO_VALOR, _pgto.getValor());
        values.put(PGTO_DATA, _pgto.getData());
        values.put(PGTO_JUROS, _pgto.getJuros());
        values.put(PGTO_PARC, _pgto.getParcelas());

        db.update(PGTO_TABLE, values, PGTO_ID + " = ?",
                new String[] {
                        String.valueOf(_pgto.getId())
                });
        db.close();
    }

    public List<Pgto> listAllPgtosOrdered() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Pgto> pgtos = new ArrayList<>();

        String QUERY = "SELECT * FROM " + PGTO_TABLE +
                " ORDER BY " + PGTO_DATA;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Cliente c = selectCliente(cursor.getInt(3));
                FormaPgto fp = selectFormaPgto(cursor.getInt(4));

                Pgto pgto = new Pgto (
                        cursor.getInt(0),
                        c,
                        fp,
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getInt(6)
                );

                pgtos.add(pgto);
            } while (cursor.moveToNext());
        } else {
            return null;
        }

        db.close();

        return pgtos;
    }

    public List<Pgto> listAllPgtosByCliente (@NonNull Cliente _cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Pgto> pgtos = new ArrayList<>();

        String QUERY = "SELECT * FROM " + PGTO_TABLE +
                " WHERE " + PGTO_CLIENTE + " == " + _cliente.getId() +
                " ORDER BY " + PGTO_DATA;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Cliente c = selectCliente(cursor.getInt(3));
                FormaPgto fp = selectFormaPgto(cursor.getInt(4));

                Pgto pgto = new Pgto (
                        cursor.getInt(0),
                        c,
                        fp,
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getInt(6)
                );

                pgtos.add(pgto);
            } while (cursor.moveToNext());
        } else {
            return null;
        }

        db.close();

        return pgtos;
    }

    public List<Pgto> listAllPgtosByDate (String _date) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Pgto> pgtos = new ArrayList<>();

        String QUERY = "SELECT * FROM " + PGTO_TABLE +
                " WHERE " + PGTO_DATA + " == " + _date +
                " ORDER BY " + PGTO_DATA;

        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Cliente c = selectCliente(cursor.getInt(3));
                FormaPgto fp = selectFormaPgto(cursor.getInt(4));

                Pgto pgto = new Pgto (
                        cursor.getInt(0),
                        c,
                        fp,
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getInt(6)
                );

                pgtos.add(pgto);
            } while (cursor.moveToNext());
        } else {
            return null;
        }

        db.close();

        return pgtos;
    }

    public int selectCountPagamentos () {
        SQLiteDatabase db = this.getWritableDatabase();

        int count = 0;

        String QUERY = "SELECT COUNT(*) FROM " + PGTO_TABLE;
        Cursor c = db.rawQuery(QUERY, null);

        if (c.moveToFirst())
            count =  c.getInt(0);

        return count;
    }

    // CRUD USUARIO /////////////////////////////////////////////////////////////////////////////

    public void addUsuario (@NonNull Usuario _usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NOME, _usuario.getNome());
        values.put(USER_EMAIL, _usuario.getEmail());
        values.put(USER_TELEFONE, _usuario.getTelefone());
        values.put(USER_UID, _usuario.getUid());

        db.insert(USER_TABLE, null, values);


        db.close();
    }

    public void deleteUsuario (Usuario usuario, int id) {
        if (usuario != null) {
            SQLiteDatabase db = this.getWritableDatabase();

            db.delete(USER_TABLE, USER_ID + " = ? ", new String[] {
                    String.valueOf(usuario.getId())
            });
            db.close();
        } else
        if (id >= 0) {
            SQLiteDatabase db = this.getWritableDatabase();

            db.delete(USER_TABLE, USER_ID + " = ? ", new String[] {
                    String.valueOf(id)
            });
            db.close();
        }
    }

    public Usuario selectUsuario (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(USER_TABLE,
                new String[] {
                        USER_ID, USER_NOME, USER_EMAIL, USER_TELEFONE, USER_UID},
                USER_ID + " = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Usuario usuario = new Usuario(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );

        db.close();
        return usuario;
    }

    public Usuario selectUsuarioByUID (String UID) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(USER_TABLE,
                new String[] {
                        USER_ID, USER_NOME, USER_EMAIL, USER_TELEFONE, USER_UID},
                USER_UID + " = ?",
                new String[] { UID },
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Usuario usuario = new Usuario(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );

        db.close();
        return usuario;
    }

    Usuario selectMaxUsuario () {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM "    + USER_TABLE
                        + " WHERE "     + USER_ID + " = (SELECT MAX(" + USER_ID + ") "
                        + " FROM "      + USER_TABLE + ")",
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Usuario usuario = new Usuario(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );

        db.close();
        return usuario;
    }

    public void updateUsuario (@NonNull Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NOME, usuario.getNome());
        values.put(USER_EMAIL, usuario.getEmail());
        values.put(USER_TELEFONE, usuario.getTelefone());
        values.put(USER_UID, usuario.getUid());

        db.update(USER_TABLE, values, USER_ID + " = ?",
                new String[] { String.valueOf(usuario.getId()) });
        db.close();
    }

    // Preenchimento estados e cidades //////////////////////////////////////////////////////////

    void listDataEstadosCidades () {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = "SELECT * FROM " + ESTADO_TABLE;
        Cursor c = db.rawQuery(QUERY, null);
        Log.e("INFO DB ESTADOS", String.valueOf(c.getCount()));

        QUERY = "SELECT * FROM " + CLIENTE_TABLE;
        c = db.rawQuery(QUERY, null);
        Log.e("INFO DB CLIENTES", String.valueOf(c.getCount()));
    }

    void addAllCidades () {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String QUERY2 =
                    "INSERT INTO " + CIDADE_TABLE + " VALUES (?, ?, ?)";

            db.execSQL(QUERY2, new String[]{String.valueOf(2843), "Campo Largo", String.valueOf(18)});
            db.execSQL(QUERY2, new String[]{String.valueOf(2844), "Campo Magro", String.valueOf(18)});
            db.execSQL(QUERY2, new String[]{String.valueOf(2845), "Campo Mourão", String.valueOf(18)});
            db.execSQL(QUERY2, new String[]{String.valueOf(2846), "Cândido de Abreu", String.valueOf(18)});
            db.execSQL(QUERY2, new String[]{String.valueOf(2847), "Candói", String.valueOf(18)});
            db.execSQL(QUERY2, new String[]{String.valueOf(2848), "Cantagalo", String.valueOf(18)});
            db.execSQL(QUERY2, new String[]{String.valueOf(2849), "Capanema", String.valueOf(18)});
            db.execSQL(QUERY2, new String[]{String.valueOf(2850), "Capitão Leônidas Marques", String.valueOf(18)});

        } catch (Exception e) {
            Log.e("ERROR DB CIDADE", e.getMessage());
        }
    }

}


