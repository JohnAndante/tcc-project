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

import javax.security.auth.Subject;

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
                + "REFERENCES "     + CLIENTE_TABLE     + " (" + TELEFONE_CLIENTE + "))";

        ESTADO_QUERY        = "CREATE TABLE " + ESTADO_TABLE + "("
                + ESTADO_ID + " INTEGER PRIMARY KEY, "
                + ESTADO_NOME + " TEXT,"
                + ESTADO_UF + " TEXT)";

        CIDADE_QUERY        = "CREATE TABLE " + CIDADE_TABLE + "("
                + CIDADE_ID         + " INTEGER PRIMARY KEY, "
                + CIDADE_NOME       + " TEXT, "
                + CIDADE_ESTADO     + " INTEGER, "
                + "FOREIGN KEY ("   + CIDADE_ESTADO     + ") "
                + "REFERENCES "     + ESTADO_TABLE      + " (" + CIDADE_ESTADO + "))";

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
                + "REFERENCES "     + CLIENTE_TABLE     + " (" + ENDERECO_CLIENTE + "))";

        MARCA_QUERY         = "CREATE TABLE " + MARCA_TABLE + "( "
                + MARCA_ID          + " INTEGER PRIMARY KEY, "
                + MARCA_DESC        + " TEXT )";

        LINHA_QUERY         = "CREATE TABLE " + LINHA_TABLE + "( "
                + LINHA_ID          + " INTEGER PRIMARY KEY, "
                + LINHA_DESC        + " TEXT,  "
                + LINHA_MARCA       + " INTEGER, "
                + "FOREIGN KEY ("   + LINHA_MARCA       + ") "
                + "REFERENCES "     + MARCA_TABLE       + " (" + LINHA_MARCA + "))";

        CATEGORIA_QUERY     = "CREATE TABLE " + CATEGORIA_TABLE + "( "
                + CATEGORIA_ID      + " INTEGER PRIMARY KEY, "
                + CATEGORIA_DESC    + " TEXT, "
                + CATEGORIA_COR     + " TEXT )";

        SUBCAT_QUERY        = "CREATE TABLE " + SUBCAT_TABLE + "( "
                + SUBCAT_ID         + " INTEGER PRIMARY KEY, "
                + SUBCAT_DESC       + " TEXT, "
                + SUBCAT_CATEGORIA  + " INTEGER, "
                + "FOREIGN KEY ("   + SUBCAT_CATEGORIA  + ") "
                + "REFERENCES "     + CATEGORIA_TABLE   + " (" + SUBCAT_CATEGORIA + "))";

        PRODUTO_QUERY       = "CREATE TABLE " + PRODUTO_TABLE + "( "
                + PRODUTO_ID        + " INTEGER PRIMARY KEY, "
                + PRODUTO_DESC      + " TEXT, "
                + PRODUTO_VALOR     + " DECIMAL(8, 2), "
                + PRODUTO_LINHA     + " INTEGER, "
                + "FOREIGN KEY ("   + PRODUTO_LINHA     + ") "
                + "REFERENCES "     + LINHA_TABLE       + " (" + PRODUTO_LINHA + "))";

        PROD_SUBCAT_QUERY   = "CREATE TABLE " + PROD_SUBCAT_TABLE + "( "
                + PROD_SUBCAT_PROD   + " INTEGER, "
                + PROD_SUBCAT_SUBCAT + " INTEGER, "
                + "FOREIGN KEY ("    + PROD_SUBCAT_PROD   + ") "
                + "REFERENCES "      + PRODUTO_TABLE      + " (" + PROD_SUBCAT_PROD   + "), "
                + "FOREIGN KEY ("    + PROD_SUBCAT_SUBCAT + ") "
                + "REFERENCES "      + SUBCAT_TABLE       + " (" + PROD_SUBCAT_SUBCAT + "))";

        Log.i("DATABASE INFO", CLIENTE_QUERY);
        Log.i("DATABASE INFO", TELEFONE_QUERY);
        Log.i("DATABASE INFO", ENDERECO_QUERY);
        Log.i("DATABASE INFO", CIDADE_QUERY);
        Log.i("DATABASE INFO", ESTADO_QUERY);
        Log.i("DATABASE INFO", MARCA_QUERY);
        Log.i("DATABASE INFO", LINHA_QUERY);
        Log.i("DATABASE INFO", CATEGORIA_QUERY);
        Log.i("DATABASE INFO", SUBCAT_QUERY);
        Log.i("DATABASE INFO", PRODUTO_QUERY);
        Log.i("DATABASE INFO", PROD_SUBCAT_QUERY);

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

    public void addCidade (Cidade cidade) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CIDADE_NOME, cidade.getNome());
        values.put(CIDADE_ESTADO, cidade.getEstado().getId());

        db.insert(CIDADE_TABLE, null, values);
        db.close();
    }

    public void deleteCidade (Cidade cidade) {
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

    public List<Cidade> listAllCidadesByEstado (Estado estado) {
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

    public List<Cidade> listAllCidadesByEstadoAndNome (Estado estado, String nome) {
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

    public void updateCidade (Cidade cidade) {
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

    public void addEndereco (Endereco endereco) {
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

    public void deleteEndereco (Endereco endereco) {
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
        Cidade cidade = new Cidade();
        Cliente cliente = new Cliente();

        if (cursor != null) {
            cursor.moveToFirst();

            endereco = new Endereco(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cidade = selectCidade(cursor.getInt(6)),
                    cliente = selectCliente(cursor.getInt(7))

            );
        } else {
            endereco = null;
        }

        cursor.close();
        db.close();
        return endereco;
    }

    public Endereco selectEnderecoByCliente (Cliente _cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = " SELECT " +
                " E." + ENDERECO_ID + ", E." + ENDERECO_NUM + ", E." + ENDERECO_RUA +
                ", E." + ENDERECO_BAIRRO + ", E." + ENDERECO_COMP + ", E." + ENDERECO_REF +
                ", E." + ENDERECO_CIDADE + ", E." + ENDERECO_CLIENTE +
                " FROM " + ENDERECO_TABLE + " E" +
                " WHERE E." + ENDERECO_CLIENTE + " = " + _cliente.getId();

        Cursor c = db.rawQuery(QUERY, null);

        Endereco endereco = new Endereco();
        Cidade cidade = new Cidade();
        Cliente cliente = new Cliente();

        if (c.moveToFirst()) {
            endereco = new Endereco(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    cidade = selectCidade(c.getInt(6)),
                    cliente = selectCliente(c.getInt(7)));

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

    public void updateEndereco (Endereco endereco) {
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

    public void addMarca (Marca marca) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MARCA_DESC, marca.getDescricao());

        db.insert(MARCA_TABLE, null, values);
        db.close();
    }

    public void deleteMarca (Marca marca) {
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

            marca = new Marca (
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

    public void updateMarca (Marca marca) {
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
        }

        db.close();
        return marcas;
    }

    // CRUD LINHA ///////////////////////////////////////////////////////////////////////////////

    public void addLinha (Linha linha) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(LINHA_DESC, linha.getDescricao());
        values.put(LINHA_MARCA, linha.getMarca().getId());

        db.insert(LINHA_TABLE, null, values);
        db.close();
    }

    public void deleteLinha (Linha linha) {
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

            linha = new Linha (
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

    public void updateLinha (Linha linha) {
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

    public List<Linha> listLinhasByMarca (Marca marca) {
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

    public List<Linha> listLinhasByMarcaOrdered (Marca marca) {
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

    public List<Linha> listLinhasByMarcaDesc (Marca marca, String _descricao) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Linha> linhas = new ArrayList<Linha>();

        String QUERY = " SELECT " +
                " L." + LINHA_ID + ", L." + LINHA_DESC + " L." + LINHA_MARCA +
                " FROM " + LINHA_TABLE + " L" +
                " WHERE L." + LINHA_MARCA + " = " + marca.getId() +
                " && L." + LINHA_DESC + " LIKE '%" + _descricao + "%'" +
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

    // CRUD CATEGORIA ///////////////////////////////////////////////////////////////////////////

    public void addCategoria (Categoria categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CATEGORIA_DESC, categoria.getDescricao());
        values.put(CATEGORIA_COR, categoria.getCor());

        db.insert(CATEGORIA_TABLE, null, values);
        db.close();
    }

    public void deleteCategoria (Categoria categoria) {
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

    public void updateCategoria (Categoria categoria) {
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
        }

        cursor.close();
        db.close();
        return categorias;
    }

    // CRUD SUBCATEGORIA ////////////////////////////////////////////////////////////////////////

    public void addSubcat (Subcat subcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SUBCAT_DESC, subcat.getDescricao());
        values.put(SUBCAT_CATEGORIA, subcat.getCategoria().getId());

        db.insert(SUBCAT_TABLE, null, values);
        db.close();
    }

    public void deleteSubcat (Subcat subcat) {
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

            subcat = new Subcat (
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

    public void updateSubcat (Subcat subcat) {
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

    public List<Subcat> listSubcatsByCategoria (Categoria _categoria) {
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

    public List<Subcat> listSubcatsByCategoriaOrdered (Categoria _categoria) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Subcat> subcats = new ArrayList<Subcat>();

        String QUERY = " SELECT " +
                " S." + SUBCAT_ID + ", S." + SUBCAT_DESC + " S." + SUBCAT_CATEGORIA +
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

    public List<Subcat> listSubcatsByCategoriaDesc (Categoria _categoria, String _descricao) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Subcat> subcats = new ArrayList<Subcat>();

        String QUERY = " SELECT " +
                " S." + SUBCAT_ID + ", S." + SUBCAT_DESC + " S." + SUBCAT_CATEGORIA +
                " FROM " + SUBCAT_TABLE + " L" +
                " WHERE S." + SUBCAT_CATEGORIA + " = " + _categoria.getId() +
                " && S." + SUBCAT_DESC + " LIKE '%" + _descricao + "%'" +
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

    // CRUD PRODUTO /////////////////////////////////////////////////////////////////////////////

    public void addProduto (Produto _produto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PRODUTO_DESC, _produto.getDescricao());
        values.put(PRODUTO_VALOR, _produto.getValor());
        values.put(PRODUTO_LINHA, _produto.getLinha().getId());

        db.insert(PRODUTO_TABLE, null, values);
    }

    public void deleteProduto (Produto _produto) {
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

    public void updateProduto (Produto produto) {
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

    // CRUD PROD_SUBCAT /////////////////////////////////////////////////////////////////////////

    public void addProdSubcat (ProdSubcat prodSubcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PROD_SUBCAT_PROD, prodSubcat.getProduto().getId());
        values.put(PROD_SUBCAT_SUBCAT, prodSubcat.getSubcat().getId());


        db.insert(PROD_SUBCAT_TABLE, null, values);
    }

    public void deleteProdSubcat (ProdSubcat prodSubcat) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = (" DELETE FROM " + PROD_SUBCAT_TABLE +
                    " WHERE "   + PROD_SUBCAT_PROD      + " == " + prodSubcat.getProduto().getId() +
                    " &&"       + PROD_SUBCAT_SUBCAT    + " == " + prodSubcat.getSubcat().getId());

        db.rawQuery(QUERY, null);
        db.close();
    }

    public ProdSubcat selectFirstProdSubcatByProd (Produto produto) {
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

    public List<ProdSubcat> listProdSubcatsByProduto (Produto _produto) {
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

    public List<ProdSubcat> ListProdSubcatsBySubcats (Subcat _subcat) {
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


