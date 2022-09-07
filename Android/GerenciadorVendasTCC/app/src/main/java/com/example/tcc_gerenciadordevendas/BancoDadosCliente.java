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
                ESTADO_ID + " = ?",
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

    /*
    public Endereco selectEndereco (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(ENDERECO_TABLE,
                )
    }

     */

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

            Log.i("INFO DB CIDADE", "QUERY REALIZADA???");

        } catch (Exception e) {
            Log.e("ERROR DB CIDADE", e.getMessage());
        }
    }

}


