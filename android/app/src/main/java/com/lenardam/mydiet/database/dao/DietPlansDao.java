package com.lenardam.mydiet.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.lenardam.mydiet.database.model.DietPlanFullData;
import com.lenardam.mydiet.database.model.DietPlans;
import com.lenardam.mydiet.database.model.Meals;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface DietPlansDao {

    @Insert
    long insert(DietPlans dietPlan);

    @Update
    void update(DietPlans dietPlan);

    @Delete
    void delete(DietPlans dietPlan);

    @Query("SELECT * FROM diet_plans ORDER BY date DESC")
    LiveData<List<DietPlanFullData>> getAllDietPlans();

    @Query("SELECT * FROM diet_plans WHERE date = :date")
    LiveData<DietPlans> getDietPlanByDate(LocalDate date);

}

