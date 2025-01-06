package com.lenardam.mydiet.utils;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lenardam.mydiet.model.Diet;

/**
 * Klasa zapisująca stan aplikacji w pamięci wewnętrznej urządzenia
 */

public class SharedPreferencesSaver
{
    private static final String MYDIET_PREF = "MYDIET_PREF";

    public static void saveTo(Diet myDiet, SharedPreferences preferences)
    {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        editor.putString(MYDIET_PREF, gson.toJson(myDiet));
        editor.apply();
    }

    public static Diet loadFrom(SharedPreferences preferences)
    {
        // Pobierz zapisany JSON ze SharedPreferences
        String json = preferences.getString(MYDIET_PREF, null);

        // Sprawdź, czy JSON istnieje
        if (json == null || json.isEmpty()) {
            return null; // Zwróć null, jeśli brak danych
        }

        // Inicjalizacja Gson i deserializacja
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, Diet.class); // Deserializacja obiektu Diet
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Zwróć null, jeśli deserializacja się nie powiedzie
        }
    }
}