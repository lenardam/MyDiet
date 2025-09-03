package com.lenardam.mydiet.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lenardam.mydiet.database.model.Tags;

import java.util.List;

@Dao
public interface TagsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Tags tag);

    @Update
    void update(Tags tag);

    @Delete
    void delete(Tags tag);

    @Query("SELECT * FROM tags")
    LiveData<List<Tags>> getAllTags();

    @Query("SELECT * FROM tags WHERE name = :name")
    Tags getTagByName(String name);

}
