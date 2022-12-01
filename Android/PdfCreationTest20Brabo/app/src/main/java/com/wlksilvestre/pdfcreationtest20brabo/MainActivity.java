package com.wlksilvestre.pdfcreationtest20brabo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private static int WRITE_REQUEST_CODE = 200;

    Button generatePDFbtn;
    View view;
    ConstraintLayout clPdfTeste;

    int pageHeight = 240;
    int pagewidth = 420;

    Bitmap bmp, scaledbmp;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view = findViewById(R.id.defaultView);
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.testebmp);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);
        clPdfTeste = findViewById(R.id.clPdfTeste);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                writePdfFile("GFG");
            }
        });

        ViewTreeObserver vto = clPdfTeste.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    this.clPdfTeste.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    this.clPdfTeste.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();

            }
        });
    }

    private void writePdfFile(String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(MainActivity.this, drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private Bitmap getBitmapFromView (View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getwi, view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == WRITE_REQUEST_CODE) {
            /*
                // Método falho 01
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = (FileOutputStream) getContentResolver().openOutputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                setContentView(R.layout.pdf_test);
                Bitmap bitmap = getBitmap(R.layout.pdf_test);

                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);

                Canvas canvas = new Canvas();
                canvas.drawBitmap(bitmap, 0, 0, null);
                pdfDocument.finishPage(myPage);
                pdfDocument.writeTo(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this, "saved " + "GFG.pdf", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "something went wrong" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


                // Método Original
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = (FileOutputStream) getContentResolver().openOutputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
                PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);

                Canvas canvas = myPage.getCanvas();

                Paint docTitle = new Paint();
                docTitle.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
                docTitle.setTextSize(12);
                docTitle.setColor(ContextCompat.getColor(this, R.color.purple_200));

                Paint textBold = new Paint();
                textBold.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                textBold.setTextSize(10);
                textBold.setColor(ContextCompat.getColor(this, R.color.black));

                Paint textRegular = new Paint();
                textRegular.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                textRegular.setTextSize(10);
                textRegular.setColor(ContextCompat.getColor(this, R.color.black));

                Paint bar = new Paint();
                bar.setColor(ContextCompat.getColor(this, R.color.purple_200));

                canvas.drawText("Gerenciador de Vendas", 5, 10, docTitle);
                canvas.drawLine(17, 146, 18, 416, bar);

                pdfDocument.finishPage(myPage);
                pdfDocument.writeTo(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this, "saved " + "GFG.pdf", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "something went wrong" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

             */

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = (FileOutputStream) getContentResolver().openOutputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                // Desenha o bitmap a partir da página
                setContentView(R.layout.pdf_test);
                //Bitmap bitmap = getBitmap(R.layout.pdf_test);
                Bitmap bitmap = getBitmapFromView(findViewById(R.id.clPdfTeste));

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
}