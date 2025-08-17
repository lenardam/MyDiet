package com.lenardam.mydiet.dbmodel.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.dbmodel.Meals;

import java.util.List;

@Dao
public interface MealsDao {

    @Insert
    void insert(Meals meals);

    @Update
    void update(Meals meals);

    @Delete
    void delete(Meals meals);

    @Query("SELECT * FROM meals ORDER BY mealId DESC")
    LiveData<List<Meals>> getAllMeals();

    @Query("SELECT * FROM meals WHERE dietPlanId = :dietPlanId")
    LiveData<List<Meals>> getMealsByDietPlanId(int dietPlanId);
}
