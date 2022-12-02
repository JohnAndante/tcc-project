package com.wlksilvestre.gerenciadordevendas;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddProduto extends AppCompatActivity {

    private LinearLayout llButtons;
    private Button Salvar;
    private Button Cancelar;

    private EditText editTextDescricao;
    private EditText editTextValor;
    private TextView textMarca;
    private TextView textLinha;
    private TextView textCategoria;
    private TextView textCifra;

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

    private int posicao;
    private boolean hasPosicao = false;

    private ChipGroup chipGroupSubcat;
    private Chip chipAddSubcat;
    private Button buttonTeste;
    private int contador = 0;

    private List<Subcat> subcats;
    private ArrayList<Subcat> listaDinamicaSubcat;
    private AdapterSubcat adapterSubcat;
    private Dialog dialogSubcat;
    private ListView listViewSubcats;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);
        Objects.requireNonNull(getSupportActionBar()).hide();

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

            if (intent.hasExtra("posicao")) {
                posicao = intent.getIntExtra("posicao", 0);
                hasPosicao = true;
            }

            Produto p = db.selectProduto(id_produto);
            Linha l = db.selectLinha(p.getLinha().getId());
            Marca m = db.selectMarca(l.getMarca().getId());
            List<ProdSubcat> prodSubcats = db.listProdSubcatsByProduto(p);
            listaDinamicaSubcat = new ArrayList<>();

            if (!prodSubcats.isEmpty()) {
                Categoria c = db.selectFirstProdSubcatByProd(p).getSubcat().getCategoria();
                textCategoria.setText(c.getDescricao());
                categoriaSelecionada = c;
                for (ProdSubcat ps : prodSubcats) {
                    listaDinamicaSubcat.add(ps.getSubcat());
                }
            }

            editTextDescricao.setText(p.getDescricao());
            editTextValor.setText(MaskEditUtil.doubleToMoneyValue(p.getValor()));
            textMarca.setText(m.getDescricao());
            textLinha.setText(l.getDescricao());

            linhaSelecionada = l;
            marcaSelecionada = m;

            if (!listaDinamicaSubcat.isEmpty()) {
                for (Subcat s : listaDinamicaSubcat) {
                    addChipSubcat(s.getDescricao(), s.getId());
                }
            }

            testeInsereSubcats();
        }

        // Colocar aqui os dropdowns de marca, linha e categoria
        marcaDropdown();
        categoriaDropdown();

        editTextValor.addTextChangedListener(new MoneyTextWatcher(editTextValor));
    }

    private void initButtonsCfg () {
        Salvar      = findViewById(R.id.btAddProdutoSalvar);
        Cancelar    = findViewById(R.id.btViewVoltar);
    }

    @SuppressLint("ResourceType")
    private void initButtonsOnClick () {
        Salvar.setOnClickListener(view -> {
            Bundle bundle = new Bundle();

            // Se o produto já existe, e os campos estão corretamente preenchidos
            if (id_produto > 0 && confereCampos()) {
                setContentView(R.layout.activity_add_produto);

                // estudar como será possível trabalhar com as subcategorias
                // analisar o que fazer quando trocar a categoria de um produto tendo
                // subcategorias e prodsubcats cadastradas ao mesmo.

                String desc = editTextDescricao.getText().toString();

                Double valor = MaskEditUtil.moneyToDoubleTest(editTextValor.getText().toString());

                bundle.putInt("ID", id_produto);
                if (hasPosicao == true)
                    bundle.putInt("posicao", posicao);
                Produto p = db.selectProduto(id_produto);
                Linha l = db.selectLinha(p.getLinha().getId());
                Marca m = db.selectMarca(l.getMarca().getId());
                ProdSubcat psc = db.selectFirstProdSubcatByProd(p);
                Categoria c = new Categoria();

                if (psc != null) {
                    Subcat sc = db.selectSubcat(psc.getSubcat().getId());
                    c = db.selectCategoria(sc.getCategoria().getId());
                }

                p.setDescricao(desc);
                p.setValor(valor);

                m = marcaSelecionada;
                l = linhaSelecionada;
                c = categoriaSelecionada;

                p.setLinha(l);

                db.updateProduto(p);


                // Método antigo
                /*
                // Aqui segue o algoritmo pra trabalhar com as subcategorias existentes
                // Primero geramos a lista do prodsubcat atual
                List<ProdSubcat> prodSubcatsOld = db.listProdSubcatsByProduto(p);
                ArrayList<ProdSubcat> prodSubcatsNew = new ArrayList<ProdSubcat>();

                // agora iremos recuperar os subcats do chipgroup, verificar se a
                // subcategoria existe, (adicionando ao banco de dados se nn existe )
                // e criar uma lista delas
                // por enquanto as listas serão em prodsubcat, pra facilitar no final
                for (int i = 0; i < chipGroupSubcat.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroupSubcat.getChildAt(i);
                    if (chip.isChecked()) {
                        if (chip.getId() < 0) {
                            Subcat scNew = new Subcat(chip.getText().toString(), categoriaSelecionada);
                            db.addSubcat(scNew);
                            scNew = db.selectMaxSubcat();
                        } else {
                            Subcat scNew = new Subcat(chip.getId(), chip.getText().toString(), categoriaSelecionada);
                            prodSubcatsNew.add(new ProdSubcat(p, scNew));
                        }
                    }
                }

                // -- lista das subcategorias selecionadas criadas
                // identificando as subcat repetidas nas listas, e deletando das duas listas
                // já as que foram removidas pelo cliente, serão removidas da lista antigas, e
                // deletadas do banco de dados
                for (ProdSubcat pscOld : prodSubcatsOld) {
                    if (prodSubcatsNew.contains(pscOld)) {
                        prodSubcatsOld.remove(pscOld);
                        prodSubcatsNew.remove(pscOld);
                    } else {
                        prodSubcatsOld.remove(pscOld);
                        db.deleteProdSubcat(pscOld);
                    }
                }

                // O que sobrou na lista nova são apenas subcategorias novas, então para
                // cada subcategoria na lista de novos, adicionaremos ela ao banco
                for (ProdSubcat pscNew : prodSubcatsNew) {
                    db.addProdSubcat(pscNew);
                }

                */

                // Método novo
                List<ProdSubcat> prodSubcatsOld = db.listProdSubcatsByProduto(p);
                for (ProdSubcat ps : prodSubcatsOld) {
                    if (db.deleteAllProdSubcat(ps))
                        Log.e("DELETADO PRODSUBCAT", ps.getSubcat().getDescricao());
                    else
                        Log.e("Não DELETADO PRODSUBCAT", ps.getSubcat().getDescricao());
                }

                List<ProdSubcat> prodSubcatNew = new ArrayList<>();
                for (int i = 0; i < chipGroupSubcat.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroupSubcat.getChildAt(i);
                    if (chip.isChecked()) {
                        Log.e("INFO CHIP LOOP 2 / IS CHECKED", chip.getText().toString());
                        Subcat sct = db.selectSubcat(chip.getId());
                        prodSubcatNew.add(new ProdSubcat(p, sct));
                    }
                }

                for (ProdSubcat psct: prodSubcatNew) {
                    db.addProdSubcat(psct);
                    Log.e("INFO PRODSUBCAT SENDO GRAVADO", psct.getProduto().getDescricao() + " - " + psct.getSubcat().getDescricao());
                }

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

                Double valor = MaskEditUtil.moneyToDouble(editTextValor.getText().toString());

                Linha l = linhaSelecionada;
                Marca m = db.selectMarca(l.getMarca().getId());

                Produto p = new Produto(desc, valor, l);
                db.addProduto(p);

                p = db.selectMaxProduto();

                Categoria c = categoriaSelecionada;
                ArrayList<ProdSubcat> prodSubcats = new ArrayList<ProdSubcat>();
                for (int i = 0; i < chipGroupSubcat.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroupSubcat.getChildAt(i);
                    if (chip.isChecked()) {
                        if (chip.getId() < 0) {
                            Subcat sc = new Subcat(chip.getText().toString(), categoriaSelecionada);
                            db.addSubcat(sc);
                            sc = db.selectMaxSubcat();
                            prodSubcats.add(new ProdSubcat(p, sc));
                        } else {
                            Subcat sc = db.selectSubcat(chip.getId());
                            prodSubcats.add(new ProdSubcat(p, sc));
                        }
                    }
                }

                for (ProdSubcat psc: prodSubcats) {
                    db.addProdSubcat(psc);
                    Log.e("INFO PRODSUBCAT SENDO GRAVADO", psc.getProduto().getDescricao() + " - " + psc.getSubcat().getDescricao());
                }

                setResult(RESULT_OK);
                finish();

            }
        });

        Cancelar.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private boolean confereCampos () {
        // conferir se os campos estão preenchidos e/ou selecionados

        String descProd = editTextDescricao.getText().toString();
        String valorBruto = editTextValor.getText().toString();

        Log.e("INFO VALOR", String.valueOf(valorBruto.length()));

        if (descProd.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha a descrição do produto", Toast.LENGTH_SHORT).show();
            editTextDescricao.requestFocus();
            return false;
        } else
        if (valorBruto.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insira um valor para o produto", Toast.LENGTH_SHORT).show();
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
        textCifra           = (TextView) findViewById(R.id.textCifraValorProduto);

        //-- Edit text e botões para teste

        chipGroupSubcat = findViewById(R.id.chipGroupAddProduto);
        chipAddSubcat = findViewById(R.id.chipAddSubcat);

    }

    private void initEditOnFocus () {

        editTextValor.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                textMarca.performClick();
            }
            return false;
        });

    }

    private void changellButtons () {

    }

    private void marcaDropdown () {

        textMarca.setOnClickListener(view -> {
            marcas = db.listMarcasOrdered();
            listaDinamicaMarca = new ArrayList<Marca>();

            if (!marcas.isEmpty()) {
                listaDinamicaMarca.addAll(marcas);
            }

            dialogMarca = new Dialog(AddProduto.this);
            dialogMarca.setContentView(R.layout.spinner_marca);

            adapterMarca = new AdapterMarca(dialogMarca.getContext(), 0, listaDinamicaMarca);

            dialogMarca.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
            dialogMarca.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogMarca.setCanceledOnTouchOutside(true);

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

            listViewMarcas.setOnItemClickListener((adapterView, view1, i, l) -> {
                try {
                    Marca m = (Marca) listViewMarcas.getItemAtPosition(i);
                    textMarca.setText(m.getDescricao());
                    if (marcaSelecionada != m) {
                        marcaSelecionada = m;
                        linhaSelecionada = null;
                    }

                    dialogMarca.dismiss();
                    linhaDropdown();
                    textLinha.performClick();
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            });
        });
    }

    private void linhaDropdown () {

        textLinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linhas = db.listLinhasByMarcaOrdered(marcaSelecionada);
                listaDinamicaLinha = new ArrayList<Linha>();

                if (!linhas.isEmpty()) {
                    listaDinamicaLinha.addAll(linhas);
                }

                dialogLinha = new Dialog(AddProduto.this);
                dialogLinha.setContentView(R.layout.spinner_linha);

                adapterLinha = new AdapterLinha(dialogLinha.getContext(), 0, listaDinamicaLinha);

                dialogLinha.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
                dialogLinha.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogLinha.setCanceledOnTouchOutside(true);

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

                listViewLinhas.setOnItemClickListener((adapterView, view1, i, lo) -> {
                    try {
                        Linha l = (Linha) listViewLinhas.getItemAtPosition(i);
                        textLinha.setText(l.getDescricao());
                        if (linhaSelecionada != l) {
                            linhaSelecionada = l;
                        }

                        dialogLinha.dismiss();
                        textCategoria.performClick();
                    } catch (Exception e) {
                        Log.e("ERROR", e.getMessage().toString());
                    }
                });
            }
        });
    }

    private void categoriaDropdown () {

        textCategoria.setOnClickListener(view -> {
            categorias = db.listCategoriasOrdered();
            listaDinamicaCategoria = new ArrayList<Categoria>();

            if (!categorias.isEmpty()) {
                listaDinamicaCategoria.addAll(categorias);
            }

            dialogCategoria = new Dialog(AddProduto.this);
            dialogCategoria.setContentView(R.layout.spinner_categoria);

            adapterCategoria = new AdapterCategoria(dialogCategoria.getContext(), 0, listaDinamicaCategoria);

            dialogCategoria.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
            dialogCategoria.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogCategoria.setCanceledOnTouchOutside(true);

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

            listViewCategorias.setOnItemClickListener((adapterView, view1, i, l) -> {
                try {
                    Categoria c = (Categoria) listViewCategorias.getItemAtPosition(i);
                    textCategoria.setText(c.getDescricao());
                    if (categoriaSelecionada != c) {
                        categoriaSelecionada = c;
                        testeInsereSubcats();
                        chipGroupSubcat.removeAllViews();
                    }

                    dialogCategoria.dismiss();
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }
            });
        });

    }

    private void atualizaListaMarcas (CharSequence _desc) {
        marcas = db.listMarcasByDesc(_desc.toString());
        listaDinamicaMarca = new ArrayList<Marca>();

        if (marcas != null && !marcas.isEmpty()) {
            listaDinamicaMarca.addAll(marcas);
        }

        adapterMarca = new AdapterMarca(dialogMarca.getContext(), 0, listaDinamicaMarca);
        listViewMarcas.setAdapter(adapterMarca);
        adapterMarca.notifyDataSetChanged();
    }

    private void atualizaListaLinhas (CharSequence _desc) {
        linhas = db.listLinhasByMarcaDesc(marcaSelecionada, _desc.toString());
        listaDinamicaLinha = new ArrayList<Linha>();

        if (linhas != null && !marcas.isEmpty()) {
            listaDinamicaLinha.addAll(linhas);
        }

        adapterLinha = new AdapterLinha(dialogLinha.getContext(), 0, listaDinamicaLinha);
        listViewLinhas.setAdapter(adapterLinha);
        adapterLinha.notifyDataSetChanged();
    }

    private void atualizaListaCategorias (CharSequence _desc) {
        categorias = db.listCategoriasByDesc(_desc.toString());
        listaDinamicaCategoria = new ArrayList<Categoria>();

        if (categorias != null && !categorias.isEmpty()) {
            listaDinamicaCategoria.addAll(categorias);
        }

        adapterCategoria = new AdapterCategoria(dialogCategoria.getContext(), 0, listaDinamicaCategoria);
        listViewCategorias.setAdapter(adapterCategoria);
        adapterCategoria.notifyDataSetChanged();
    }

    private void atualizaListaSubcats (CharSequence _desc) {
        subcats = db.listSubcatsByCategoriaDesc(categoriaSelecionada, _desc.toString());
        listaDinamicaSubcat = new ArrayList<Subcat>();


        if (subcats != null && !subcats.isEmpty()) {
            listaDinamicaSubcat.addAll(subcats);
        }

        adapterSubcat = new AdapterSubcat(dialogSubcat.getContext(), 0, listaDinamicaSubcat);
        listViewSubcats.setAdapter(adapterSubcat);
        adapterSubcat.notifyDataSetChanged();
    }

    private void testeInsereSubcats () {

        chipAddSubcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("INFO CHIP", "ENTROU NO ONCLICK");

                subcats = db.listSubcatsByCategoriaOrdered(categoriaSelecionada);
                listaDinamicaSubcat = new ArrayList<Subcat>();

                if (!subcats.isEmpty()) {
                    listaDinamicaSubcat.addAll(subcats);
                }

                dialogSubcat = new Dialog(AddProduto.this);
                dialogSubcat.setContentView(R.layout.spinner_subcat);

                adapterSubcat = new AdapterSubcat(dialogSubcat.getContext(), 0, listaDinamicaSubcat);

                dialogSubcat.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
                dialogSubcat.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogSubcat.setCanceledOnTouchOutside(true);

                dialogSubcat.show();

                TextView tvSubcat = dialogSubcat.findViewById(R.id.tvSpinnerSubcat);
                EditText editSubcat = dialogSubcat.findViewById(R.id.editTextSpinnerSubcat);

                listViewSubcats = (ListView) dialogSubcat.findViewById(R.id.lvSpinnerSubcat);
                listViewSubcats.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                listViewSubcats.setAdapter(adapterSubcat);

                editSubcat.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        atualizaListaSubcats(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listViewSubcats.setOnItemClickListener((adapterView, view1, i, l) -> {
                    try {
                        Subcat s = (Subcat) listViewSubcats.getItemAtPosition(i);
                        addChipSubcat(s.getDescricao(), s.getId());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", e.getMessage());
                    }

                    dialogSubcat.dismiss();
                });
            }
        });
    }

    private void addChipSubcat (String text, int id) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setChecked(true);
        chip.setCheckedIconVisible(false);
        if (id > 0)
            chip.setId(id);

        chipGroupSubcat.addView(chip);

        setChipCheckedStyle(chip);

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChipCheckedStyle(chip);
                Log.e("INFO TOUCK CHIP - ", String.valueOf(chip.isChecked()));
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void setChipCheckedStyle(@NonNull Chip chip) {
        if (chip.isChecked()) {
            chip.setChipBackgroundColorResource(R.color.light_gray_01);
            chip.setChipStrokeColorResource(R.color.purple_500);
            chip.setTextAppearanceResource(R.style.ChipText_SelectedStyle);
            chip.setChipStrokeWidth(5);
        } else {
            chip.setChipBackgroundColorResource(R.color.light_gray_01);
            chip.setRippleColor(ColorStateList.valueOf(R.color.dark_gray_01));
            chip.setTextAppearanceResource(R.style.ChipText_SelectedStyle);
            chip.setChipStrokeWidth(2);
            chip.setChipStrokeColorResource(R.color.dark_gray_01);
        }
    }
}
