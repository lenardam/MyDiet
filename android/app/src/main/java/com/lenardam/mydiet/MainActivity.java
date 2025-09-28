package com.lenardam.mydiet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lenardam.mydiet.model.Diet;
import com.lenardam.mydiet.utils.SharedPreferencesSaver;

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
    }

    /*
    Wywoływana przy zatrzymaniu aplikacji
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /*
    Metoda wywoływana przy wznowieniu aplikacji
    */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
    Metoda wywoływana przy zatrzymaniu aplikacji
    */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MY_DIET_TAG, myDiet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_top_item_settings_fragment) {

            Fragment selectedFragment = new SettingsFragment();

            // Zamień fragment w FragmentContainerView
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.act_main_fragment_container_view, selectedFragment)
                        .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                        .commit();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    Inicjalizacja danych dotyczących diety
     */
    private void initDiet(Bundle savedInstanceState) {
        //pierw ładujemy z savedInstanceState
        if (savedInstanceState != null) {
            myDiet = (Diet) savedInstanceState.getSerializable(MY_DIET_TAG);
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
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Inicjalizacja Top App Bar jako ActionBar
        MaterialToolbar topAppBar = findViewById(R.id.act_main_top_app_bar);
        setSupportActionBar(topAppBar);

        // Domyślny fragment z danymi
        Bundle bundle = new Bundle();
        Fragment defaultFragment = new DietFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.act_main_fragment_container_view, defaultFragment)
                .commit();

        navigationView = findViewById(R.id.act_main_bottom_navigation_view);
        navigationView.setSelectedItemId(R.id.menu_bottom_item_diet_fragment);

        navigationView.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.menu_bottom_item_diet_fragment) {
                selectedFragment = new DietFragment();
            }
            else if (item.getItemId() == R.id.menu_bottom_item_recipe_list_fragment) {
                selectedFragment = new RecipesListFragment();
            }
            else if (item.getItemId() == R.id.menu_bottom_item_shopping_list_fragment) {
                selectedFragment = new ShoppingListFragment();
            }


            // Usuwanie wszystkich fragmentów z back stack, aby uniknąć nakładania
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // Zamień fragment w FragmentContainerView
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.act_main_fragment_container_view, selectedFragment)
                        .addToBackStack(null) // Dodajemy do back stack, by móc wrócić
                        .commit();
            }
            return true;

        });
        
    }

    public void loadDiet(){
        SharedPreferences preferences = getSharedPreferences(MY_DIET_SHARED_PREFERENCES_TAG, MODE_PRIVATE);
        myDiet = SharedPreferencesSaver.loadDietFromSharedPreferences(preferences);
    }

    public void setBottomNavigationItem(int itemId) {
        navigationView.getMenu().findItem(itemId).setChecked(true);
    }
}