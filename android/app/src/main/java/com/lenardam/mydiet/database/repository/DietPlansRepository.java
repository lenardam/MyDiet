package com.lenardam.mydiet.database.repository;

import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.dao.DietPlansDao;
import com.lenardam.mydiet.database.model.DietPlans;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DietPlansRepository {

    private DietPlansDao dietPlansDao;
    private LiveData<List<DietPlans>> allDietPlans;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DietPlansRepository(DietPlansDao dietPlansDao) {
        this.dietPlansDao = dietPlansDao;
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

    public LiveData<List<DietPlans>> getAllDietPlans() {
        return allDietPlans;
    }

    public LiveData<DietPlans> getDietPlanByDate(LocalDate date) {
        return dietPlansDao.getDietPlanByDate(date);
    }


}
