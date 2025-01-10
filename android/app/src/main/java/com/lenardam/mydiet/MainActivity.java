package com.lenardam.mydiet;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lenardam.mydiet.model.Diet;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.ShoppingList;
import com.lenardam.mydiet.utils.SharedPreferencesSaver;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MY_DIET_TAG = "MY_DIET_TAG";
    private static final String MY_DIET_SHARED_PREFERENCES_TAG = "MY_DIET_SP_TAG";

    private BottomNavigationView navigationView;
    public static Diet myDiet;

    /*
    Metoda wywoływana przy starcie aplikacji
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDiet(savedInstanceState);
        initViews(savedInstanceState);
        initFragmentResultListeners();
    }

    /*
    Wywoływana przy zatrzymaniu aplikacji
     */
    @Override
    protected void onStop() {
        super.onStop();
        saveDiet();
    }

    /*
    Metoda wywoływana przy wznowieniu aplikacji
    */
    @Override
    protected void onResume() {
        super.onResume();
        loadDiet();
        //navigationView.setSelectedItemId(R.id.Home);
    }

    /*
    Metoda wywoływana przy zatrzymaniu aplikacji
    */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveDiet();
        outState.putSerializable("MY_DIET_TAG", myDiet);
    }

    /*
    Inicjalizacja danych dotyczących diety
     */
    private void initDiet(Bundle savedInstanceState) {
        //pierw ładujemy z savedInstanceState
        if (savedInstanceState != null) {
            myDiet = (Diet) savedInstanceState.getSerializable("MY_DIET_TAG");
        }

        //jeżeli nie ma w savedInstanceState, to ładujemy z SharedPreferences
        if (myDiet == null) {
            // Jeśli brak danych w savedInstanceState, ładowanie z SharedPreferences
            loadDiet();
        }

        //jeżeli nie ma w SharedPreferences ani w savedInstanceState, to inicjalizacja nowej instancji
        if (myDiet == null) {
            // Jeśli brak danych w obu źródłach, inicjalizacja nowej instancji
            myDiet = new Diet();
        }
    }

    /*
    Inicjalizacja widoku aplikacji
    */
    private void initViews(Bundle savedInstanceState) {

        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Domyślny fragment z danymi
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            Fragment selectedFragment = new DietFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, selectedFragment)
                    .commit();
        }

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.Home);

        navigationView.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.Home) {
                selectedFragment = new DietFragment();
                saveDiet();
            }
            else if (item.getItemId() == R.id.Recipes) {
                selectedFragment = new RecipesListFragment();
                saveDiet();
            }
            else if (item.getItemId() == R.id.Shopping_List) {
                selectedFragment = new ShoppingListFragment();
                saveDiet();
            }
            else if (item.getItemId() == R.id.Settings) {
                selectedFragment = new SettingsFragment();
                saveDiet();
            }

            // Zamień fragment w FragmentContainerView
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, selectedFragment)
                        .commit();
            }
            return true;

        });
        
    }

    /*
    Inicjalizacja nasłuchiwania wyników z fragmentów
     */
    private void initFragmentResultListeners() {

        // Rejestracja nasłuchiwacza wyników z fragmentu DietFragment
        getSupportFragmentManager().setFragmentResultListener(DietFragment.DIET_CHANGED_DIET_PLAN_TAG, this, (requestKey, result) -> {
            // Odbieramy Bundle
            if (result != null) {
                // Pobieramy dane z Bundle
                ArrayList<DietPlan> diet_plan = (ArrayList<DietPlan>) result.getSerializable(DietFragment.DIET_CHANGED_DIET_PLAN_TAG);

                if (diet_plan != null)
                {
                    saveDiet();
                }
            }

        });

        // Rejestracja nasłuchiwacza wyników z fragmentu ShoppingListFragment
        getSupportFragmentManager().setFragmentResultListener(ShoppingListFragment.SHOPPING_LIST_SELECTED_TAG, this, (requestKey, result) -> {
            // Odbieramy Bundle
            if (result != null) {
                // Pobieramy dane z Bundle
                ShoppingList shopping_list = (ShoppingList) result.getSerializable(ShoppingListFragment.SHOPPING_LIST_SELECTED_TAG);

                if (shopping_list != null)
                {
                    myDiet.setShopping_list(shopping_list);
                    saveDiet();
                }
            }

        });
    }

    /*
    Metoda zapisywania danych dotyczących diety w SharedPreferences
     */
    public void saveDiet() {
        // Pobierz instancję SharedPreferences
        SharedPreferences preferences = getSharedPreferences(MY_DIET_SHARED_PREFERENCES_TAG, MODE_PRIVATE);

        // Zapisz obiekt Diet
        SharedPreferencesSaver.saveTo(myDiet, preferences);
    }

    public void loadDiet(){
        SharedPreferences preferences = getSharedPreferences(MY_DIET_SHARED_PREFERENCES_TAG, MODE_PRIVATE);
        myDiet = SharedPreferencesSaver.loadFrom(preferences);
    }
}