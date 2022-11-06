package com.example.gerenciadordevendas_tcc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateCustomText {


    public static String getWeekDay (String date) {
        String dayWeek = "";
        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTime(new SimpleDateFormat("dd/MM/yyy HH:mm:ss").parse(date));
            switch (gc.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    dayWeek = "Domingo";
                    break;
                case Calendar.MONDAY:
                    dayWeek = "Segunda";
                    break;
                case Calendar.TUESDAY:
                    dayWeek = "Terça";
                    break;
                case Calendar.WEDNESDAY:
                    dayWeek = "Quarta";
                    break;
                case Calendar.THURSDAY:
                    dayWeek = "Quinta";
                    break;
                case Calendar.FRIDAY:
                    dayWeek = "Sexta";
                    break;
                case Calendar.SATURDAY:
                    dayWeek = "Sábado";
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayWeek;
    }

    public static String getMonthName (String date) {
        String monthName = "";
        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date));
            switch (gc.get(Calendar.MONTH)) {
                case Calendar.JANUARY:
                    monthName = "Janeiro";
                    break;
                case Calendar.FEBRUARY:
                    monthName = "Fevereiro";
                    break;
                case Calendar.MARCH:
                    monthName = "Março";
                    break;
                case Calendar.APRIL:
                    monthName = "Abril";
                    break;
                case Calendar.MAY:
                    monthName = "Maio";
                    break;
                case Calendar.JUNE:
                    monthName = "Junho";
                    break;
                case Calendar.JULY:
                    monthName = "Julho";
                    break;
                case Calendar.AUGUST:
                    monthName = "Agosto";
                    break;
                case Calendar.SEPTEMBER:
                    monthName = "Setembro";
                    break;
                case Calendar.OCTOBER:
                    monthName = "Outubro";
                    break;
                case Calendar.NOVEMBER:
                    monthName = "Novembro";
                    break;
                case Calendar.DECEMBER:
                    monthName = "Dezembro";
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthName;
    }

    public static String getExtenseDate (String date) {
        GregorianCalendar gc = new GregorianCalendar();
        String extenseDate = "";
        try {
            gc.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date));
            extenseDate = getWeekDay(date) + ", " +
                    gc.get(Calendar.DAY_OF_MONTH) + " de " +
                    getMonthName(date) + " de " +
                    gc.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return extenseDate;
    }

    public static String getCustomTime (String date) {
        GregorianCalendar gc = new GregorianCalendar();
        String customTime = "";
        try {
            gc.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date));
            customTime = gc.get(Calendar.HOUR) + "h" + gc.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return customTime;
    }
}
