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
        TextView rv_recipe_name;
        TextView rv_caloriesAmountTextView;
        TextView rv_proteinAmountTextView;
        TextView rv_fatAmountTextView;
        TextView rv_carvsAmountTextView;
        TextView carbsLabel;
        TextView fatLabel;
        TextView proteinLabel;
        ImageButton mealReplaceButton;
        ImageButton mealDeleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_recipe_name = (TextView) itemView.findViewById(R.id.rv_recipe_name);
            rv_caloriesAmountTextView = (TextView) itemView.findViewById(R.id.rv_caloriesAmountTextView);
            rv_proteinAmountTextView = (TextView) itemView.findViewById(R.id.rv_proteinAmountTextView);
            rv_fatAmountTextView = (TextView) itemView.findViewById(R.id.rv_fatAmountTextView);
            rv_carvsAmountTextView = (TextView) itemView.findViewById(R.id.rv_carvsAmountTextView);
            carbsLabel = (TextView) itemView.findViewById(R.id.carbsLabel);
            fatLabel = (TextView) itemView.findViewById(R.id.fatLabel);
            proteinLabel = (TextView) itemView.findViewById(R.id.proteinLabel);
            mealReplaceButton = (ImageButton) itemView.findViewById(R.id.mealReplaceButton);
            mealDeleteButton = (ImageButton) itemView.findViewById(R.id.recipeDeleteButton);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        String recipe_name = "";
        String calories_amount = "";
        String protein_amount = "";
        String fat_amount = "";
        String carbs_amount = "";

        if (meal.getRecipe() == null){
            recipe_name = "WYBIERZ PRZEPIS";
            holder.rv_caloriesAmountTextView.setVisibility(View.INVISIBLE);
            holder.rv_proteinAmountTextView.setVisibility(View.INVISIBLE);
            holder.rv_fatAmountTextView.setVisibility(View.INVISIBLE);
            holder.rv_carvsAmountTextView.setVisibility(View.INVISIBLE);
            holder.carbsLabel.setVisibility(View.INVISIBLE);
            holder.fatLabel.setVisibility(View.INVISIBLE);
            holder.proteinLabel.setVisibility(View.INVISIBLE);
        }
        else {
            recipe_name = meal.getRecipe().getName();
            calories_amount = String.valueOf(meal.getRecipe().getCalories_amount()) + " kcal";
            protein_amount = String.valueOf(meal.getRecipe().getProtein_amount()) + " g";
            fat_amount = String.valueOf(meal.getRecipe().getFat_amount()) + " g";
            carbs_amount = String.valueOf(meal.getRecipe().getCarbs_amount()) + " g";
        }
        holder.rv_recipe_name.setText(recipe_name);
        holder.rv_caloriesAmountTextView.setText(calories_amount);
        holder.rv_proteinAmountTextView.setText(protein_amount);
        holder.rv_fatAmountTextView.setText(fat_amount);
        holder.rv_carvsAmountTextView.setText(carbs_amount);
        holder.mealReplaceButton.setVisibility(View.INVISIBLE);
        holder.mealDeleteButton.setVisibility(View.INVISIBLE);

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
}