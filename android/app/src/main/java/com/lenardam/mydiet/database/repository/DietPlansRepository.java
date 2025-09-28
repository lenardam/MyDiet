package com.lenardam.mydiet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.MyDietDatabase;
import com.lenardam.mydiet.database.dao.DietPlansDao;
import com.lenardam.mydiet.database.dao.MealsDao;
import com.lenardam.mydiet.database.model.DietPlanFullData;
import com.lenardam.mydiet.database.model.DietPlans;
import com.lenardam.mydiet.database.model.Meals;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DietPlansRepository {

    private MyDietDatabase database;
    private DietPlansDao dietPlansDao;
    private MealsDao mealsDao;
    private LiveData<List<DietPlanFullData>> allDietPlans;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DietPlansRepository(Application application) {
        database = MyDietDatabase.getInstance(application);
        dietPlansDao = database.dietPlansDao();
        mealsDao = database.mealsDao();
        allDietPlans = dietPlansDao.getAllDietPlans();
    }

    public void insert(DietPlans dietPlan) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dietPlansDao.insert(dietPlan);
            }
        });

    }

    public void update(DietPlans dietPlan) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dietPlansDao.update(dietPlan);
            }
        });

    }

    public void delete(DietPlans dietPlan) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dietPlansDao.delete(dietPlan);
            }
        });
    }

    public LiveData<List<DietPlanFullData>> getAllDietPlans() {
        return allDietPlans;
    }

    public LiveData<DietPlans> getDietPlanByDate(LocalDate date) {
        return dietPlansDao.getDietPlanByDate(date);
    }

    public void insertWithMeals(DietPlans dietPlan, List<Meals> meals) {
        executorService.execute(() -> {
            // Room nie pozwala na @Transaction między różnymi DAO, ale możesz użyć transakcji bazy ręcznie
            database.runInTransaction(() -> {
                Long dietPlanId = dietPlansDao.insert(dietPlan);

                for (Meals meal : meals) {
                    meal.setDietPlanId(dietPlanId);
                    mealsDao.insert(meal);
                }
            });
        });
    }


}
