package com.wlksilvestre.gerenciadordevendas;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyTextWatcher implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;

    public MoneyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String value = charSequence.toString();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        String formatted = "";

        if (editText == null) return;

        String s = editable.toString();

        if (s.isEmpty()) return;

        if (s.length() >= 13) {
            formatted =  s.substring(0, s.length() - 1);
            editText.setText(formatted);
            return;
        }

        editText.removeTextChangedListener(this);

        String a = "";
        String b = "";
        String c = "";
        String d = "";

        String cleanString = s.replaceAll("[^0-9]+", "")
                //.replace("[/^0+/]", "");
                .replaceFirst("^0+(?!$)", "");

        if (cleanString.replaceAll("[0]", "").equals(""))
            Log.e("INFO", "True");

        if (cleanString.length() > 1) {
            a = cleanString.substring(cleanString.length()-2);
        } else
        if (cleanString.length() > 0) {
            a = cleanString.substring(0, cleanString.length());
            a = "0" + a;
        }

        if (cleanString.length() > 2 && cleanString.length() >= 5) {
            b = cleanString.substring(cleanString.length()-5, cleanString.length()-2);
        } else
        if (cleanString.length() > 2) {
            b = cleanString.substring(0, cleanString.length()-2);
        }

        if (cleanString.length() > 5 && cleanString.length() >= 8) {
            c = cleanString.substring(cleanString.length()-8, cleanString.length()-5);
        } else
        if (cleanString.length() > 5) {
            c = cleanString.substring(0, cleanString.length()-5);
        }

        if (cleanString.length() > 8 && cleanString.length() >= 11) {
            d = cleanString.substring(cleanString.length()-11, cleanString.length()-8);
        } else
        if (cleanString.length() > 8) {
            d = cleanString.substring(0, cleanString.length()-8);
        }

        String y = "0," + a;

        if (!b.equals("")) {
            y = b + "," + a;

            if (!c.equals("")) {
                y = c + "." + b + "," + a;

                if (!d.equals("")) {
                    y = d + "." + c + "." + b + "," + a;
                }
            }
        }

        formatted = y;
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}
