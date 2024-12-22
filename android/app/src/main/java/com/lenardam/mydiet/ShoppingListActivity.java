package com.lenardam.mydiet;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShoppingListActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;

    /*
    Metoda wywoływana przy starcie aplikacji
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.shopping_list_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.Shopping_List);

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                Intent intent = new Intent(ShoppingListActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.Recipes) {
                Intent intent = new Intent(ShoppingListActivity.this, RecipesListActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.Shopping_List) {
                return true;
            }
            else if (item.getItemId() == R.id.Settings) {
                Intent intent = new Intent(ShoppingListActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            else return false;
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
        navigationView.setSelectedItemId(R.id.Shopping_List);
    }

    /*
    Metoda wywoływana przy zatrzymaniu aplikacji
    */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}