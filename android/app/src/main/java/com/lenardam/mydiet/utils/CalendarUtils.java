package com.lenardam.mydiet.utils;

import android.content.Context;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import com.lenardam.mydiet.R;

public class CalendarUtils {

    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date)
    {
        // użyje języka ustawionego w systemie lub aplikacji
        Locale locale = Locale.getDefault();

        // pobierz nazwę miesiąca w mianowniku (dla PL) lub normalną (dla innych języków)
        String month = date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, locale);

        return month.toUpperCase() + " " + date.getYear();
    }

    public static String formatDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = mondayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    public static Integer getIndexInWeekArray(LocalDate selectedDate, ArrayList<LocalDate> week){
        for (int i=0 ; i<week.size(); i++){
            if (week.get(i).equals(selectedDate)){
                return i;
            }
        }
        return -1;
    }

    public static LocalDate mondayForDate(LocalDate current)
    {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.MONDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }

    public static String getDayName(LocalDate date, Context context) {
        String[] dayNames = context.getResources().getStringArray(R.array.days_shortnames);
        int index = date.getDayOfWeek().getValue() - 1; // MONDAY=1 → indeks 0 w tablicy
        return dayNames[index];
    }
}
