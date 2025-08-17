package com.lenardam.mydiet.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lenardam.mydiet.database.dao.DietPlansDao;
import com.lenardam.mydiet.database.dao.MealsDao;
import com.lenardam.mydiet.database.dao.RecipeIngredientsDao;
import com.lenardam.mydiet.database.dao.RecipeInstructionsDao;
import com.lenardam.mydiet.database.dao.RecipeTagsDao;
import com.lenardam.mydiet.database.dao.RecipesDao;
import com.lenardam.mydiet.database.dao.ShoppingListDao;
import com.lenardam.mydiet.database.dao.TagsDao;
import com.lenardam.mydiet.database.dao.UnitsDao;
import com.lenardam.mydiet.database.model.DietPlans;
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.RecipeInstructions;
import com.lenardam.mydiet.database.model.RecipeTags;
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.database.model.ShoppingList;
import com.lenardam.mydiet.database.model.Tags;
import com.lenardam.mydiet.database.model.Units;

@Database(entities = {DietPlans.class, Meals.class, RecipeIngredients.class, RecipeInstructions.class, RecipeTags.class, Recipes.class, ShoppingList.class, Tags.class, Units.class}, version = 1)
public abstract class MyDietDatabase extends RoomDatabase {

    private static MyDietDatabase INSTANCE;

    public abstract DietPlansDao dietPlansDao();
    public abstract MealsDao mealsDao();
    public abstract RecipeIngredientsDao recipeIngredientsDao();
    public abstract RecipeInstructionsDao recipeInstructionsDao();
    public abstract RecipeTagsDao recipeTagsDao();
    public abstract RecipesDao recipesDao();
    public abstract ShoppingListDao shoppingListDao();
    public abstract TagsDao tagsDao();
    public abstract UnitsDao unitsDao();

    public static synchronized MyDietDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                    , MyDietDatabase.class, "mydiet_database")
                    .fallbackToDestructiveMigration(true)
                    .build();

        }

        return INSTANCE;

    }

}
