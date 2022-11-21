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

        if (editText == null) return;

        String s = editable.toString();

        if (s.isEmpty()) return;

        editText.removeTextChangedListener(this);


        Log.e("INFO NUMBER 1//", s);
        String cleanString = s.replaceAll("[R$,.]", "");

        //StringBuilder str = new StringBuilder(cleanString);

        Log.e("INFO NUMBER 2//", cleanString);

        /*
        char spl[] = cleanString.toCharArray();
        char novo[] = new char[spl.length];

        for (int i = 0; i < spl.length; i++) {
            if (spl[i] == '0' || spl[i] == 0 ) {
                Log.e("INFO FOR", "zero encontrado");
            } else {
                // corrigir
            }
        }
        */
        int value = Integer.parseInt(cleanString);
        Log.e("INFO NUMBER 3//", String.valueOf(value));

        double test = (double) (value / 100);
        Log.e("INFO NUMBER 5//", String.valueOf(test));

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String values = nf.getCurrency().getDisplayName();
        Log.e("INFO NUMBER 6//", values);

        String formatted = nf.format(test);
        Log.e("INFO NUMBER 7//", formatted);

        /*
        for (int i = test.length; i > 0; i--) {
            Log.e("INFO CHAR", i + " - " + test[i]);
            valor += test[i] / exp;
            exp = exp / 10;
            Log.e("INFO CHAR", valor + " - " + exp);
        }
         */
        //String formatted = NumberFormat.getCurrencyInstance().format(test);

        /*
        BigDecimal parsed = new BigDecimal(String.valueOf(test)).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);*/
        formatted = formatted.replaceAll("[$]", "")
                             .replaceAll("[R]", "")
                             .replaceAll("[ ]", ""); /*
                             .replaceAll("[,]", "a")
                             .replaceAll("[.]", ",")
                             .replaceAll("[a]", ".");*/

        editText.setText(formatted);
        editText.setSelection(formatted.length());

        editText.addTextChangedListener(this);
    }
}
