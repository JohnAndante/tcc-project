package com.example.tcc_gerenciadordevendas;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

    // Variáveis para os dropdowns
    private List<Marca> marcas;
    private ArrayList<Marca> listaDinamicaMarca;
    private AdapterMarca adapterMarca;
    private Dialog dialogMarca;
    private ListView listViewMarcas;

    private List<Linha> linhas;
    private ArrayList<Linha> listaDinamicaLinha;
    private AdapterLinha adapterLinha;
    private Dialog dialogLinha;
    private ListView listViewLinhas;

    private List<Categoria> categorias;
    private ArrayList<Categoria> listaDinamicaCategoria;
    private AdapterCategoria adapterCategoria;
    private Dialog dialogCategoria;
    private ListView listViewCategorias;

    private Marca marcaSelecionada = null;
    private Linha linhaSelecionada = null;
    private Categoria categoriaSelecionada = null;


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

            linhaSelecionada = l;
            marcaSelecionada = m;
            categoriaSelecionada = c;
        }

        // Colocar aqui os dropdowns de marca, linha e categoria
        marcaDropdown();
        categoriaDropdown();
    }

    private void initButtonsCfg () {
        Salvar      = findViewById(R.id.btAddProdutoSalvar);
        Cancelar    = findViewById(R.id.btAddProdutoVoltar);
    }

    private void initButtonsOnClick () {
        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                // Se o produto já existe, e os campos estão corretamente preenchidos
                if (id_produto > 0 && confereCampos()) {
                    setContentView(R.layout.activity_add_produto);

                    // processo para sobrescrever os dados já gravados anteriormente
                    // e então fazer o update do produto e tabelas relacionadas
                    // se necessário

                    // estudar como será possível trabalhar com as subcategorias

                    String desc = editTextDescricao.getText().toString();

                    // necessário tratar o texto exibido no editText
                    Double valor = Double.valueOf(editTextValor.getText().toString());

                    bundle.putInt("id", id_produto);
                    Produto p = db.selectProduto(id_produto);
                    Linha l = db.selectLinha(p.getLinha().getId());
                    Marca m = db.selectMarca(l.getMarca().getId());
                    ProdSubcat psc = db.selectFirstProdSubcatByProd(p);
                    Subcat sc = db.selectSubcat(psc.getSubcat().getId());
                    Categoria c = db.selectCategoria(sc.getCategoria().getId());

                    p.setDescricao(desc);
                    p.setValor(valor);

                    m = marcaSelecionada;
                    l = linhaSelecionada;

                    p.setLinha(l);

                    c = categoriaSelecionada;

                    sc = new Subcat("Subcategoria Teste Edit", c);
                    psc = new ProdSubcat(p, sc);

                    db.updateProduto(p);

                    setResult(RESULT_OK);
                    finish();

                } else
                if (confereCampos()) {
                    // processo para gravar as informações na classe variável e
                    // gravar essas informações no banco de dados

                    // estudar como será possível trabalhar com as subcategorias

                    setContentView(R.layout.activity_add_produto);
                    String desc = editTextDescricao.getText().toString();

                    // necessário tratar o texto exibido no editText
                    Double valor = Double.valueOf(editTextValor.getText().toString());

                    Linha l = linhaSelecionada;
                    Marca m = db.selectMarca(l.getMarca().getId());
                    Categoria c = categoriaSelecionada;

                    Produto p = new Produto(desc, valor, l);
                    db.addProduto(p);

                    p = db.selectMaxProduto();

                    Subcat sc = new Subcat("Subcategoria Teste", c);
                    db.addSubcat(sc);
                    sc = db.selectMaxSubcat();

                    ProdSubcat psc = new ProdSubcat(p, sc);
                    db.addProdSubcat(psc);

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
        // conferir se os campos estão preenchidos e/ou selecionados

        String descProd = editTextDescricao.getText().toString();
        String valorBruto = editTextValor.getText().toString();
        Double valorProd = 0.00;

        valorBruto.replaceAll("[R]", "").replaceAll("[$]", "").replaceAll("[ ]", "");
        valorProd = Double.valueOf(valorBruto);

        if (descProd.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha a descrição do produto", Toast.LENGTH_SHORT).show();
            editTextDescricao.requestFocus();
            return false;
        } else
        if (valorProd.isNaN() || valorProd == 0.00) {
            Toast.makeText(getApplicationContext(), "Insira um valor válido para o produto", Toast.LENGTH_SHORT).show();
            editTextValor.requestFocus();
            return false;
        } else
        if (marcaSelecionada == null) {
            Toast.makeText(getApplicationContext(), "Selecione uma marca para o produto", Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (linhaSelecionada == null) {
            Toast.makeText(getApplicationContext(), "Selecione uma linha para o produto", Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (categoriaSelecionada == null) {
            Toast.makeText(getApplicationContext(), "Selecione uma categoria para o produto", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void initEditTexts () {

        editTextDescricao   = (EditText) findViewById(R.id.editDescricaoProduto);
        editTextValor       = (EditText) findViewById(R.id.editValorProduto);
        textMarca           = (TextView) findViewById(R.id.tvMarcaProduto);
        textLinha           = (TextView) findViewById(R.id.tvLinhaProduto);
        textCategoria       = (TextView) findViewById(R.id.tvCategoriaProduto);

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

        textMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcas = db.listMarcasOrdered();
                listaDinamicaMarca = new ArrayList<Marca>();

                if (!marcas.isEmpty()) {
                    for (Marca m : marcas)
                        listaDinamicaMarca.add(m);
                }

                dialogMarca = new Dialog(AddProduto.this);
                dialogMarca.setContentView(R.layout.spinner_marca);

                adapterMarca = new AdapterMarca(dialogMarca.getContext(), 0, listaDinamicaMarca);

                dialogMarca.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
                dialogMarca.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogMarca.show();

                TextView tvMarca = dialogMarca.findViewById(R.id.tvSpinnerMarca);
                EditText editMarca = dialogMarca.findViewById(R.id.editTextSpinnerMarca);

                listViewMarcas = (ListView) dialogMarca.findViewById(R.id.lvSpinnerMarca);
                listViewMarcas.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewMarcas.setAdapter(adapterMarca);

                editMarca.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        atualizaListaMarcas(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listViewMarcas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            Marca m = (Marca) listViewMarcas.getItemAtPosition(i);
                            textMarca.setText(m.getDescricao());
                            if (marcaSelecionada != m) {
                                marcaSelecionada = m;
                                linhaSelecionada = null;
                            }

                            dialogMarca.dismiss();
                            linhaDropdown();
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage().toString());
                        }
                    }
                });
            }
        });
    }

    private void linhaDropdown () {

        textLinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linhas = db.listLinhasByMarcaOrdered(marcaSelecionada);
                listaDinamicaLinha = new ArrayList<Linha>();

                if (!linhas.isEmpty()) {
                    for (Linha l : linhas)
                        listaDinamicaLinha.add(l);
                }

                dialogLinha = new Dialog(AddProduto.this);
                dialogLinha.setContentView(R.layout.spinner_linha);

                adapterLinha = new AdapterLinha(dialogLinha.getContext(), 0, listaDinamicaLinha);

                dialogLinha.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
                dialogLinha.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogLinha.show();

                TextView tvLinha = dialogLinha.findViewById(R.id.tvSpinnerLinha);
                EditText editLinha = dialogLinha.findViewById(R.id.editTextSpinnerLinha);

                listViewLinhas = (ListView) dialogLinha.findViewById(R.id.lvSpinnerLinha);
                listViewLinhas.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewLinhas.setAdapter(adapterLinha);

                editLinha.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        atualizaListaLinhas(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listViewLinhas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long lo) {
                        try {
                            Linha l = (Linha) listViewLinhas.getItemAtPosition(i);
                            textLinha.setText(l.getDescricao());
                            if (linhaSelecionada != l) {
                                linhaSelecionada = l;
                            }

                            dialogLinha.dismiss();
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage().toString());
                        }
                    }
                });
            }
        });
    }

    private void categoriaDropdown () {

        textCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorias = db.listCategoriasOrdered();
                listaDinamicaCategoria = new ArrayList<Categoria>();

                if (!categorias.isEmpty()) {
                    for (Categoria c : categorias)
                        listaDinamicaCategoria.add(c);
                }

                dialogCategoria = new Dialog(AddProduto.this);
                dialogCategoria.setContentView(R.layout.spinner_categoria);

                adapterCategoria = new AdapterCategoria(dialogCategoria.getContext(), 0, listaDinamicaCategoria);

                dialogCategoria.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
                dialogCategoria.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogCategoria.show();

                TextView tvCategoria = dialogCategoria.findViewById(R.id.tvSpinnerCategoria);
                EditText editCategoria = dialogCategoria.findViewById(R.id.editTextSpinnerCategoria);

                listViewCategorias = (ListView) dialogCategoria.findViewById(R.id.lvSpinnerCategoria);
                listViewCategorias.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewCategorias.setAdapter(adapterCategoria);

                editCategoria.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        atualizaListaCategorias(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listViewCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            Categoria c = (Categoria) listViewCategorias.getItemAtPosition(i);
                            textCategoria.setText(c.getDescricao());
                            if (categoriaSelecionada != c) {
                                categoriaSelecionada = c;
                                categoriaSelecionada = null;
                            }

                            dialogCategoria.dismiss();
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage().toString());
                        }
                    }
                });
            }
        });

    }

    private void atualizaListaMarcas (CharSequence _desc) {
        marcas = db.listMarcasByDesc(_desc.toString());
        listaDinamicaMarca = new ArrayList<Marca>();
        for (Marca m : marcas)
            listaDinamicaMarca.add(m);

        adapterMarca = new AdapterMarca(dialogMarca.getContext(), 0, listaDinamicaMarca);
        listViewMarcas.setAdapter(adapterMarca);
        adapterMarca.notifyDataSetChanged();
    }

    private void atualizaListaLinhas (CharSequence _desc) {
        linhas = db.listLinhasByMarcaDesc(marcaSelecionada, _desc.toString());
        listaDinamicaLinha = new ArrayList<Linha>();
        for (Linha l : linhas)
            listaDinamicaLinha.add(l);

        adapterLinha = new AdapterLinha(dialogLinha.getContext(), 0, listaDinamicaLinha);
        listViewLinhas.setAdapter(adapterLinha);
        adapterLinha.notifyDataSetChanged();
    }

    private void atualizaListaCategorias (CharSequence _desc) {
        categorias = db.listCategoriasByDesc(_desc.toString());
        listaDinamicaCategoria = new ArrayList<Categoria>();
        for (Categoria c : categorias)
            listaDinamicaCategoria.add(c);

        adapterCategoria = new AdapterCategoria(dialogCategoria.getContext(), 0, listaDinamicaCategoria);
        listViewCategorias.setAdapter(adapterCategoria);
        adapterCategoria.notifyDataSetChanged();
    }

    private void testeInsereSubcats () {

    }
}
