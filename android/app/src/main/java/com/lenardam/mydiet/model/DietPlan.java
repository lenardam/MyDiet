package com.lenardam.mydiet.model;

import java.util.ArrayList;
import java.util.Date;

public class DietPlan {

        private Date diet_plan_date;
        private ArrayList<Recipe> recipes;

        public DietPlan(Date diet_plan_date, ArrayList<Recipe> recipes) {
            this.diet_plan_date = diet_plan_date;
            this.recipes = recipes;
        }

        public Date getDiet_plan_date() {
            return diet_plan_date;
        }

        public void setDiet_plan_date(Date diet_plan_date) {
            this.diet_plan_date = diet_plan_date;
        }

        public ArrayList<Recipe> getRecipes() {
            return recipes;
        }

}
