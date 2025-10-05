package com.lenardam.mydiet.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
import com.lenardam.mydiet.database.utils.Converters;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DietPlans.class, Meals.class, RecipeIngredients.class, RecipeInstructions.class, RecipeTags.class, Recipes.class, ShoppingList.class, Tags.class, Units.class}, version = 6)
@TypeConverters({Converters.class})
public abstract class MyDietDatabase extends RoomDatabase {

    private static MyDietDatabase INSTANCE;
    private static Context appContext;

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
            appContext = context.getApplicationContext();
            INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                    , MyDietDatabase.class, "mydiet_database")
                    .fallbackToDestructiveMigration(true)
                    .addCallback(roomCallback)
                    .setQueryCallback(new RoomDatabase.QueryCallback() {
                        @Override
                        public void onQuery(@NonNull String sqlQuery, @NonNull List<?> bindArgs) {
                            Log.d("RoomQuery", "SQL: " + sqlQuery + " ARGS: " + bindArgs.toString());
                        }
                    }, Executors.newSingleThreadExecutor())
                    .addMigrations(MIGRATION_3_4)
                    .addMigrations(MIGRATION_4_5)
                    .addMigrations(MIGRATION_5_6)
                    .build();
        }

        return INSTANCE;

    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            UnitsDao unitsDao = INSTANCE.unitsDao();
            TagsDao tagsDao = INSTANCE.tagsDao();

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    //Uzupełnienie tabeli units danymi z tablicy stringów z pliku strings.xml
                    String[] units = appContext.getResources().getStringArray(com.lenardam.mydiet.R.array.recipe_units);
                    for (int i = 0; i < units.length; i++) {
                        unitsDao.insert(new Units(units[i]));
                    }

                    String breakfast = appContext.getResources().getString(com.lenardam.mydiet.R.string.breakfast);
                    String lunch = appContext.getResources().getString(com.lenardam.mydiet.R.string.lunch);
                    String dinner = appContext.getResources().getString(com.lenardam.mydiet.R.string.dinner);

                    tagsDao.insert(new Tags(breakfast));
                    tagsDao.insert(new Tags(lunch));
                    tagsDao.insert(new Tags(dinner));

                }
            });

        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {

            //dodanie nowych kolumn
            db.execSQL("ALTER TABLE meals ADD COLUMN mealPosition Integer");

            // Uzupełnienie wartościami rosnącymi
            db.execSQL(
                    "WITH numbered AS (" +
                            "   SELECT mealId, ROW_NUMBER() OVER (PARTITION BY dietPlanId ORDER BY mealId ASC) AS rn " +
                            "   FROM meals" +
                            ") " +
                            "UPDATE meals " +
                            "SET mealPosition = (" +
                            "   SELECT rn FROM numbered WHERE numbered.mealId = meals.mealId" +
                            ")"
            );

            db.execSQL("ALTER TABLE shopping_list ADD COLUMN itemPosition Integer");

            // Uzupełnienie wartościami rosnącymi
            db.execSQL(
                    "WITH numbered AS (" +
                            "   SELECT shoppingListId, ROW_NUMBER() OVER (ORDER BY shoppingListId ASC) AS rn " +
                            "   FROM shopping_list" +
                            ") " +
                            "UPDATE shopping_list " +
                            "SET itemPosition = (" +
                            "   SELECT rn FROM numbered WHERE numbered.shoppingListId = shopping_list.shoppingListId" +
                            ")"
            );



        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {

            // 1. Tworzymy nową tabelę bez unitId, z indeksami
            db.execSQL(
                    "CREATE TABLE shopping_list_new (" +
                            "shoppingListId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "itemName TEXT, " +
                            "itemPosition INTEGER, " +
                            "isBought INTEGER NOT NULL)"
            );

            // 2. Usuwamy starą tabelę
            db.execSQL("DROP TABLE shopping_list");

            // 3. Zmieniamy nazwę nowej tabeli na starą
            db.execSQL("ALTER TABLE shopping_list_new RENAME TO shopping_list");

            // 4. Tworzymy indeksy, których oczekuje Room
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_shopping_list_itemName` ON `shopping_list` (`itemName`)");



        }
    };

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {

            //dodanie nowych kolumn
            db.execSQL("ALTER TABLE meals ADD COLUMN isSkipped Integer not null default 0");

            // Uzupełnienie wartościami rosnącymi
            db.execSQL("UPDATE meals SET isSkipped = 0");

        }
    };


}
