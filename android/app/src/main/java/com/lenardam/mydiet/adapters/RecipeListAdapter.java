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
import com.lenardam.mydiet.database.model.Recipes;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>  {

    private List<Recipes> recipes;
    private OnRecipeClickListener listener;
    private boolean canEdit;
    private int selectedRecipePosition = -1;

    public interface OnRecipeClickListener {
        void onRecipeClick(int position);
        void onRecipeLongClick(int position, View v);
        void onRecipeDeleteClick(int position);
    }

//    public RecipeListAdapter(ArrayList<Recipe> recipes, OnRecipeClickListener listener, boolean canEdit) {
//        this.recipes = recipes;
//        this.listener = listener;
//        this.canEdit = canEdit;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View nameDeleteButtonView;
        View recipeImageCaloriesView;
        TextView recipeNameTextView;
        TextView caloriesAmountTextView;
        TextView proteinCarbsFatAmountTextView;
        ImageButton recieDeleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDeleteButtonView = itemView.findViewById(R.id.it_recipe_layout_recipe_name_delete_button);
            recipeImageCaloriesView = itemView.findViewById(R.id.it_recipe_layout_recipe_image_calories);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_recipe_name);
            caloriesAmountTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_calories_amount);
            recieDeleteButton = (ImageButton) itemView.findViewById(R.id.it_recipe_btn_delete_recipe);
            proteinCarbsFatAmountTextView = (TextView) itemView.findViewById(R.id.it_recipe_tv_protein_carbs_fat_amount);
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
                        .setTitle(R.string.alert_dialog_delete_recipe_title)
                        .setMessage(v.getContext().getString(R.string.alert_dialog_delete_recipe_question))
                        .setPositiveButton(R.string.alert_dialog_delete_recipe_positive_button, (dialog, which) -> {
                            if (listener != null) {
                                listener.onRecipeDeleteClick(position);
                            }
                        })
                        .setNegativeButton(R.string.alert_dialog_delete_recipe_negative_button, (dialog, which) -> {
                            dialog.dismiss();
                            this.recieDeleteButton.setVisibility(View.INVISIBLE);
                        })
                        .show();
            });
        }
    }

    public void setRecipes(List<Recipes> recipes){
        this.recipes = recipes;
    }

    public void setOnRecipeClickListener(OnRecipeClickListener listener){
        this.listener = listener;
    }

    public void setCanEdit(boolean canEdit){
        this.canEdit = canEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipes recipe = recipes.get(position);

        if (selectedRecipePosition == position) {
            holder.itemView.setBackgroundResource(R.color.colorSecondary);
            holder.nameDeleteButtonView.setBackgroundResource(R.color.colorPrimary);
            holder.recipeImageCaloriesView.setBackgroundResource(R.color.colorSecondary);
            holder.caloriesAmountTextView.setBackgroundResource(R.color.colorSecondary);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.background_light_green_rounded);
            holder.nameDeleteButtonView.setBackgroundResource(R.drawable.background_green_rounded);
            holder.recipeImageCaloriesView.setBackgroundResource(R.color.colorItemInForeground);
            holder.caloriesAmountTextView.setBackgroundResource(R.color.lightGreen);
        }

        String recipeName = recipe.getName();
        String caloriesAmount = holder.itemView.getContext().getString(R.string.calories_formated_text, recipe.getCaloriesAmount());
        int proteinAmount = recipe.getProteinAmount();
        int fatAmount = recipe.getFatAmount();
        int carbsAmount = recipe.getCarbsAmount();
        String proteinCarbsFatAmountLabel = holder.itemView.getContext().getString(R.string.protein_carbs_fat_amount_formated_text, proteinAmount, carbsAmount, fatAmount);

        holder.recipeNameTextView.setText(recipeName);
        holder.caloriesAmountTextView.setText(caloriesAmount);
        holder.proteinCarbsFatAmountTextView.setText (proteinCarbsFatAmountLabel);

        holder.recieDeleteButton.setVisibility(View.INVISIBLE);

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