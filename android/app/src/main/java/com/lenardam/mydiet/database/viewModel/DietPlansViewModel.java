package com.lenardam.mydiet.database.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lenardam.mydiet.database.model.DietPlans;
import com.lenardam.mydiet.database.repository.DietPlansRepository;

import java.time.LocalDate;
import java.util.List;

public class DietPlansViewModel extends AndroidViewModel {

    private DietPlansRepository dietPlansRepository;
    private LiveData<List<DietPlans>> allDietPlans;

    public DietPlansViewModel(@NonNull Application application) {
        super(application);

        dietPlansRepository = new DietPlansRepository(application);
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

    public LiveData<List<DietPlans>> getAllDietPlans() {
        return allDietPlans;
    }

    public LiveData<DietPlans> getDietPlanByDate(LocalDate date) {
        return dietPlansRepository.getDietPlanByDate(date);
    }

}
