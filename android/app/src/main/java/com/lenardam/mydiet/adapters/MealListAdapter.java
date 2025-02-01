package com.lenardam.mydiet.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.Meal;

import java.util.ArrayList;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder> {

    private ArrayList<Meal> meals;
    private OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(int position);
        void onMealLongClick(int position);
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
        TextView proteinAmountTextView;
        TextView fatAmountTextView;
        TextView carbsAmountTextView;
        TextView carbsLabelTextView;
        TextView fatLabelTextView;
        TextView proteinLabelTextView;
        ImageButton mealReplaceButton;
        ImageButton mealDeleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_recipe_name);
            caloriesAmountTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_calories_amount);
            proteinAmountTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_protein_amount);
            fatAmountTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_fat_amount);
            carbsAmountTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_carbs_amount);
            carbsLabelTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_carbs_label);
            fatLabelTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_fat_label);
            proteinLabelTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_protein_label);
            mealReplaceButton = (ImageButton) itemView.findViewById(R.id.it_meal_btn_replace_meal);
            mealDeleteButton = (ImageButton) itemView.findViewById(R.id.it_meal_btn_delete_meal);
        }

        public void bind(OnMealClickListener listener, int position) {
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealClick(position);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    this.mealReplaceButton.setVisibility(View.VISIBLE);
                    this.mealDeleteButton.setVisibility(View.VISIBLE);
                    return true;
                }
                return true;
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
        String recipeName = "";
        String caloriesAmount = "";
        String proteinAmount = "";
        String fatAmount = "";
        String carbsAmount = "";

        if (meal.getRecipe() == null){
            recipeName = "WYBIERZ PRZEPIS";
            holder.caloriesAmountTextView.setVisibility(View.INVISIBLE);
            holder.proteinAmountTextView.setVisibility(View.INVISIBLE);
            holder.fatAmountTextView.setVisibility(View.INVISIBLE);
            holder.carbsAmountTextView.setVisibility(View.INVISIBLE);
            holder.carbsLabelTextView.setVisibility(View.INVISIBLE);
            holder.fatLabelTextView.setVisibility(View.INVISIBLE);
            holder.proteinLabelTextView.setVisibility(View.INVISIBLE);
        }
        else {
            recipeName = meal.getRecipe().getName();
            caloriesAmount = String.valueOf(meal.getRecipe().getCaloriesAmount()) + " kcal";
            proteinAmount = String.valueOf(meal.getRecipe().getProteinAmount()) + " g";
            fatAmount = String.valueOf(meal.getRecipe().getFatAmount()) + " g";
            carbsAmount = String.valueOf(meal.getRecipe().getCarbsAmount()) + " g";
        }
        holder.recipeNameTextView.setText(recipeName);
        holder.caloriesAmountTextView.setText(caloriesAmount);
        holder.proteinAmountTextView.setText(proteinAmount);
        holder.fatAmountTextView.setText(fatAmount);
        holder.carbsAmountTextView.setText(carbsAmount);
        holder.mealReplaceButton.setVisibility(View.INVISIBLE);
        holder.mealDeleteButton.setVisibility(View.INVISIBLE);

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
}