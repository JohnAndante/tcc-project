package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.List;

public class AddProduto extends AppCompatActivity {

    private LinearLayout llButtons;
    private Button Salvar;
    private Button Cancelar;

    private EditText editTextDescricao;
    private EditText editTextValor;
    private TextView textMarca;
    private TextView textLinha;
    private TextView textCategoria;

    public final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    private Boolean buttonsVisibility = true;
    private Boolean isEditTextFocused = false;

    BancoDadosCliente db = new BancoDadosCliente(this);

    int id_produto;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        llButtons = (LinearLayout) findViewById(R.id.llBotoesProduto);

        initButtonsCfg();
        initButtonsOnClick();
        initEditTexts();
        changellButtons();
        initEditOnFocus();

        Intent intent = getIntent();

        if (intent.hasExtra("ID")) {
            Salvar.setText("Alterar");
            id_produto = intent.getIntExtra("ID", 0);

            Produto p = db.selectProduto(id_produto);
            Linha l = db.selectLinha(p.getLinha().getId());
            Marca m = db.selectMarca(l.getMarca().getId());
            List<ProdSubcat> ps = db.listProdSubcatsByProduto(p);
            Categoria c = db.selectFirstProdSubcatByProd(p).getSubcat().getCategoria();

            editTextDescricao.setText(p.getDescricao());
            editTextValor.setText("R$ " + df.format(p.getValor()));
            textMarca.setText(m.getDescricao());
            textLinha.setText(l.getDescricao());
            textCategoria.setText(c.getDescricao());

        }

        // Colocar aqui os dropdowns de marca, linha e categoria
    }

    private void initButtonsCfg () {
        Salvar      = findViewById(R.id.btProdutoSalvar);
        Cancelar    = findViewById(R.id.btProdutoCancelar);
    }

    private void initButtonsOnClick () {
        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                // Se o produto já existe, e os campos estão corretamente preenchidos
                if (id_produto > 0 && confereCampos()) {
                    setContentView(R.layout.activity_add_produto);

                    bundle.putInt("id", id_produto);

                    // processo para sobrescrever os dados já gravados anteriormente
                    // e então fazer o update do produto e tabelas relacionadas
                    // se necessário

                    // estudar como será possível trabalhar com as subcategorias
                } else
                if (confereCampos()) {

                    // processo para gravar as informações na classe variável e
                    // gravar essas informações no banco de dados

                    // estudar como será possível trabalhar com as subcategorias
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
        // conferir se os campos estão preenchidos e/ou selecionados

        /*
        String clienteRua = editTextRua.getText().toString();
        String clienteNum = editTextNum.getText().toString();
        String clienteBairro = editTextBairro.getText().toString();
        Estado clienteEstado = estadoSelecionado;
        Cidade clienteCidade = cidadeSelecionada;

        if (clienteRua.isEmpty() && clienteNum.isEmpty() && clienteBairro.isEmpty() && clienteEstado == null && clienteCidade == null) {
            Toast.makeText(getApplicationContext(), "Regularize o endereço do cliente quando possível", Toast.LENGTH_SHORT).show();
            return true;
        } else
        if (clienteEstado == null) {
            Toast.makeText(getApplicationContext(), "Insira o estado ao endereço", Toast.LENGTH_SHORT).show();
            return false;
        }
         */

        return false;
    }

    private void initEditTexts () {
        /*
        editTextNome        = (EditText) findViewById(R.id.editNomeCliente);
        editTextTelefone    = (EditText) findViewById(R.id.editTelefoneCliente);
        editTextRua         = (EditText) findViewById(R.id.editRuaEndereco);
        editTextNum         = (EditText) findViewById(R.id.editNumEndereco);
         */
    }

    private void initEditOnFocus () {
        /*
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
         */
    }

    private void changellButtons () {

    }

    private void marcaDropdown () {

    }

    private void linhaDropdown () {

    }

    private void categoriaDropdown () {

    }

    private void atualizaListaMarcas (CharSequence _desc) {

    }

    private void atualizaListaLinhas (CharSequence _desc) {

    }

    private void atualizaListaCategorias (CharSequence _desc) {

    }
}
