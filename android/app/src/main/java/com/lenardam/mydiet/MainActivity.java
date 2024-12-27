package com.lenardam.mydiet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.lenardam.mydiet.model.Diet;
import com.lenardam.mydiet.model.Recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String MY_DIET_TAG = "MY_DIET_TAG";
    public static final String FRAGMENT_RESULT_KEY_TAG = "FRAGMENT_RESULT_KEY_TAG";


    private BottomNavigationView navigationView;
    private Diet myDiet;

    /*
    Metoda wywoływana przy starcie aplikacji
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDiet(savedInstanceState);
        initViews(savedInstanceState);

        // Rejestracja nasłuchiwacza wyników z fragmentów
        getSupportFragmentManager().setFragmentResultListener(FRAGMENT_RESULT_KEY_TAG, this, (requestKey, result) -> {
            // Odbieramy Bundle
            if (result != null) {
                // Pobieramy dane z Bundle
                ArrayList<Recipe> all_recipes = (ArrayList<Recipe>) result.getSerializable(RecipesListFragment.CHANGED_RECIPES_LIST_TAG);

                if (all_recipes != null)
                {
                    myDiet.setAll_recipes(all_recipes);
                }
            }

        });
    }

    /*
    Metoda wywoływana przy zatrzymaniu aplikacji
    */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /*
    Metoda wywoływana przy wznowieniu aplikacji
    */
    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setSelectedItemId(R.id.Home);
    }

    /*
    Metoda wywoływana przy zatrzymaniu aplikacji
    */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("MY_DIET_TAG", myDiet);
    }

    /*
    Inicjalizacja danych dotyczących diety
     */
    private void initDiet(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date date30DaysAhead = cal.getTime();

        if (savedInstanceState != null) {
            myDiet = (Diet) savedInstanceState.getSerializable("MY_DIET_TAG");
            myDiet.init_diet_plan(date30DaysAhead);
        }
        else {
            myDiet = new Diet();
            myDiet.init_diet_plan(date30DaysAhead);
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
            bundle.putSerializable(DietFragment.DIET_PLAN_TAG, myDiet.getDiet_plan());
            Fragment selectedFragment = new DietFragment();
            selectedFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, selectedFragment)
                    .commit();
        }

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.Home);

        navigationView.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.Home) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(DietFragment.DIET_PLAN_TAG, myDiet.getDiet_plan());
                selectedFragment = new DietFragment();
                selectedFragment.setArguments(bundle);
            }
            else if (item.getItemId() == R.id.Recipes) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(RecipesListFragment.RECIPES_LIST_TAG, myDiet.getAll_recipes());
                selectedFragment = new RecipesListFragment();
                selectedFragment.setArguments(bundle);
            }
            else if (item.getItemId() == R.id.Shopping_List) {
                selectedFragment = new ShoppingListFragment();
            }
            else if (item.getItemId() == R.id.Settings) {
                selectedFragment = new SettingsFragment();
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
}