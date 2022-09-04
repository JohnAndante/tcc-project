package com.example.tcc_gerenciadordevendas;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddCliente extends AppCompatActivity {

    private Button Salvar;
    private Button Cancelar;

    private EditText editTextNome;
    private EditText editTextTelefone;
    private EditText editTextRua;
    private EditText editTextNum;
    private EditText editTextCompl;
    private EditText editTextBairro;
    private TextView textUf;
    private TextView textCidade;

    //private EditText editTextUf;
    //private EditText editTextCidade;


    // Variáveis utilizadas no spinner do estado, ajustar depois para seguir o padrão
    private ArrayList<String> arrayList;
    Dialog dialog;

    private Estado estadoSelecionado = null;


    private LinearLayout llButtons;

    BancoDadosCliente db = new BancoDadosCliente(this);

    int id_cliente;

    private Boolean buttonsVisibility = true;
    private Boolean isEditTextFocused = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);

        llButtons = (LinearLayout) findViewById(R.id.llBotoesCliente);

        initButtonsCfg();
        initButtonsOnclick();
        initEditTexts();
        changellButtons();
        initEditOnFocus();

        testeSelecionarEstado();

        Intent intent = getIntent();

        if (intent.hasExtra("ID")) {
            Salvar.setText("Alterar");
            id_cliente = intent.getIntExtra("ID", 0);

            Cliente c = db.selectCliente(id_cliente);
            Telefone t = db.selectTelefoneFirst(c);

            editTextNome.setText(c.getNome());

            if (t != null) {
                editTextTelefone.setText(t.getNum());
            } else {
                editTextTelefone.setText(c.getTelefone());
            }
        }

        testeSelecionarEstado2();
    }

    private void initButtonsCfg(){
        Salvar   = (Button) findViewById(R.id.btClienteSalvar);
        Cancelar = (Button) findViewById(R.id.btClienteCancelar);
    }

    private void initButtonsOnclick(){
        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if (id_cliente > 0 && confereCampos()) {
                    // Se o cliente já existe, e os campos estão preenchidos
                    bundle.putInt("id", id_cliente);
                    Cliente c = new Cliente();
                    Telefone t = new Telefone();

                    c.setId(id_cliente);
                    c.setNome(editTextNome.getText().toString());
                    c.setTelefone(editTextTelefone.getText().toString());

                    t = db.selectTelefoneFirst(c);

                    if (t == null) {
                        t = new Telefone();
                        t.setCliente(c);
                        t.setNum(c.getTelefone());
                        db.addTelefone(t);
                    }
                    else if (t.getId() > 0) {
                        t.setNum(c.getTelefone());
                        db.updateTelefone(t);
                    } else {
                        Log.e("ERROR onCLICK TELEFONE", "Não entrou em nenhum dos IF's");
                    }

                    db.updateCliente(c);

                    setResult(RESULT_OK);
                    finish();

                } else
                if (confereCampos()){
                    String nome = editTextNome.getText().toString();
                    String telefone = editTextTelefone.getText().toString();

                    Cliente c = new Cliente(nome, telefone);
                    Telefone t = new Telefone(telefone, c);

                    db.addCliente(c);
                    db.addTelefone(t);

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private boolean confereCampos(){
        String clienteNome      = editTextNome.getText().toString();
        String clienteTelefone  = editTextTelefone.getText().toString();

        if (clienteNome.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Favor preencher Nome", Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (clienteTelefone.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Favor preencher Telefone!", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private void initEditTexts(){
        editTextNome        = (EditText) findViewById(R.id.editNomeCliente);
        editTextTelefone    = (EditText) findViewById(R.id.editTelefoneCliente);
        editTextRua         = (EditText) findViewById(R.id.editRuaEndereco);
        editTextNum         = (EditText) findViewById(R.id.editNumEndereco);
        editTextCompl       = (EditText) findViewById(R.id.editComplEndereco);
        editTextBairro      = (EditText) findViewById(R.id.editBairroEndereco);
        textUf              = (TextView) findViewById(R.id.editUfEndereco);
        textCidade          = (TextView) findViewById(R.id.editCidadeEndereco);
    }

    private void initEditOnFocus(){
        editTextNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            //    if (editTextNome.isFocused() && )
                changellButtons();
            }
        });

        editTextTelefone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextRua.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextCompl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        textUf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        textCidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });
    }

    private void changellButtons(){
       /*
        if (    !editTextNome.isFocused()       &&
                !editTextTelefone.isFocused()   &&
                !editTextRua.isFocused()        &&
                !editTextNum.isFocused()        &&
                !editTextBairro.isFocused()     &&
                !editTextCompl.isFocused()      &&
                !editTextUf.isFocused()         &&
                !editTextCidade.isFocused())
            llButtons.setVisibility(View.VISIBLE);
        else
            llButtons.setVisibility(View.GONE);

        */
    }

    private void testeSelecionarEstado () {
        // Initialize variable
        TextView textview;
        ArrayList<String> arrayList;
        Dialog dialog;

    }

    private void testeSelecionarEstado2 () {
        // Alterar depois

        // initialize array list
        arrayList = new ArrayList<>();

        // set value in array list
        List<Estado> estados = db.listAllEstados();

        for (Estado e : estados) {
            arrayList.add(e.getNome());
        }

        textUf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog = new Dialog(AddCliente.this);
                dialog.setContentView(R.layout.spinner_estado);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                // Initialize and assign variable
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter=new ArrayAdapter<>(
                        AddCliente.this, android.R.layout.simple_list_item_1,arrayList
                );

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        textUf.setText(adapter.getItem(position));
                        estadoSelecionado = db.selectEstado(position + 1);
                        // Dismiss dialog
                        dialog.dismiss();
                        testeCidadeDropdown();
                    }
                });
            }
        });
    }

    private void testeCidadeDropdown () {

        arrayList = new ArrayList<>();
        List<Cidade> cidades = db.listAllCidadesByEstado(estadoSelecionado);

        textCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inicializando o Dialog
                dialog = new Dialog(AddCliente.this);
                dialog.setContentView(R.layout.spinner_estado);
                dialog.getWindow().setLayout(650, 800); // Alterar isso para algo melhor futuramente
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                TextView textView = dialog.findViewById(R.id.spinnerMainText);
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<Cidade> adapter = new ArrayAdapter<>(
                        AddCliente.this, android.R.layout.simple_list_item_1, cidades
                );

                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        textCidade.setText(adapter.getItem(i).getNome());
                        dialog.dismiss();
                    }
                });

            }
        });


    }
}
