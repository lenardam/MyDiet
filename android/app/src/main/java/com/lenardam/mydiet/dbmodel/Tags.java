package com.lenardam.mydiet.dbmodel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tags")
public class Tags {

    @PrimaryKey(autoGenerate = true)
    private int tagId;

    private String name;

    public Tags(String name) {
        this.name = name;
    }

    public int getTagId() {
        return tagId;
    }

    public String getName() {
        return name;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
