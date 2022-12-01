package com.wlksilvestre.gerenciadordevendas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewVenda extends AppCompatActivity {

    private Button Voltar;
    private Button NovaVenda;
    private Button EnviaComprovante;
    private Button AdicionaPgto;
    private TextView textNomeCliente;
    private TextView textTelefoneCliente;
    private TextView textDataVenda;
    private TextView textHoraVenda;
    private TextView textValorVenda;
    private TextView textFormaPgto;
    private TextView textPgtoVenda;
    private TextView textQtdParcelas;
    private TextView textValorJuros;
    private TextView textValorTotalVenda;
    private ListView listProdutos;
    private ConstraintLayout clFormaPgto;
    private ConstraintLayout clJurosAplicados;
    private ConstraintLayout clDadosVenda;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private int idVenda;
    private Venda venda;
    private List<Produto> produtos;
    private ArrayList<ProdVenda> arrayListProdVenda;
    private AdapterProdutoVenda adapterProdutoVenda;

    private static int WRITE_REQUEST_CODE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venda);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Cliente cliente = new Cliente();
        Telefone telefone = new Telefone();
        Pgto pgto = new Pgto();
        arrayListProdVenda = new ArrayList<ProdVenda>();

        initTextViews();
        initButtons();
        initButtonsOnClick();

        Intent intent = getIntent();
        idVenda = 0;

        clDadosVenda = (ConstraintLayout) findViewById(R.id.clDadosVenda);

        if (intent.hasExtra("ID")) {
            idVenda = intent.getIntExtra("ID", 0);

            venda = db.selectVenda(idVenda);
            cliente = venda.getCliente();
            telefone = db.selectTelefoneFirst(cliente);
            pgto = venda.getPgto();
            List<ProdVenda> prodsVenda = db.listProdVendaByVenda(venda);

            String dataVenda = venda.getData();

            textDataVenda.setText(DateCustomText.getExtenseDate(dataVenda));
            textHoraVenda.setText(DateCustomText.getCustomTime(dataVenda));

            textNomeCliente.setText(cliente.getNome());
            textTelefoneCliente.setText(telefone.getNum());

            textValorVenda.setText(MaskEditUtil.doubleToMoneyValue(venda.getValor()));

            clFormaPgto         = (ConstraintLayout) findViewById(R.id.clFormaPgto);
            clJurosAplicados    = (ConstraintLayout) findViewById(R.id.clJurosAplicados);
            clDadosVenda        = (ConstraintLayout) findViewById(R.id.clDadosVenda);

            if (pgto == null || pgto.getId() < 0){
                clFormaPgto.setVisibility(View.GONE);
                AdicionaPgto.setVisibility(View.VISIBLE);
            } else {
                clFormaPgto.setVisibility(View.VISIBLE);
                textPgtoVenda.setText(pgto.getFormaPgto().getDescricao());
                textQtdParcelas.setText(Integer.toString(pgto.getParcelas()));
                textValorTotalVenda.setText(MaskEditUtil.doubleToMoneyValue(pgto.getValor()));
                if (pgto.getJuros() != 0.00) {
                    clJurosAplicados.setVisibility(View.VISIBLE);
                    textValorJuros.setText(MaskEditUtil.doubleToMoneyValue(pgto.getJuros()));
                } else {
                    clJurosAplicados.setVisibility(View.GONE);
                }
                AdicionaPgto.setVisibility(View.GONE);
            }

            adapterProdutoVenda = new AdapterProdutoVenda(this, 0, arrayListProdVenda);

            listProdutos = findViewById(R.id.lvProdutosVenda);
            listProdutos.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            listProdutos.setAdapter(adapterProdutoVenda);

            if (!prodsVenda.isEmpty()) {
                for (ProdVenda pv : prodsVenda) {
                    arrayListProdVenda.add(pv);
                    justifyListViewHeightBasedOnChildren(listProdutos);
                }
            }
        }
    }

    private void initTextViews () {
        textNomeCliente     = (TextView) findViewById(R.id.tvNomeClienteVenda);
        textTelefoneCliente = (TextView) findViewById(R.id.tvTelefoneClienteVenda);
        textDataVenda       = (TextView) findViewById(R.id.textDataVenda);
        textHoraVenda       = (TextView) findViewById(R.id.textHoraVenda);
        textValorVenda      = (TextView) findViewById(R.id.tvValorVenda);
        textFormaPgto       = (TextView) findViewById(R.id.textLabelFormaPgto);
        textPgtoVenda       = (TextView) findViewById(R.id.tvFormaPgtoDesc);
        textQtdParcelas     = (TextView) findViewById(R.id.tvQtdParcela);
        textValorJuros      = (TextView) findViewById(R.id.tvValorJurosAplicados);
        textValorTotalVenda = (TextView) findViewById(R.id.tvValorTotalComJuros);
    }

    private void initButtons () {
        Voltar = (Button) findViewById(R.id.btViewVoltar);
        NovaVenda = (Button) findViewById(R.id.btViewNovaVenda);
        EnviaComprovante = (Button) findViewById(R.id.btExportarComprovante);
        AdicionaPgto = (Button) findViewById(R.id.btAdicionarPgtoVenda);
    }

    private void initButtonsOnClick () {
        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewVenda.this, ListVendas.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        NovaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewVenda.this, AddVenda.class);
                startActivity(intent);
            }
        });

        EnviaComprovante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewData();
                writePdfFile("comprovante_venda" + venda.getCliente().getDataUpdate());
            }
        });

        AdicionaPgto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPgtoActivity();
            }
        });
    }

    private void startPgtoActivity () {
        Intent intent = new Intent(ViewVenda.this, AddVendaDetails.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", venda.getId());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void writePdfFile(String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    private void getViewData () {
        ConstraintLayout cl = findViewById(R.id.clDadosVenda);
        Log.e("VIEW SIZE", cl.getWidth() + " // " + cl.getHeight());

    }

    private Bitmap LoadBitmap (View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(ContextCompat.getColor(this, R.color.dark_gray_01));

        v.draw(canvas);
        return bitmap;
    }

    private Bitmap getBitmap () {
        ConstraintLayout cl = findViewById(R.id.clDadosVenda);
        Bitmap bitmap = Bitmap.createBitmap(cl.getWidth(), cl.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        return bitmap;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == WRITE_REQUEST_CODE) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = (FileOutputStream) getContentResolver().openOutputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                ConstraintLayout cl = findViewById(R.id.clDadosVenda);

                //Bitmap bitmap = getBitmap(R.layout.pdf_test);
                Bitmap bitmap = LoadBitmap(cl, cl.getWidth(), cl.getHeight());

                // Cria o documento virtual
                PdfDocument pdfDocument = new PdfDocument();

                // seta o tamanho a partir do bitmap
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);

                Canvas canvas = myPage.getCanvas();

                // Insere o bitmap no canvas
                canvas.drawBitmap(bitmap, 0f, 0f, null);

                // finaliza a página
                pdfDocument.finishPage(myPage);

                pdfDocument.writeTo(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this, "Arquivo salvo com sucesso.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao salvar arquivo." + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

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
}
