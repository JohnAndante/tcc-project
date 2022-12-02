package com.wlksilvestre.gerenciadordevendas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ComplexColorCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewProduto extends AppCompatActivity {

    private Button Editar;
    private Button Voltar;
    private TextView textDesc;
    private TextView textValor;
    private TextView textMarca;
    private TextView textLinha;
    private TextView textCategoria;
    private ChipGroup chipGroupSubcats;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private int idProduto;

    public static final int ALTERAR_PRODUTO = 102;
    public static final int RESULT_ALT_PRODUTO = 202;

    private boolean produto_alterado;

    private Produto produto;
    private int posicao;
    private boolean hasPosicao = false;
    private List<Subcat> subcats;
    private ArrayList<Subcat> arrayListSubcats;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_produto);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Marca marca = new Marca();
        Linha linha = new Linha();
        Categoria categoria = new Categoria();
        Subcat subcat = new Subcat();
        ProdSubcat prodSubcat = new ProdSubcat();
        arrayListSubcats = new ArrayList<Subcat>();

        initTexts();
        initButtons();
        initButtonsOnClick();

        Intent intent = getIntent();
        idProduto = 0;
        produto_alterado = false;

        chipGroupSubcats.removeViews(0, chipGroupSubcats.getChildCount());

        if (intent.hasExtra("ID")) {
            idProduto = intent.getIntExtra("ID", 0);


            produto = db.selectProduto(idProduto);
            linha = produto.getLinha();
            marca = linha.getMarca();
            prodSubcat = db.selectFirstProdSubcatByProd(produto);

            if (prodSubcat != null) {
                subcat = prodSubcat.getSubcat();
                categoria = subcat.getCategoria();
            }

            List<ProdSubcat> prodSubcats = db.listProdSubcatsByProduto(produto);

            arrayListSubcats = new ArrayList<>();

            if (!prodSubcats.isEmpty()) {
                for (ProdSubcat ps : prodSubcats) {
                    arrayListSubcats.add(ps.getSubcat());
                }
            }

            textDesc.setText(produto.getDescricao());
            textValor.setText("R$ " + MaskEditUtil.doubleToMoneyValue(produto.getValor()));
            textMarca.setText(marca.getDescricao());
            textLinha.setText(linha.getDescricao());
            textCategoria.setText(categoria.getDescricao());

            if (!arrayListSubcats.isEmpty()) {
                for (Subcat s : arrayListSubcats) {
                    addChipSubcat(s.getDescricao());
                }
            }

            if (intent.hasExtra("posicao")) {
                posicao = intent.getIntExtra("posicao", 0);
                hasPosicao = true;
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Marca marca = new Marca();
        Linha linha = new Linha();
        Categoria categoria = new Categoria();
        Subcat subcat = new Subcat();
        ProdSubcat prodSubcat = new ProdSubcat();

        // Alterando dados
        if ((requestCode == ALTERAR_PRODUTO) && (resultCode == RESULT_OK)) {
            produto_alterado = true;

            produto = db.selectProduto(idProduto);
            linha = produto.getLinha();
            marca = linha.getMarca();
            prodSubcat = db.selectFirstProdSubcatByProd(produto);
            if (prodSubcat != null) {
                subcat = prodSubcat.getSubcat();
                categoria = subcat.getCategoria();
            }

            List<ProdSubcat> prodSubcats = db.listProdSubcatsByProduto(produto);

            arrayListSubcats = new ArrayList<>();

            for (int i = 0; i < chipGroupSubcats.getChildCount(); i++) {
                chipGroupSubcats.removeView(chipGroupSubcats.getChildAt(i));
            }

            if (!prodSubcats.isEmpty()) {
                for (ProdSubcat ps : prodSubcats) {
                    arrayListSubcats.add(ps.getSubcat());
                }
            }

            textDesc.setText(produto.getDescricao());
            textValor.setText("R$ " + MaskEditUtil.doubleToMoneyValue(produto.getValor()));
            textMarca.setText(marca.getDescricao());
            textLinha.setText(linha.getDescricao());
            textCategoria.setText(categoria.getDescricao());

            if (!arrayListSubcats.isEmpty()) {
                for (Subcat s : arrayListSubcats) {
                    addChipSubcat(s.getDescricao());
                }
            }
        }
    }

    private void initTexts () {
        textDesc            = (TextView) findViewById(R.id.tvDescricaoProduto);
        textValor           = (TextView) findViewById(R.id.tvValorProduto);
        textMarca           = (TextView) findViewById(R.id.tvMarcaProduto);
        textLinha           = (TextView) findViewById(R.id.tvLinhaProduto);
        textCategoria       = (TextView) findViewById(R.id.tvCategoriaProduto);
        chipGroupSubcats    = (ChipGroup) findViewById(R.id.chipGroupViewProduto);
    }

    private void initButtons () {
        Editar = (Button) findViewById(R.id.btViewProdutoEditar);
        Voltar = (Button) findViewById(R.id.btViewProdutoVoltar);
    }

    private void initButtonsOnClick () {
        Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProduto.this, AddProduto.class);
                Bundle bundle = new Bundle();

                bundle.putInt("ID", idProduto);
                if (hasPosicao == true)
                    bundle.putInt("posicao", posicao);
                intent.putExtras(bundle);
                startActivityForResult(intent, ALTERAR_PRODUTO);
            }
        });

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (produto_alterado) {
                    Intent intent2 = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", produto.getId());
                    if (hasPosicao == true)
                        bundle.putInt("posicao", posicao);
                    intent2.putExtras(bundle);

                    setResult(RESULT_ALT_PRODUTO, intent2);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void addChipSubcat (String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(false);

        chip.setChipBackgroundColorResource(R.color.light_gray_01);
        chip.setChipStrokeColorResource(R.color.purple_500);
        chip.setTextAppearanceResource(R.style.ChipText_SelectedStyle);
        chip.setChipStrokeWidth(5);

        //chip.setChipStrokeColor();
        //chip.setChipWidth();
        //chip.setChipBackgroundColor(ColorStateList.valueOf(R.color.light_gray_02));
        //chip.setChipIconTint(ColorStateList.valueOf(R.color.light_blue_01));
        //chip.setBackgroundColor(R.color.light_blue_01);
        //chip.setTextColor(R.color.light_gray_02);

        chipGroupSubcats.addView(chip);
    }
}
