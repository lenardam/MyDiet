package com.lenardam.mydiet.utils;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.Diet;
import com.lenardam.mydiet.model.Recipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa zapisująca stan aplikacji w pamięci wewnętrznej urządzenia
 */

public class SharedPreferencesSaver
{
    private static final String MYDIET_PREF = "MYDIET_PREF";

    public static void saveRecipesToFile(Context context, Uri uri, ArrayList<Recipe> recipes) {
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            if (outputStream != null) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(recipes);
                outputStream.write(json.getBytes());
                outputStream.close();
                Toast.makeText(context, R.string.save_recipes_to_file_positive_message, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.save_recipes_to_file_negative_message, Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<Recipe> loadRecipesFromFile(Context context,Uri uri) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gson = new Gson();
                Recipe[] recipesArray = gson.fromJson(reader, Recipe[].class);
                recipes = new ArrayList<Recipe>(Arrays.asList(recipesArray));
                Toast.makeText(context, R.string.load_recipes_from_file_positive_message, Toast.LENGTH_SHORT).show();
                reader.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.load_recipes_from_file_negative_message, Toast.LENGTH_SHORT).show();
        }
        return recipes;
    }


    public static void saveDietToSharedPreferences(Diet myDiet, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();

        // Tworzymy Gson z zarejestrowanym LocalDateTypeAdapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()) // Rejestracja adaptera dla LocalDate
                .create();

        String myDietJson = gson.toJson(myDiet); // Serializacja obiektu Diet
        editor.putString(MYDIET_PREF, myDietJson);
        editor.apply();
    }

    public static Diet loadDietFromSharedPreferences(SharedPreferences preferences) {
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