package com.wlksilvestre.gerenciadordevendas;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;


    // Variáveis utilizadas no spinner do estado, ajustar depois para seguir o padrão
    Dialog dialogEstado;
    Dialog dialogCidade;
    private Estado estadoSelecionado = null;
    private Cidade cidadeSelecionada = null;
    private Boolean enderecoNovo = false;

    private ArrayList<Estado> listaDinamicaEstado;
    private ArrayList<Cidade> listaDinamicaCidade;
    private List<Cidade> cidades;
    private List<Estado> estados;
    private AdapterCidade adapterCidade;
    private AdapterEstado adapterEstado;
    private ListView listViewCidades;
    private ListView listViewEstados;

    BancoDadosCliente db = new BancoDadosCliente(this);

    int id_cliente;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Usuario usuario;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.e("INFO USER ADDCLIENTE", currentUser.getEmail());
            try {
                usuario = db.selectUsuarioByUID(currentUser.getUid());
                Log.d("INFO UID USUARIO ADDCLIENTE", usuario.getUid());
            } catch (Exception e) {
                Log.e("INFO ERROR GET USER", e.getMessage());
            }
        }

        initButtonsCfg();
        initButtonsOnclick();
        initEditTexts();
        initEditOnFocus();

        Intent intent = getIntent();

        if (intent.hasExtra("ID")) {
            Salvar.setText("Alterar");
            id_cliente = intent.getIntExtra("ID", 0);

            Cliente c = db.selectCliente(id_cliente);
            Telefone t = db.selectTelefoneFirst(c);
            Endereco e = db.selectEnderecoByCliente(c);

            editTextNome.setText(c.getNome());

            if (t != null) {
                editTextTelefone.setText(t.getNum());
            } else {

            }

            if (e != null && e.getCidade() != null) {
                editTextRua.setText(e.getRua());
                editTextNum.setText(e.getNum());
                editTextCompl.setText(e.getComp());
                editTextBairro.setText(e.getBairro());

                estadoSelecionado = e.getCidade().getEstado();
                cidadeSelecionada = e.getCidade();

                textUf.setText(estadoSelecionado.getNome());
                textCidade.setText(cidadeSelecionada.getNome());
            }
        }

        estadoDropdown();
    }

    private void initButtonsCfg(){
        Salvar   = findViewById(R.id.btClienteSalvar);
        Cancelar = findViewById(R.id.btClienteCancelar);
    }

    private void initButtonsOnclick(){
        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                // Se o cliente já existe, e os campos estão preenchidos
                if (id_cliente > 0 && confereCampos() && confereCamposEndereco()) {
                    setContentView(R.layout.activity_add_cliente);

                    bundle.putInt("id", id_cliente);
                    Cliente c = db.selectCliente(id_cliente);
                    Telefone t = db.selectTelefoneFirst(c);
                    Endereco e = db.selectEnderecoByCliente(c);

                    c.setId(id_cliente);
                    c.setNome(editTextNome.getText().toString());
                    c.setUid(usuario.getUid());
                    String telefoneText = editTextTelefone.getText().toString();

                    // Em algum momento, verificar se este if está filtrando, e fazer o possível pra simplificar ele
                    if (t == null) {
                        t = new Telefone();
                        t.setCliente(c);
                        t.setNum(telefoneText);
                        db.addTelefone(t);
                        Log.e("INFO ENTROU NO IF DO TELEFONE NULO", "ADICIONOU TELEFONE");
                    }
                    else if (t.getId() > 0) {
                        t.setNum(telefoneText);
                        db.updateTelefone(t);
                        Log.e("INFO ENTROU NO IF DO ID DO TELEFONE MAIOR QUE ZERO", "ATUALIZOU TELEFONE");
                    } else {
                        Log.e("ERROR onCLICK TELEFONE", "Não entrou em nenhum dos IF's");
                    }

                    db.updateCliente(c);

                    if (cidadeSelecionada != null) {
                        if (e == null) {
                            e = new Endereco();
                            enderecoNovo = true;
                        }

                        String rua = editTextRua.getText().toString();
                        String num = editTextNum.getText().toString();
                        String compl = editTextCompl.getText().toString();
                        String ref = null;
                        String bairro = editTextBairro.getText().toString();
                        Cidade cidade = cidadeSelecionada;

                        e.setRua(rua);
                        e.setNum(num);
                        e.setComp(compl);
                        e.setRef(null);
                        e.setBairro(bairro);
                        e.setCidade(cidade);
                        e.setCliente(c);

                        if (enderecoNovo) {
                            db.addEndereco(e);
                        } else {
                            db.updateEndereco(e);
                        }
                    }

                    setResult(RESULT_OK);
                    finish();

                } else
                if (confereCampos() && confereCamposEndereco()){
                    String nome = editTextNome.getText().toString();
                    String telefone = editTextTelefone.getText().toString();

                    Cliente c = new Cliente(nome, usuario.getUid(), DateCustomText.getActualDateTime());
                    db.addCliente(c);
                    saveClienteData(c);
                    c = db.selectMaxCliente(usuario.getUid());

                    Telefone t = new Telefone(telefone, c);
                    db.addTelefone(t);
                    saveTelefoneData(t);

                    if (cidadeSelecionada != null) {
                        Endereco e = new Endereco();
                        String rua = editTextRua.getText().toString();
                        String num = editTextNum.getText().toString();
                        String compl = editTextCompl.getText().toString();
                        String ref = null;
                        String bairro = editTextBairro.getText().toString();
                        Cidade cidade = cidadeSelecionada;

                        e.setRua(rua);
                        e.setNum(num);
                        e.setComp(compl);
                        e.setRef(null);
                        e.setBairro(bairro);
                        e.setCidade(cidade);
                        e.setCliente(c);

                        db.addEndereco(e);
                        saveEnderecoData(e);
                    }

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

    private boolean confereCampos () {
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
        if (clienteTelefone.length() < 12 || clienteTelefone.length() > 13 ) {
            Toast.makeText(getApplicationContext(), "Número de telefone inválido!", Toast.LENGTH_SHORT).show();
            return false;
        }
            return true;
    }

    private boolean confereCamposEndereco() {
        String clienteRua = editTextRua.getText().toString();
        String clienteNum = editTextNum.getText().toString();
        String clienteBairro = editTextBairro.getText().toString();
        Estado clienteEstado = estadoSelecionado;
        Cidade clienteCidade = cidadeSelecionada;

        if (clienteRua.isEmpty() && clienteNum.isEmpty() && clienteBairro.isEmpty() && clienteEstado == null && clienteCidade == null) {
            Toast.makeText(getApplicationContext(), "Regularize o endereço do cliente quando possível", Toast.LENGTH_SHORT).show();
            return true;
        } else
        if (clienteRua.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insira a rua ao endereço", Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (clienteNum.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insira o número ao endereço", Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (clienteBairro.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insira o bairro ao endereço", Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (clienteEstado == null) {
            Toast.makeText(getApplicationContext(), "Insira o estado ao endereço", Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (clienteCidade == null) {
            Toast.makeText(getApplicationContext(), "Insira a cidade ao endereço", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void initEditTexts(){
        editTextNome        = findViewById(R.id.editNomeCliente);
        editTextTelefone    = findViewById(R.id.editTelefoneCliente);
        editTextRua         = findViewById(R.id.editRuaEndereco);
        editTextNum         = findViewById(R.id.editNumEndereco);
        editTextCompl       = findViewById(R.id.editComplEndereco);
        editTextBairro      = findViewById(R.id.editBairroEndereco);
        textUf              = findViewById(R.id.editUfEndereco);
        textCidade          = findViewById(R.id.editCidadeEndereco);

        editTextTelefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void initEditOnFocus(){
        editTextNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //    if (editTextNome.isFocused() && )
            }
        });

        editTextTelefone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        editTextRua.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        editTextNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            }
        });

        editTextCompl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            }
        });

        editTextBairro.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    textUf.performClick();
                }
                return false;
            }
        });

        textUf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ;
            }
        });

        textCidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });
    }



    private void estadoDropdown () {

        textUf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                estados = db.listAllEstadosOrdered();
                listaDinamicaEstado = new ArrayList<>();

                if (!estados.isEmpty()) {
                    listaDinamicaEstado.addAll(estados);
                }

                dialogEstado = new Dialog(AddCliente.this);
                dialogEstado.setContentView(R.layout.spinner_estado);

                adapterEstado = new AdapterEstado(dialogEstado.getContext(), 0, listaDinamicaEstado);

                dialogEstado.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75)); // Alterar isso para algo melhor futuramente
                dialogEstado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogEstado.show();

                EditText editEstado = dialogEstado.findViewById(R.id.editTextSpinnerEstado);

                listViewEstados = dialogEstado.findViewById(R.id.lvSpinnerEstado);
                listViewEstados.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewEstados.setAdapter(adapterEstado);

                editEstado.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        atualizaListaEstados(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listViewEstados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            Estado e = (Estado) listViewEstados.getItemAtPosition(i);
                            textUf.setText(e.getNome());
                            textCidade.setText(null);
                            if (estadoSelecionado != e) {
                                estadoSelecionado = (e);
                                cidadeSelecionada = null;
                            }

                            dialogEstado.dismiss();
                            cidadeDropdown();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void cidadeDropdown() {

        textCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cidades = db.listAllCidadesByEstado(estadoSelecionado);
                listaDinamicaCidade = new ArrayList<>();

                if (!cidades.isEmpty()) {
                    listaDinamicaCidade.addAll(cidades);
                }

                dialogCidade = new Dialog(AddCliente.this);
                dialogCidade.setContentView(R.layout.spinner_cidade);

                adapterCidade = new AdapterCidade(dialogCidade.getContext(), 0, listaDinamicaCidade);

                dialogCidade.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75)); // Alterar isso para algo melhor futuramente
                dialogCidade.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogCidade.show();

                EditText editText = dialogCidade.findViewById(R.id.editTextSpinnerCidade);

                listViewCidades = dialogCidade.findViewById(R.id.lvSpinnerCidade);
                listViewCidades.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewCidades.setAdapter(adapterCidade);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        atualizaListaCidades(charSequence);

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
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void atualizaListaCidades (CharSequence nome) {
        cidades = db.listAllCidadesByEstadoAndNome(estadoSelecionado, nome.toString());
        listaDinamicaCidade = new ArrayList<>();
        listaDinamicaCidade.addAll(cidades);

        adapterCidade = new AdapterCidade(dialogCidade.getContext(), 0, listaDinamicaCidade);
        listViewCidades.setAdapter(adapterCidade);
        adapterCidade.notifyDataSetChanged();
    }

    private void atualizaListaEstados (CharSequence nome) {
        estados = db.listAllEstadosByName(nome.toString());
        listaDinamicaEstado = new ArrayList<>();
        listaDinamicaEstado.addAll(estados);

        adapterEstado = new AdapterEstado(dialogEstado.getContext(), 0, listaDinamicaEstado);
        listViewEstados.setAdapter(adapterEstado);
        adapterEstado.notifyDataSetChanged();
    }

    private void saveClienteData(@NonNull Cliente cliente) {
        FirebaseFirestore fireDB = FirebaseFirestore.getInstance();

        Map<String, Object> values = new HashMap<>();
        values.put("id", cliente.getId());
        values.put("nome", cliente.getNome());
        values.put("usuario_uid", cliente.getUid());
        values.put("data_update", cliente.getDataUpdate());

        DocumentReference dr = fireDB.collection("clientes").document(String.valueOf(cliente.getId()));
        dr.set(values)
                .addOnSuccessListener(unused -> Log.d("INFO FIREDB SUCCESS", "Sucesso ao salvar dados"))
                .addOnFailureListener(e -> Log.d("INFO FIREDB FAILURE", "Erro ao salvar dados \n" + e));
    }

    private void saveTelefoneData(@NonNull Telefone telefone) {
        FirebaseFirestore fireDB = FirebaseFirestore.getInstance();

        Map<String, Object> values = new HashMap<>();
        values.put("id", telefone.getId());
        values.put("numero", telefone.getNum());
        values.put("cliente_id", telefone.getCliente().getId());

        DocumentReference dr = fireDB.collection("telefones").document(String.valueOf(telefone.getId()));
        dr.set(values)
                .addOnSuccessListener(unused -> Log.d("INFO FIREDB SUCCESS", "Sucesso ao salvar dados"))
                .addOnFailureListener(e -> Log.d("INFO FIREDB FAILURE", "Erro ao salvar dados \n" + e));
    }

    private void saveEnderecoData(@NonNull Endereco endereco) {
        FirebaseFirestore fireDB = FirebaseFirestore.getInstance();

        Map<String, Object> values = new HashMap<>();
        values.put("id", endereco.getId());
        values.put("rua", endereco.getRua());
        values.put("num", endereco.getNum());
        values.put("compl", endereco.getComp());
        values.put("bairro", endereco.getBairro());
        values.put("cidade", endereco.getCidade().getId());
        values.put("cliente_id", endereco.getCliente().getId());

        DocumentReference dr = fireDB.collection("enderecos").document(String.valueOf(usuario.getUid()));
        dr.set(values)
                .addOnSuccessListener(unused -> Log.d("INFO FIREDB SUCCESS", "Sucesso ao salvar dados"))
                .addOnFailureListener(e -> Log.d("INFO FIREDB FAILURE", "Erro ao salvar dados \n" + e));
    }
}
