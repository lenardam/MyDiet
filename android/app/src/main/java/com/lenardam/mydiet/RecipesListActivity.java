package com.lenardam.mydiet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.adapters.IngredientAdapter;
import com.lenardam.mydiet.adapters.RecipesAdapter;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

public class RecipesListActivity extends AppCompatActivity {

    private ArrayList<Recipe> all_recipes;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private RecipesAdapter recipes_adapter;
    private RecyclerView recipes_recycle_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        all_recipes = new ArrayList<Recipe>();
        initViews();
        initRecycleView();

    }

    private void initRecycleView() {
        recipes_recycle_view = findViewById(R.id.recipes_recycle_view);
        recipes_adapter = new RecipesAdapter(all_recipes);
        recipes_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        recipes_recycle_view.setAdapter(recipes_adapter);
    }

    private void initViews() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.recipes_list_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Button add_new_recipe = (Button) findViewById(R.id.add_new_recipe);
        add_new_recipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RecipesListActivity.this, NewRecipeActivity.class);
                        activityResultLauncher.launch(intent);
                    }
                }
        );

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {

                            Recipe new_recipe = (Recipe) data.getExtras().get(NewRecipeActivity.NEW_RECIPE_TAG);
                            if (new_recipe != null)
                            {
                                all_recipes.add(new_recipe);
                                recipes_adapter.notifyDataSetChanged();
                            }
                            Toast.makeText(this, "Przepis został dodany", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
        );
    }
}