package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.MealsDao;
import com.lenardam.mydiet.database.model.Meals;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsRepository {

    private MealsDao mealsDao;
    private LiveData<List<Meals>> allMeals;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MealsRepository(Application application) {
        MyDietDatabase database = MyDietDatabase.getInstance(application);
        mealsDao = database.mealsDao();
        allMeals = mealsDao.getAllMeals();
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

    public LiveData<Meals> getMealById(Long id) {
        return mealsDao.getMealById(id);
    }

}
