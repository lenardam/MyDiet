package com.lenardam.mydiet.utils;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lenardam.mydiet.model.Diet;

import java.time.LocalDate;

/**
 * Klasa zapisująca stan aplikacji w pamięci wewnętrznej urządzenia
 */

public class SharedPreferencesSaverNew
{
    private static final String MYDIET_PREF = "MYDIET_PREF";

    public static void saveTo(Diet myDiet, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();

        // Tworzymy Gson z zarejestrowanym LocalDateTypeAdapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()) // Rejestracja adaptera dla LocalDate
                .create();

        String myDietJson = gson.toJson(myDiet); // Serializacja obiektu Diet
        editor.putString(MYDIET_PREF, myDietJson);
        editor.apply();
    }

    public static Diet loadFrom(SharedPreferences preferences) {
        // Pobierz zapisany JSON
        String json = preferences.getString(MYDIET_PREF, null);

        if (json == null || json.isEmpty()) {
            return null; // Jeśli brak danych
        }

        // Tworzymy Gson z zarejestrowanym LocalDateTypeAdapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()) // Rejestracja adaptera dla LocalDate
                .create();

        // Deserializacja obiektu Diet
        return gson.fromJson(json, Diet.class);
    }
}