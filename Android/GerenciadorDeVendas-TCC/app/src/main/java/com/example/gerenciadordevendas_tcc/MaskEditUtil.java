package com.example.gerenciadordevendas_tcc;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class MaskEditUtil {

    public static final String FORMAT_CPF = "###.###.###-##";
    public static final String FORMAT_FONE = "(##) ####-#####";
    public static final String FORMAT_FONE2 = "(##) # ####-#####";
    public static final String FORMAT_CEP = "#####-###";
    public static final String FORMAT_DATE = "##/##/####";
    public static final String FORMAT_HOUR = "##:##";

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt
     * @param mask
     * @return
     */
    public static TextWatcher mask(final EditText ediTxt, final String mask) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void afterTextChanged(final Editable s) {}

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str = MaskEditUtil.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }
        };
    }

    public static TextWatcher decimalMask (final EditText editText) {
        return new TextWatcher() {
            int length = editText.length();

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }

    public static Double moneyToDouble (final String money) {
        String formatted = money.replaceAll("[.]", "")
                                .replaceAll("[,]", ".");

        return Double.parseDouble(formatted);
    }

    public static String doubleToMoneyValue(final Double value) {
        String s = new DecimalFormat("#.00").format(value);
        String cleanString = s.replaceAll("[$,.]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        formatted = formatted.replaceAll("[R]", "")
                             .replaceAll("[$]", "");
        /*
                             .replaceAll("[,]", "a")
                             .replaceAll("[.]", ",")
                             .replaceAll("[a]", ".");
        */
        return formatted;
    }

    public static String doubleToMoneyCipher(final Double value) {
        String s = new DecimalFormat("#.00").format(value);
        String cleanString = s.replaceAll("[$,.]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        return formatted;
    }

    public static String doubleToMoneyTest(final Double value) {

        String s = new DecimalFormat("#.00").format(value);

        Log.e("INFO MASK ORIGINAL DOUBLE", s);

        String cleanString = s.replaceAll("[$,.]", "");

        Log.e("INFO MASK CLEAN STRING", cleanString);

        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);

        Log.e("INFO MASK PARSED", parsed.toString());

        String formatted = NumberFormat.getCurrencyInstance().format(parsed);/*
        formatted = formatted.replaceAll("[R]", "")
                             .replaceAll("[$]", "")
                             .replaceAll("[,]", "a")
                             .replaceAll("[.]", ",")
                             .replaceAll("[a]", ".");
                             */

        Log.e("INFO MASK FORMATTED", formatted);

        return formatted;
    }
}