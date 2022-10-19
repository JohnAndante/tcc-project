package com.example.tcc_gerenciadordevendas;

import android.content.Context;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreatePdf {

    public static void main (String args[]) throws IOException {

        PDDocument pdfdoc = new PDDocument();
        pdfdoc.addPage(new PDPage());

        pdfdoc.save("");
    }

    public void writeFileOnInternalStorage (Context context, String sFileName, String sBody) {
        File dir = new File(context.getFilesDir(), "mydir");
        if (!dir.exists())
            dir.mkdir();

        try {
            File gpxfile = new File(dir, sFileName);
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(sBody);
                writer.flush();
                writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
