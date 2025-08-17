package com.lenardam.mydiet.dbmodel.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.dbmodel.DietPlans;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface DietPlansDao {

    @Insert
    void insert(DietPlans dietPlan);

    @Update
    void update(DietPlans dietPlan);

    @Delete
    void delete(DietPlans dietPlan);

    @Query("SELECT * FROM diet_plans ORDER BY date DESC")
    LiveData<List<DietPlans>> getAllDietPlans();

    @Query("SELECT * FROM diet_plans WHERE date = :date")
    LiveData<DietPlans> getDietPlanByDate(LocalDate date);

}
