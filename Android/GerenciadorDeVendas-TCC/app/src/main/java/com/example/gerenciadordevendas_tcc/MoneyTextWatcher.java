package com.example.gerenciadordevendas_tcc;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class MoneyTextWatcher implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;

    public MoneyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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

        String cleanString = s.replaceAll("[R$,.]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        formatted = formatted.replaceAll("[$]", "")
                             .replaceAll("[R]", "");/*
                             .replaceAll("[,]", "a")
                             .replaceAll("[.]", ",")
                             .replaceAll("[a]", ".");*/

        editText.setText(formatted);
        editText.setSelection(formatted.length());

        editText.addTextChangedListener(this);
    }
}
