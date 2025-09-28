package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.MealsDao;
import com.lenardam.mydiet.database.model.MealFullData;
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.model.RecipeFullData;
import com.lenardam.mydiet.database.model.Recipes;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsRepository {

    private MyDietDatabase database;
    private MealsDao mealsDao;
    private LiveData<List<Meals>> allMeals;
    private LiveData<List<MealFullData>> allMealsFullData;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MealsRepository(Application application) {
        database = MyDietDatabase.getInstance(application);
        mealsDao = database.mealsDao();
        allMeals = mealsDao.getAllMeals();
        allMealsFullData = mealsDao.getMealsFullData();
    }

    public void insert(Meals meals) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mealsDao.insert(meals);
            }
        });

    }

    public void update(Meals meals) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mealsDao.update(meals);
            }
        });
    }

    public void delete(Meals meals) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mealsDao.delete(meals);
            }
        });

    }

    public LiveData<List<Meals>> getAllMeals() {
        return allMeals;
    }

    public LiveData<List<Meals>> getMealsByDietPlanId(Long dietPlanId) {
        return mealsDao.getMealsByDietPlanId(dietPlanId);
    }

    public LiveData<List<MealFullData>> getMealsFullDataByDietPlanId(Long dietPlanId) {
        return mealsDao.getMealsFullDataByDietPlanId(dietPlanId);
    }

    public LiveData<Meals> getMealById(Long id) {
        return mealsDao.getMealById(id);
    }

    public LiveData<MealFullData> getMealFullDataByMealId(long mealId) {
        return mealsDao.getMealWithDetails(mealId);
    }

    public LiveData<List<MealFullData>> getMealsFullData() {
        return allMealsFullData;
    }

}
