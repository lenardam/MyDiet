package com.lenardam.mydiet.database.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tags",
        indices = {@Index(value = "name", unique = true)})

public class Tags {

    @PrimaryKey(autoGenerate = true)
    private Long tagId;

    private String name;

    public Tags(String name) {
        this.name = name;
    }

    public Long getTagId() {
        return tagId;
    }

    public String getName() {
        return name;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
