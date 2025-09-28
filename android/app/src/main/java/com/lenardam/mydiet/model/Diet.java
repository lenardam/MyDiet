package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Diet implements Serializable {

    private DietSettings dietSettings;

    public Diet() {
        this.dietSettings = new DietSettings();
    }

    public DietSettings getDietSettings() {
        return dietSettings;
    }

}
