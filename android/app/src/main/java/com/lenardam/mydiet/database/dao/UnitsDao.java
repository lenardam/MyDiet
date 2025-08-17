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
    void insert(Units unit);

    @Update
    void update(Units unit);

    @Delete
    void delete(Units unit);

    @Query("SELECT * FROM units")
    LiveData<List<Units>> getAllUnits();

}
