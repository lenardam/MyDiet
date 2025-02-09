package com.lenardam.mydiet.adapters;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>  {

    private ArrayList<Recipe> recipes;
    private OnRecipeClickListener listener;
    private boolean canEdit;
    private RecipeTagAdapter recipeTagListAdapter;
    private int selectedRecipePosition = -1;

    public interface OnRecipeClickListener {
        void onRecipeClick(int position);
        void onRecipeLongClick(int position, View v);
        void onRecipeDeleteClick(int position);
    }

    public RecipeListAdapter(ArrayList<Recipe> recipes, OnRecipeClickListener listener, boolean canEdit) {
        this.recipes = recipes;
        this.listener = listener;
        this.canEdit = canEdit;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recipeTagRecyclerView;
        TextView recipeNameTextView;
        TextView caloriesAmountTextView;
        TextView proteinAmountTextView;
        TextView fatAmountTextView;
        TextView carvsAmountTextView;
        TextView carbsLabelTextView;
        TextView fatLabelTextView;
        TextView proteinLabelTextView;
        ImageButton recieDeleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_recipe_name);
            caloriesAmountTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_calories_amount);
            proteinAmountTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_protein_amount);
            fatAmountTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_fat_amount);
            carvsAmountTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_carbs_amount);
            carbsLabelTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_carbs_label);
            fatLabelTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_fat_label);
            proteinLabelTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_protein_label);
            recieDeleteButton = (ImageButton) itemView.findViewById(R.id.it_recipe_btn_delete_recipe);
            recipeTagRecyclerView = (RecyclerView) itemView.findViewById(R.id.it_recipe_rv_recipe_tag);
        }

        public void bind(OnRecipeClickListener listener, int position, boolean canEdit) {
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRecipeClick(position);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null && canEdit) {
                    this.recieDeleteButton.setVisibility(View.VISIBLE);
                }
                return true;
            });

            recieDeleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Usuń posiłek")
                        .setMessage("Czy na pewno chcesz usunąć ten przepis?")
                        .setPositiveButton("Tak", (dialog, which) -> {
                            if (listener != null) {
                                listener.onRecipeDeleteClick(position);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        if (selectedRecipePosition == position) {
            holder.itemView.setBackgroundResource(R.color.colorSecondary);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        String recipeName = recipe.getName();
        String caloriesAmount = String.valueOf(recipe.getCaloriesAmount()) + " kcal";
        String proteinAmount = String.valueOf(recipe.getProteinAmount()) + " g";
        String fatAmount = String.valueOf(recipe.getFatAmount()) + " g";
        String carbsAmount = String.valueOf(recipe.getCarbsAmount()) + " g";

        holder.recipeNameTextView.setText(recipeName);
        holder.caloriesAmountTextView.setText(caloriesAmount);
        holder.proteinAmountTextView.setText(proteinAmount);
        holder.fatAmountTextView.setText(fatAmount);
        holder.carvsAmountTextView.setText(carbsAmount);

        holder.recieDeleteButton.setVisibility(View.INVISIBLE);
        holder.recipeTagRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeTagListAdapter = new RecipeTagAdapter(recipe.getTags(), null, false);
        holder.recipeTagRecyclerView.setAdapter(recipeTagListAdapter);

        holder.bind(listener, position, canEdit);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    // Zaznacza element
    public void setSelectedItem(int position) {
        notifyItemChanged(selectedRecipePosition);
        selectedRecipePosition = position;
        notifyItemChanged(position);
    }

}