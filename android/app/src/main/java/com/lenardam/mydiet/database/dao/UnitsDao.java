package com.lenardam.mydiet.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.database.model.Units;

import java.util.List;

@Dao
public interface UnitsDao {

    @Insert
    Long insert(Units unit);

    @Update
    void update(Units unit);

    @Delete
    void delete(Units unit);

    @Query("SELECT * FROM units")
    LiveData<List<Units>> getAllUnits();

    @Query("SELECT * FROM units WHERE unitId = :id LIMIT 1")
    Units getUnitById(Long id);

    @Query("SELECT * FROM units WHERE name = :name LIMIT 1")
    Units getUnitByName(String name);


}
