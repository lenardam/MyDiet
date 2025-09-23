package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lenardam.mydiet.database.model.DietPlanFullData;
import com.lenardam.mydiet.database.model.DietPlans;
import com.lenardam.mydiet.database.model.MealFullData;
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.repository.DietPlansRepository;
import com.lenardam.mydiet.database.repository.MealsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DietPlansViewModel extends AndroidViewModel {

    private DietPlansRepository dietPlansRepository;
    private MealsRepository mealsRepository;
    private LiveData<List<DietPlanFullData>> allDietPlans;

    public DietPlansViewModel(@NonNull Application application) {
        super(application);

        dietPlansRepository = new DietPlansRepository(application);
        mealsRepository = new MealsRepository(application);
        allDietPlans = dietPlansRepository.getAllDietPlans();
    }

    public void insert(DietPlans dietPlan) {
        dietPlansRepository.insert(dietPlan);
    }

    public void update(DietPlans dietPlan) {
        dietPlansRepository.update(dietPlan);
    }

    public void delete(DietPlans dietPlan) {
        dietPlansRepository.delete(dietPlan);
    }

    public LiveData<List<DietPlanFullData>> getAllDietPlans() {
        return allDietPlans;
    }

    public LiveData<DietPlans> getDietPlanByDate(LocalDate date) {
        return dietPlansRepository.getDietPlanByDate(date);
    }

    // metoda do wstawienia DietPlan z Meals
    public void insertWithMeals(DietPlans dietPlan, List<Meals> meals) {
        dietPlansRepository.insertWithMeals(dietPlan, meals);
    }

}
