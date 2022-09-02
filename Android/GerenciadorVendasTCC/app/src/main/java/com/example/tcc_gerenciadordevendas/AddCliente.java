package com.example.tcc_gerenciadordevendas;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddCliente extends AppCompatActivity {

    private Button Salvar;
    private Button Cancelar;

    private EditText editTextNome;
    private EditText editTextTelefone;
    private EditText editTextRua;
    private EditText editTextNum;
    private EditText editTextCompl;
    private EditText editTextBairro;
    private EditText editTextUf;
    private EditText editTextCidade;

    // Variáveis utilizadas no spinner do estado, ajustar depois para seguir o padrão
    private TextView textView;
    private ArrayList<String> arrayList;
    Dialog dialog;


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
        editTextUf          = (EditText) findViewById(R.id.editUfEndereco);
        editTextCidade      = (EditText) findViewById(R.id.editCidadeEndereco);
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

        editTextUf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changellButtons();
            }
        });

        editTextCidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // assign variable
            textview=findViewById(R.id.testView);

            // initialize array list
            arrayList=new ArrayList<>();

            // set value in array list
            arrayList.add("DSA Self Paced");
            arrayList.add("Complete Interview Prep");
            arrayList.add("Amazon SDE Test Series");
            arrayList.add("Compiler Design");
            arrayList.add("Git & Github");
            arrayList.add("Python foundation");
            arrayList.add("Operating systems");
            arrayList.add("Theory of Computation");

            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Initialize dialog
                    dialog=new Dialog(MainActivity.this);

                    // set custom dialog
                    dialog.setContentView(R.layout.dialog_searchable_spinner);

                    // set custom height and width
                    dialog.getWindow().setLayout(650,800);

                    // set transparent background
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    // show dialog
                    dialog.show();

                    // Initialize and assign variable
                    EditText editText=dialog.findViewById(R.id.edit_text);
                    ListView listView=dialog.findViewById(R.id.list_view);

                    // Initialize array adapter
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);

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
                            textview.setText(adapter.getItem(position));

                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }
}
