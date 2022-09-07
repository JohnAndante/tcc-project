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
import android.view.ViewGroup;
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
    private ArrayList<String> arrayListEstado;
    private ArrayList<String> arrayListCidade;
    Dialog dialogEstado;
    Dialog dialogCidade;
    private Estado estadoSelecionado = null;
    private Cidade cidadeSelecionada = null;

    private ArrayList<Cidade> listaDinamicaCidade;
    private AdapterCidade adapterCidade;
    private ListView listViewCidades = null;

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

        testeEstadoDropdown();
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


    private void testeEstadoDropdown () {
        // Alterar depois

        // initialize array list
        arrayListEstado = new ArrayList<>();

        // set value in array list
        List<Estado> estados = db.listAllEstados();

        for (Estado e : estados) {
            arrayListEstado.add(e.getNome());
        }

        textUf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                dialogEstado = new Dialog(AddCliente.this);
                dialogEstado.setContentView(R.layout.spinner_estado);
                dialogEstado.getWindow().setLayout(650,800);
                dialogEstado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogEstado.show();

                // Initialize and assign variable
                EditText editText = dialogEstado.findViewById(R.id.editTextSpinnerEstado);
                ListView listView = dialogEstado.findViewById(R.id.lvSpinnerEstado);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        AddCliente.this, android.R.layout.simple_list_item_1, arrayListEstado
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
                        textCidade.setText(null);
                        estadoSelecionado = db.selectEstado(position + 1);
                        // Dismiss dialog
                        dialogEstado.dismiss();
                        testeCidadeDropdown2();
                    }
                });
            }
        });
    }

    private void testeCidadeDropdown () {

        arrayListCidade = new ArrayList<>();
        List<Cidade> cidades = db.listAllCidadesByEstado(estadoSelecionado);

        textCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inicializando o Dialog
                dialogCidade = new Dialog(AddCliente.this);
                dialogCidade.setContentView(R.layout.spinner_cidade);
                dialogCidade.getWindow().setLayout(650, 800); // Alterar isso para algo melhor futuramente
                dialogCidade.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogCidade.show();

                TextView textView = dialogCidade.findViewById(R.id.tvSpinnerCidade);
                EditText editText = dialogCidade.findViewById(R.id.editTextSpinnerCidade);
                ListView listView = dialogCidade.findViewById(R.id.lvSpinnerCidade);

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
                        cidadeSelecionada = (adapter.getItem(i));
                        dialogCidade.dismiss();
                    }
                });

            }
        });


    }

    private void testeCidadeDropdown2 () {

        textCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Cidade> cidades = db.listAllCidadesByEstado(estadoSelecionado);
                listaDinamicaCidade = new ArrayList<Cidade>();

                if (!cidades.isEmpty()) {
                    for (Cidade c : cidades)
                        listaDinamicaCidade.add(c);
                }

                dialogCidade = new Dialog(AddCliente.this);
                dialogCidade.setContentView(R.layout.spinner_cidade);

                adapterCidade = new AdapterCidade(dialogCidade.getContext(), 0, listaDinamicaCidade);

                dialogCidade.getWindow().setLayout(650, 800); // Alterar isso para algo melhor futuramente
                dialogCidade.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogCidade.show();

                TextView textView = dialogCidade.findViewById(R.id.tvSpinnerCidade);
                EditText editText = dialogCidade.findViewById(R.id.editTextSpinnerCidade);

                listViewCidades = (ListView) dialogCidade.findViewById(R.id.lvSpinnerCidade);
                listViewCidades.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewCidades.setAdapter(adapterCidade);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapterCidade.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listViewCidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            Cidade c = (Cidade) listViewCidades.getItemAtPosition(i);
                            textCidade.setText(c.getNome());
                            cidadeSelecionada = (c);
                            dialogCidade.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            Log.e("ERROR", e.getMessage().toString());
                        }
                    }
                });
            }
        });

    }
}
