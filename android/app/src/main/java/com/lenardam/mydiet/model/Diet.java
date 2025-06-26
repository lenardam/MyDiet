package com.lenardam.mydiet.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Diet implements Serializable {

    private ArrayList<Recipe> allRecipes;
    private ArrayList<DietPlan> dietPlan;
    private ArrayList<String> allTags;
    private ShoppingList shoppingList;
    private DietSettings dietSettings;

    private static final int ARCHIW_DAYS_IN_DIET_PLAN = 30;

    public Diet() {
        this.allRecipes = new ArrayList<Recipe>();
        this.dietPlan = new ArrayList<DietPlan>();
        this.allTags = new ArrayList<String>();
        this.dietSettings = new DietSettings();
        this.shoppingList = new ShoppingList();
    }

    public Diet(ArrayList<Recipe> allRecipes, ArrayList<DietPlan> dietPlan, ArrayList<String> allTags, DietSettings dietSettings) {
        if (allRecipes != null) {
            this.allRecipes = allRecipes;
        }
        else {
            this.allRecipes = new ArrayList<Recipe>();
        }

        if (dietPlan != null) {
            this.dietPlan = dietPlan;
        }
        else {
            this.dietPlan = new ArrayList<DietPlan>();
        }

        if (allTags != null) {
            this.allTags = allTags;
        }
        else {
            this.allTags = new ArrayList<String>();
        }
        if (dietSettings != null) {
            this.dietSettings = dietSettings;
        }
        else {
            this.dietSettings = new DietSettings();
        }
    }

    public ArrayList<Recipe> getAllRecipes() {
        return allRecipes;
    }

    public void setAllRecipes(ArrayList<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
    }

    public ArrayList<DietPlan> getDietPlan() {
        return dietPlan;
    }

    public void setDietPlan(ArrayList<DietPlan> dietPlan) {
        this.dietPlan = dietPlan;
    }

    public ArrayList<String> getAllTags() {
        return allTags;
    }

    public void setAllTags(ArrayList<String> allTags) {
        this.allTags = allTags;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = new ShoppingList(shoppingList);
    }

    public DietSettings getDietSettings() {
        return dietSettings;
    }

    public void setDietSettings(DietSettings dietSettings) {
        this.dietSettings = dietSettings;
    }

    public DietPlan getDietPlanForDate(LocalDate date) {
        for (int i = 0; i < dietPlan.size(); i++) {
            DietPlan dp = dietPlan.get(i);
            if (dp.getDietPlanDate().equals(date)) {
                return dp;
            }
        }
        return null;
    }

    public ArrayList<Recipe> filterRecipes(String recipeName, ArrayList<String> selectedTags) {
        ArrayList<Recipe> filteredRecipes = new ArrayList<>();

        for (int i = 0; i< allRecipes.size(); i++) {
            Recipe recipe = allRecipes.get(i);

            boolean nameMatches = recipe.getName().toLowerCase().contains(recipeName.toLowerCase());
            boolean tagsMatch = selectedTags.isEmpty() || recipe.getTags().containsAll(selectedTags);

            if (nameMatches && tagsMatch) {
                filteredRecipes.add(recipe);
            }
        }

        return filteredRecipes;
    }

    public void loadRecipe(Recipe recipe) {
        boolean recipeExists = false;
        boolean recipeTagExists = false;
        ArrayList<String> newRecipeTags = recipe.getTags();

        for (int i=0; i<newRecipeTags.size(); i++) {
            recipeTagExists = false;
            for (int j = 0; j< allTags.size(); j++) {
                if (newRecipeTags.get(i).equals(allTags.get(j))) {
                    recipeTagExists = true;
                }
            }
            if (!recipeTagExists) {
                allTags.add(newRecipeTags.get(i));
            }
        }

        //sprawdzamy czy już jest taki przepis
        for (int i = 0; i< allRecipes.size(); i++) {
            if (allRecipes.get(i).getName().equals(recipe.getName())) {
                recipeExists = true;
            }
        }

        //jeżeli nie mamy jeszcze tego przepisu to dodajemy
        if (!recipeExists) {
            allRecipes.add(new Recipe(recipe));
        }
    }

    public void clearOldRecipes(){
        for (int i = dietPlan.size() - 1; i >= 0; i--){
            if (dietPlan.get(i).getDietPlanDate().isBefore(LocalDate.now().minusDays(ARCHIW_DAYS_IN_DIET_PLAN))) {
                dietPlan.remove(i);
            }
        }
    }
}
