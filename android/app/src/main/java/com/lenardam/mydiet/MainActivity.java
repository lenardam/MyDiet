package com.lenardam.mydiet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private Button recipes_button;
    private Button shopping_list_button;
    private Button settings_button;
    private BottomNavigationView navigationView;


    /*
    Metoda wywoływana przy starcie aplikacji
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

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
    }

    /*
    Metoda wywoływana przy zatrzymaniu aplikacji
    */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /*
    Inicjalizacja widoku aplikacji
    */
    private void initViews() {

        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recipes_button = (Button) findViewById(R.id.recipes_button);
        shopping_list_button = (Button) findViewById(R.id.shopping_list_button);
        settings_button = (Button) findViewById(R.id.settings_button);
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.Home);

        navigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.Home) {
                    return true;
                }
                else if (item.getItemId() == R.id.Recipes) {
                    Intent intent = new Intent(MainActivity.this, RecipesListActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.Shopping_List) {
                    Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.Settings) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
                }
                else return false;
        });

        recipes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipesListActivity.class);
                startActivity(intent);
            }
        });

        shopping_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent);
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}