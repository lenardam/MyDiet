package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Diet implements Serializable {

    private ArrayList<Recipe> all_recipes;
    private ArrayList<DietPlan> diet_plan;
    private Integer number_of_meals_for_diet;
    private ArrayList<String> all_tags;
    private ShoppingList shopping_list;

    public Diet() {
        this.all_recipes = new ArrayList<Recipe>();
        this.diet_plan = new ArrayList<DietPlan>();
        this.all_tags = new ArrayList<String>();
        this.number_of_meals_for_diet = 3;
    }

    public Diet(ArrayList<Recipe> all_recipes, ArrayList<DietPlan> diet_plan, Integer number_of_meals_for_diet, ArrayList<String> all_tags) {
        if (all_recipes != null) {
            this.all_recipes = all_recipes;
        }
        else {
            this.all_recipes = new ArrayList<Recipe>();
        }

        if (diet_plan != null) {
            this.diet_plan = diet_plan;
        }
        else {
            this.diet_plan = new ArrayList<DietPlan>();
        }

        if (all_tags != null) {
            this.all_tags = all_tags;
        }
        else {
            this.all_tags = new ArrayList<String>();
        }

        this.number_of_meals_for_diet = number_of_meals_for_diet;
    }

    public ArrayList<Recipe> getAll_recipes() {
        return all_recipes;
    }

    public void setAll_recipes(ArrayList<Recipe> all_recipes) {
        this.all_recipes = all_recipes;
    }

    public ArrayList<DietPlan> getDiet_plan() {
        return diet_plan;
    }

    public void set_diet_plan(ArrayList<DietPlan> diet_plan) {
        this.diet_plan = diet_plan;
    }

    public Integer getNumber_of_meals_for_diet() {
        return number_of_meals_for_diet;
    }

    public void setNumber_of_meals_for_diet(Integer number_of_meals_for_diet) {
        this.number_of_meals_for_diet = number_of_meals_for_diet;
    }

    public ArrayList<String> getAll_tags() {
        return all_tags;
    }

    public void setAll_tags(ArrayList<String> all_tags) {
        this.all_tags = all_tags;
    }

    public ShoppingList getShopping_list() {
        return shopping_list;
    }

    public void setShopping_list(ShoppingList shopping_list) {
        this.shopping_list = shopping_list;
    }

    public void init_diet_plan(Date date) {
        Date last_day_of_diet;
        if (diet_plan.isEmpty()) {
            // Jeśli lista jest pusta, nie ma daty do porównania, bierzemy currentdate
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            last_day_of_diet = cal.getTime();
        }
        else {
            // Pobranie ostatniego dnia z planu diety
            last_day_of_diet = diet_plan.get(diet_plan.size() - 1).getDiet_plan_date();
        }



        // Reset czasu w obu datach, aby porównywać tylko daty
        Date normalizedLastDay = resetTime(last_day_of_diet);
        Date normalizedInputDate = resetTime(date);

        // Jeśli podana data jest późniejsza niż ostatni dzień diety
        if (normalizedInputDate.after(normalizedLastDay)) {
            // Dodawanie dni do planu diety
            Calendar cal = Calendar.getInstance();
            cal.setTime(normalizedLastDay);

            // Przejdź do następnego dnia po ostatnim dniu diety
            cal.add(Calendar.DAY_OF_MONTH, 1);

            while (!cal.getTime().after(date)) {
                Date currentDay = cal.getTime();
                diet_plan.add(new DietPlan(currentDay, number_of_meals_for_diet, null));
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    }

    // Metoda resetująca czas w obiekcie Date
    private Date resetTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
