package com.wlksilvestre.gerenciadordevendas;

import android.app.Dialog;
import android.content.Context;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AddVendaDetails extends AppCompatActivity {

    private Button btSalvar;
    private Button btVoltar;
    private TextView tvDataVenda;
    private TextView tvHoraVenda;
    private TextView tvNomeCliente;
    private TextView tvTelefoneCliente;
    private TextView tvValorTotalVenda;
    private TextView tvFormaPgto;
    private TextView tvTituloParcelas;
    private TextView tvTituloJuros;
    private TextView tvValorTotalJuros;
    private TextView tvValorJuros;
    private EditText editParcelas;
    private EditText editJuros;
    private ListView lvProdutosVenda;
    private LinearLayout llParcelas;
    private LinearLayout llBotoesVenda;
    private ConstraintLayout clJuros;
    private ConstraintLayout clValorJuros;
    private ConstraintLayout clJurosAplicados;
    private ScrollView svVendaDetails;
    private ImageView imgLessParcela;
    private ImageView imgMoreParcela;

    BancoDadosCliente db = new BancoDadosCliente(this);
    private final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    private ArrayList<ProdVenda> arrayListProdVenda;
    private ArrayList<FormaPgto> listaDinamicaFormaPgto;
    private AdapterProdutoVenda adapterProdutoVenda;
    private AdapterFormaPgto adapterFormaPgto;
    private ListView listViewFormaPgtos;
    private Dialog dialogFormaPgto;
    private Venda venda;
    private FormaPgto formaPgtoSelecionado;
    private int idVenda;
    private int qtdParcelas;
    private Double valorComJuros = 0.00;
    private Double pctJuros = 0.00;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venda_02);

        Cliente cliente = new Cliente();
        Telefone telefone = new Telefone();
        arrayListProdVenda = new ArrayList<>();

        initTextViews();
        initButtons();
        initEditTexts();
        initListViews();
        initLayouts();
        initImageViews();

        Intent intent = getIntent();
        idVenda = 0;

        if (intent.hasExtra("ID")) {
            idVenda = intent.getIntExtra("ID", 0);

            venda = db.selectVenda(idVenda);
            cliente = venda.getCliente();
            telefone = db.selectTelefoneFirst(cliente);

            List<ProdVenda> prodVendaList = db.listProdVendaByVenda(venda);

            if (!prodVendaList.isEmpty()) {
                arrayListProdVenda.addAll(prodVendaList);
            }

            tvDataVenda.setText(DateCustomText.getExtenseDate(venda.getData()));
            tvHoraVenda.setText(DateCustomText.getCustomTime(venda.getData()));
            tvNomeCliente.setText(cliente.getNome());
            tvTelefoneCliente.setText(cliente.getTelefone());
            tvValorTotalVenda.setText(MaskEditUtil.doubleToMoneyValue(venda.getValor()));

            clValorJuros.setVisibility(View.GONE);
            tvTituloParcelas.setVisibility(View.GONE);
            llParcelas.setVisibility(View.GONE);
            tvTituloJuros.setVisibility(View.GONE);
            clJuros.setVisibility(View.GONE);
            clJurosAplicados.setVisibility(View.GONE);

            adapterProdutoVenda = new AdapterProdutoVenda(this, 0, arrayListProdVenda);
            lvProdutosVenda.setAdapter(adapterProdutoVenda);

            justifyListViewHeightBasedOnChildren(lvProdutosVenda);

            initButtonsOnClick();
            initImageViewsOnClick();
            dropdownFormaPgto();

            editJuros.setOnFocusChangeListener((view, b) -> {
                if (editJuros.isFocused()) {
                    autoFocus(editJuros);
                    llBotoesVenda.setVisibility(View.GONE);
                } else {
                    llBotoesVenda.setVisibility(View.VISIBLE);
                }
            });

            editParcelas.setOnFocusChangeListener((view, b) -> {
                if (editParcelas.isFocused()) {
                    autoFocus(editParcelas);
                    llBotoesVenda.setVisibility(View.GONE);
                } else {
                    llBotoesVenda.setVisibility(View.VISIBLE);
                }
            });

            editJuros.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (editJuros == null) return;
                    String s = editJuros.getText().toString();
                    Log.e("INFO STRING 1", s);
                    if (s.isEmpty()) return;

                    editJuros.removeTextChangedListener(this);

                    String cleanString = s.replaceAll("[R$,.]", "");
                    BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
                    String formatted = NumberFormat.getCurrencyInstance().format(parsed);
                    formatted = formatted.replaceAll("[$]", "")
                                .replaceAll("[R]", "")
                                .replaceAll("[,]", "a")
                                .replaceAll("[.]", ",")
                                .replaceAll("[a]", ".");
                    editJuros.setText(formatted);
                    editJuros.setSelection(formatted.length());
                    editJuros.addTextChangedListener(this);

                    changeTotalJuros();
                }
            });

            editParcelas.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    changeTotalJuros();
                }
            });

            editJuros.setOnEditorActionListener((textView, i, keyEvent) -> {
                if ((i & EditorInfo.IME_MASK_ACTION) != 0) {
                    closeSoftKeyboard();
                    llBotoesVenda.setVisibility(View.VISIBLE);
                    return true;
                }
                else {
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (llBotoesVenda.getVisibility() == View.GONE)
            llBotoesVenda.setVisibility(View.VISIBLE);
    }

    private void initTextViews () {
        tvDataVenda         = findViewById(R.id.textDataNovaVenda);
        tvHoraVenda         = findViewById(R.id.textHoraNovaVenda);
        tvNomeCliente       = findViewById(R.id.tvNomeClienteNovaVenda);
        tvTelefoneCliente   = findViewById(R.id.tvTelefoneClienteNovaVenda);
        tvValorTotalVenda   = findViewById(R.id.tvValorTotalVenda);
        tvFormaPgto         = findViewById(R.id.tvFormaPgtoOpcao);
        tvTituloParcelas    = findViewById(R.id.textParcelas);
        tvTituloJuros       = findViewById(R.id.textJurosParcela);
        tvValorTotalJuros   = findViewById(R.id.tvValorTotalComJuros);
        tvValorJuros        = findViewById(R.id.tvValorJurosAplicados);
    }

    private void initEditTexts () {
        editParcelas    = findViewById(R.id.tvQtdParcelasVenda);
        editJuros       = findViewById(R.id.editPercJurosParcela);
    }

    private void initButtons () {
        btSalvar        = findViewById(R.id.btViewSalvar);
        btVoltar        = findViewById(R.id.btViewVoltar);
    }

    private void initListViews () {
        lvProdutosVenda = findViewById(R.id.lvProdutosNovaVenda);

    }

    private void initLayouts() {
        llParcelas      = findViewById(R.id.llParcelas);
        llBotoesVenda   = findViewById(R.id.llBotoesVenda);
        clJuros         = findViewById(R.id.clJurosParcela);
        clValorJuros    = findViewById(R.id.clValorComJuros);
        clJurosAplicados= findViewById(R.id.clJurosAplicados);
        svVendaDetails  = findViewById(R.id.svVendaDetails);
    }

    private void initImageViews () {
        imgLessParcela = findViewById(R.id.imgLessQtdParcela);
        imgMoreParcela = findViewById(R.id.imgMoreQtdParcela);
    }

    private void justifyListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter listadp = listView.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            for (int i = 0; i < listadp.getCount(); i++) {
                View listItem = listadp.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listadp.getCount() - 1) + 2);
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    private void initButtonsOnClick () {
        btVoltar.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        btSalvar.setOnClickListener(view -> {
            adicionarPgto();
        });
    }

    private void adicionarPgto () {
        Pgto pgto = new Pgto ();
        pgto.setCliente(venda.getCliente());
        pgto.setFormaPgto(formaPgtoSelecionado);
        pctJuros = Double.parseDouble(editJuros.getText().toString());
        if (pctJuros > 0) {
            pgto.setValor(valorComJuros);
            pgto.setJuros(pctJuros);
        } else {
            pgto.setValor(venda.getValor());
        }
        pgto.setData(DateCustomText.getActualDateTime());
        pgto.setParcelas(Integer.parseInt(editParcelas.getText().toString()));

        db.addPgto(pgto);
        pgto = db.selectMaxPgto();
        venda.setPgto(pgto);
        db.updateVenda(venda);

        Intent intent = new Intent(AddVendaDetails.this, ViewVenda.class);
        Bundle bundle = new Bundle();
        bundle.putInt("ID", venda.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initImageViewsOnClick () {
        imgMoreParcela.setOnClickListener(view -> {
            int qtdParcela = Integer.parseInt(editParcelas.getText().toString());
            qtdParcela++;
            qtdParcelas = qtdParcela;
            editParcelas.setText(String.valueOf(qtdParcelas));
            changeTotalJuros();
        });

        imgLessParcela.setOnClickListener(view -> {
            int qtdParcela = Integer.parseInt(editParcelas.getText().toString());
            if (qtdParcela > 1) {
                qtdParcela--;
                editParcelas.setText(String.valueOf(qtdParcela));
            } else {
                Toast.makeText(getApplicationContext(), "Quantidade de parcelas inválida", Toast.LENGTH_SHORT).show();
            }
            qtdParcelas = qtdParcela;
            changeTotalJuros();
        });
    }

    private void dropdownFormaPgto () {
        tvFormaPgto.setOnClickListener(view -> {
            List<FormaPgto> formaPgtos = db.listFormaPgtoOrdered();
            listaDinamicaFormaPgto = new ArrayList<>();

            if (!formaPgtos.isEmpty()) {
                listaDinamicaFormaPgto.addAll(formaPgtos);
            }

            dialogFormaPgto = new Dialog(AddVendaDetails.this);
            dialogFormaPgto.setContentView(R.layout.spinner_forma_pgto);

            adapterFormaPgto = new AdapterFormaPgto(dialogFormaPgto.getContext(), 0, listaDinamicaFormaPgto);
            dialogFormaPgto.getWindow().setLayout((int) (deviceWidth * 0.75), (int) (deviceHeight * 0.75));
            dialogFormaPgto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialogFormaPgto.show();

            TextView tvFormaPgtoDialog = dialogFormaPgto.findViewById(R.id.tvNomeFormaPgto);
            EditText editFormaPgto = dialogFormaPgto.findViewById(R.id.editTextSpinnerFormaPgto);
            listViewFormaPgtos = dialogFormaPgto.findViewById(R.id.lvSpinnerFormaPgto);
            listViewFormaPgtos.setAdapter(adapterFormaPgto);

            editFormaPgto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    atualizaListaFormaPgto(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            listViewFormaPgtos.setOnItemClickListener((adapterView, view1, i, l) -> {
                    FormaPgto fp = (FormaPgto) listViewFormaPgtos.getItemAtPosition(i);
                    formaPgtoSelecionado = fp;
                    tvFormaPgto.setText(fp.getDescricao());
                    if (fp.getParcelavel() > 0) {
                        tvTituloParcelas.setVisibility(View.VISIBLE);
                        llParcelas.setVisibility(View.VISIBLE);
                        tvTituloJuros.setVisibility(View.VISIBLE);
                        editParcelas.setText("1");
                        clJuros.setVisibility(View.VISIBLE);
                        clValorJuros.setVisibility(View.VISIBLE);
                        clJurosAplicados.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvTituloParcelas.setVisibility(View.GONE);
                        llParcelas.setVisibility(View.GONE);
                        tvTituloJuros.setVisibility(View.GONE);
                        clJuros.setVisibility(View.GONE);
                        clValorJuros.setVisibility(View.GONE);
                        clJurosAplicados.setVisibility(View.GONE);
                    }
                    dialogFormaPgto.dismiss();
            });
        });
    }

    private void atualizaListaFormaPgto (CharSequence desc) {
        List<FormaPgto> formaPgtos = db.listFormaPgtoByDesc(desc.toString());
        listaDinamicaFormaPgto = new ArrayList<>();
        listaDinamicaFormaPgto.addAll(formaPgtos);

        adapterFormaPgto = new AdapterFormaPgto(dialogFormaPgto.getContext(), 0, listaDinamicaFormaPgto);
        listViewFormaPgtos.setAdapter(adapterFormaPgto);
        adapterFormaPgto.notifyDataSetChanged();
    }

    private void autoFocus (EditText editText) {
        int[] xy = new int[2];
        editText.getLocationOnScreen(xy);
        Log.e("INFO EDIT TEXT", xy[0] + " " + xy[1]);
        svVendaDetails.getLocationOnScreen(xy);
        Log.e("INFO Scrollview", xy[0] + " " + xy[1]);
        svVendaDetails.smoothScrollTo(xy[0], xy[1]);
    }

    private void closeSoftKeyboard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void changeTotalJuros () {
        qtdParcelas = Integer.parseInt(editParcelas.getText().toString());

        if (editJuros.getText().toString() != null) {
            try {
                pctJuros = Double.parseDouble(editJuros.getText().toString());
            } catch (Exception e) {
                Log.e("INFO ERROR", e.getMessage());
            }
        } else {
            valorComJuros = venda.getValor();
            Log.e("INFO ERROR", "editJuros is null ???");
        }

        if (pctJuros > 0) {
            valorComJuros = venda.getValor();
            double valorTotal = 0.00;
            double valorParcela = valorComJuros / qtdParcelas;
            for (int i = 0; i < qtdParcelas; i++) {
                double y = (valorParcela * (pctJuros / 100));
                valorParcela = valorParcela + y;
                valorTotal = valorTotal + valorParcela;
            }
            valorComJuros = MaskEditUtil.moneyToDouble(MaskEditUtil.doubleToMoneyValue(valorTotal));
            tvValorJuros.setText(MaskEditUtil.doubleToMoneyValue(valorComJuros - venda.getValor()));
        }
        tvValorTotalJuros.setText(MaskEditUtil.doubleToMoneyValue(valorComJuros));
    }
}
