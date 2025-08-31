package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.repository.MealsRepository;

import java.util.List;

public class MealsViewModel extends AndroidViewModel {

    private MealsRepository mealsRepository;
    private LiveData<List<Meals>> allMeals;

    public MealsViewModel(@NonNull Application application) {
        super(application);

        mealsRepository = new MealsRepository(application);
        allMeals = mealsRepository.getAllMeals();
    }

    public void insert(Meals meals) {
        mealsRepository.insert(meals);
    }

    public void update(Meals meals) {
        mealsRepository.update(meals);
    }

    public void delete(Meals meals) {
        mealsRepository.delete(meals);
    }

    public LiveData<List<Meals>> getAllMeals() {
        return allMeals;
    }

    public LiveData<List<Meals>> getMealsByDietPlanId(int dietPlanId) {
        return mealsRepository.getMealsByDietPlanId(dietPlanId);
    }
}
