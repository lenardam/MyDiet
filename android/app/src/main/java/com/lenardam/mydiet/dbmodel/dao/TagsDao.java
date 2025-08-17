package com.lenardam.mydiet.dbmodel.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.dbmodel.Tags;

import java.util.List;

@Dao
public interface TagsDao {

    @Insert
    void insert(Tags tag);

    @Update
    void update(Tags tag);

    @Delete
    void delete(Tags tag);

    @Query("SELECT * FROM tags")
    LiveData<List<Tags>> getAllTags();

}
