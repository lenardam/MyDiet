package com.lenardam.mydiet.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.Meal;

import java.util.ArrayList;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder> {

    private ArrayList<Meal> meals;
    private OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(int position);

        void onMealEatedClick(int position);
        void onMealReplaceClick(int position);
        void onMealDeleteClick(int position);
    }

    public MealListAdapter(ArrayList<Meal> meals, OnMealClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameTextView;
        TextView caloriesAmountTextView;
        TextView proteinCarbsFatAmountTextView;

        View viewRecipeImageCalories;

        MaterialButton mealEatedButton;
        MaterialButton mealReplaceButton;
        MaterialButton mealDeleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_recipe_name);
            caloriesAmountTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_calories_amount);
            proteinCarbsFatAmountTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_protein_carbs_fat_amount);
            mealEatedButton = (MaterialButton) itemView.findViewById(R.id.it_meal_btn_eated_meal);
            mealReplaceButton = (MaterialButton) itemView.findViewById(R.id.it_meal_btn_replace_meal);
            mealDeleteButton = (MaterialButton) itemView.findViewById(R.id.it_meal_btn_delete_meal);
            viewRecipeImageCalories = itemView.findViewById(R.id.it_meal_layout_recipe_image_calories);
        }

        public void bind(OnMealClickListener listener, int position) {
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealClick(position);
                }
            });

            mealEatedButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealEatedClick(position);
                }
            });


            mealReplaceButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealReplaceClick(position);
                }
            });

            mealDeleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Usuń posiłek")
                        .setMessage("Czy na pewno chcesz usunąć ten posiłek?")
                        .setPositiveButton("Tak", (dialog, which) -> {
                            if (listener != null) {
                                listener.onMealDeleteClick(position);
                            }
                        })
                        .setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss())
                        .show();
            });

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        boolean eated = meal.getIsEaten();
        String recipeName = "";
        String caloriesAmount = "";
        String proteinAmount = "";
        String fatAmount = "";
        String carbsAmount = "";

        //obsługa niewybranego przepisu
        if (meal.getRecipe() == null){
            recipeName = "WYBIERZ PRZEPIS";

            holder.mealReplaceButton.setVisibility(View.INVISIBLE);
            holder.mealEatedButton.setVisibility(View.INVISIBLE);
            holder.mealDeleteButton.setVisibility(View.INVISIBLE);
            holder.viewRecipeImageCalories.setVisibility(View.INVISIBLE);

            holder.mealReplaceButton.setEnabled(false);
            holder.mealDeleteButton.setEnabled(false);
            holder.mealEatedButton.setEnabled(false);
        }
        else {
            recipeName = meal.getRecipe().getName();
            caloriesAmount = String.valueOf(meal.getRecipe().getCaloriesAmount()) + " kcal";
            proteinAmount = String.valueOf(meal.getRecipe().getProteinAmount()) + " g";
            fatAmount = String.valueOf(meal.getRecipe().getFatAmount()) + " g";
            carbsAmount = String.valueOf(meal.getRecipe().getCarbsAmount()) + " g";

            holder.mealReplaceButton.setVisibility(View.VISIBLE);
            holder.mealEatedButton.setVisibility(View.VISIBLE);
            holder.mealDeleteButton.setVisibility(View.VISIBLE);
            holder.viewRecipeImageCalories.setVisibility(View.VISIBLE);

            holder.mealReplaceButton.setEnabled(true);
            holder.mealDeleteButton.setEnabled(true);
            holder.mealEatedButton.setEnabled(true);

            //obsługa zjedzonego przepisu
            if (eated) {
                holder.mealEatedButton.setText("Przywróć");

                holder.mealReplaceButton.setVisibility(View.INVISIBLE);
                holder.mealDeleteButton.setVisibility(View.INVISIBLE);

                holder.mealReplaceButton.setEnabled(false);
                holder.mealDeleteButton.setEnabled(false);

                holder.itemView.setBackgroundResource(R.drawable.background_light_grey_rounded);
                holder.caloriesAmountTextView.setBackgroundResource(R.color.lightGrey);

                holder.viewRecipeImageCalories.setBackgroundResource(R.color.white);

                holder.mealReplaceButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGrey));
                holder.mealDeleteButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGrey));
                holder.mealEatedButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGrey));
            }
            else {
                holder.mealEatedButton.setText("Zjedzone");

                holder.mealReplaceButton.setVisibility(View.VISIBLE);
                holder.mealDeleteButton.setVisibility(View.VISIBLE);

                holder.mealReplaceButton.setEnabled(true);
                holder.mealDeleteButton.setEnabled(true);

                holder.itemView.setBackgroundResource(R.drawable.background_light_green_rounded);
                holder.caloriesAmountTextView.setBackgroundResource(R.color.lightGreen);

                holder.viewRecipeImageCalories.setBackgroundResource(R.color.colorItemInForeground);

                holder.mealReplaceButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
                holder.mealDeleteButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
                holder.mealEatedButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
            }

        }
        holder.recipeNameTextView.setText(recipeName);
        holder.caloriesAmountTextView.setText(caloriesAmount);
        holder.proteinCarbsFatAmountTextView.setText ("B: " + proteinAmount + ",  W: " + carbsAmount + ",  T: " + fatAmount);

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
}