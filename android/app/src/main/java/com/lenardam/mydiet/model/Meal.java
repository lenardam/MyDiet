package com.lenardam.mydiet.model;

import java.io.Serializable;

public class Meal implements Serializable {
    private Recipe recipe;
    private Double portion_of_recipe;
    private Boolean is_eaten;
}
