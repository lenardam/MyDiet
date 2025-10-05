package com.lenardam.mydiet.adapters;

import android.app.AlertDialog;
import android.view.HapticFeedbackConstants;
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
import com.lenardam.mydiet.database.model.MealFullData;
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.model.Recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<MealFullData> meals = new ArrayList<>();
    private OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(int position, Meals meal);

        void onMealEatedClick(int position, Meals meal);
        void onMealReplaceClick(int position, Meals meal);
        void onMealRemoveClick(int position, Meals meal);

        void onMealSkipClick(int position, Meals meal);
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
        void onMealAddButtonClick();
    }

    // ---------------------- ViewHolder dla zwykłych elementów ----------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameTextView;
        TextView caloriesAmountTextView;
        TextView proteinCarbsFatAmountTextView;

        View viewRecipeImageCalories;

        MaterialButton mealEatedButton;
        MaterialButton mealReplaceButton;
        MaterialButton mealSkipButton;
        ImageButton moveItemButton;
        ImageButton removeItemButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_recipe_name);
            caloriesAmountTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_calories_amount);
            proteinCarbsFatAmountTextView = (TextView) itemView.findViewById(R.id.it_meal_tv_protein_carbs_fat_amount);
            mealEatedButton = (MaterialButton) itemView.findViewById(R.id.it_meal_btn_eated_meal);
            mealReplaceButton = (MaterialButton) itemView.findViewById(R.id.it_meal_btn_replace_meal);
            mealSkipButton = (MaterialButton) itemView.findViewById(R.id.it_meal_btn_skip_meal);
            viewRecipeImageCalories = itemView.findViewById(R.id.it_meal_layout_recipe_image_calories);
            moveItemButton = itemView.findViewById(R.id.it_meal_btn_move_item);
            removeItemButton = itemView.findViewById(R.id.it_meal_btn_remove_item);
        }

        public void bind(OnMealClickListener listener, int position, MealFullData meal, ViewHolder holder) {

            Recipes recipe;
            if (meal.recipe != null) {
                recipe = meal.recipe.recipe;
            }
            else {
                recipe = null;
            }
            Long mealRecipeId = meal.meal.getRecipeId();
            boolean eated = meal.meal.isEaten();
            String recipeName = "";
            String caloriesAmount = "";
            int proteinAmount = 0;
            int fatAmount = 0;
            int carbsAmount = 0;
            String proteinCarbsFatAmountLabel = "";

            //obsługa niewybranego przepisu
            if (meal.meal.getRecipeId() == null){
                recipeName = holder.itemView.getContext().getString(R.string.empty_meal_name);

                holder.mealReplaceButton.setVisibility(View.INVISIBLE);
                holder.mealEatedButton.setVisibility(View.INVISIBLE);
                holder.mealSkipButton.setVisibility(View.INVISIBLE);
                holder.viewRecipeImageCalories.setVisibility(View.INVISIBLE);

                holder.mealReplaceButton.setEnabled(false);
                holder.mealSkipButton.setEnabled(false);
                holder.mealEatedButton.setEnabled(false);

                holder.itemView.setBackgroundResource(R.drawable.background_light_green_rounded);
                holder.caloriesAmountTextView.setBackgroundResource(R.color.lightGreen);

                holder.viewRecipeImageCalories.setBackgroundResource(R.color.colorItemInForeground);

                holder.mealReplaceButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
                holder.mealSkipButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
                holder.mealEatedButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
            }
            else {
                recipeName = recipe.getName();//meal.getRecipe().getName();
                caloriesAmount = holder.itemView.getContext().getString(R.string.calories_formated_text, recipe.getCaloriesAmount());
                proteinAmount = recipe.getProteinAmount();
                fatAmount = recipe.getFatAmount();
                carbsAmount = recipe.getCarbsAmount();
                proteinCarbsFatAmountLabel = holder.itemView.getContext().getString(R.string.protein_carbs_fat_amount_formated_text, proteinAmount, carbsAmount, fatAmount);

                holder.mealReplaceButton.setVisibility(View.VISIBLE);
                holder.mealEatedButton.setVisibility(View.VISIBLE);
                holder.mealSkipButton.setVisibility(View.VISIBLE);
                holder.viewRecipeImageCalories.setVisibility(View.VISIBLE);

                holder.mealReplaceButton.setEnabled(true);
                holder.mealSkipButton.setEnabled(true);
                holder.mealEatedButton.setEnabled(true);

                //obsługa zjedzonego przepisu
                if (eated) {
                    holder.mealEatedButton.setText(R.string.meal_eated_button_restore);

                    holder.mealReplaceButton.setVisibility(View.INVISIBLE);
                    holder.mealSkipButton.setVisibility(View.INVISIBLE);

                    holder.mealReplaceButton.setEnabled(false);
                    holder.mealSkipButton.setEnabled(false);

                    holder.itemView.setBackgroundResource(R.drawable.background_light_grey_rounded);
                    holder.caloriesAmountTextView.setBackgroundResource(R.color.lightGrey);

                    holder.viewRecipeImageCalories.setBackgroundResource(R.color.white);

                    holder.mealReplaceButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGrey));
                    holder.mealSkipButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGrey));
                    holder.mealEatedButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGrey));
                } else {
                    holder.mealEatedButton.setText(R.string.meal_eated_button_eated);

                    holder.mealReplaceButton.setVisibility(View.VISIBLE);
                    holder.mealSkipButton.setVisibility(View.VISIBLE);

                    holder.mealReplaceButton.setEnabled(true);
                    holder.mealSkipButton.setEnabled(true);

                    holder.itemView.setBackgroundResource(R.drawable.background_light_green_rounded);
                    holder.caloriesAmountTextView.setBackgroundResource(R.color.lightGreen);

                    holder.viewRecipeImageCalories.setBackgroundResource(R.color.colorItemInForeground);

                    holder.mealReplaceButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
                    holder.mealSkipButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
                    holder.mealEatedButton.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.lightGreen));
                }
            }
            holder.recipeNameTextView.setText(recipeName);
            holder.caloriesAmountTextView.setText(caloriesAmount);
            holder.proteinCarbsFatAmountTextView.setText(proteinCarbsFatAmountLabel);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealClick(position, meal.meal);
                }
            });

            mealEatedButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealEatedClick(position, meal.meal);
                }
            });


            mealReplaceButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealReplaceClick(position, meal.meal);
                }
            });

            mealSkipButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealSkipClick(position, meal.meal);
                }
            });

            removeItemButton.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.alert_dialog_delete_meal_title)
                        .setMessage(v.getContext().getString(R.string.alert_dialog_delete_meal_question))
                        .setPositiveButton(R.string.dialog_positive_button_yes_text, (dialog, which) -> {
                            if (listener != null) {
                                listener.onMealRemoveClick(position, meal.meal);
                            }
                        })
                        .setNegativeButton(R.string.dialog_negative_button_abort_text, (dialog, which) -> dialog.dismiss())
                        .show();
            });

            moveItemButton.setOnLongClickListener(v -> {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                if (listener != null) listener.onStartDrag(holder);
                return true;
            });

        }
    }

    // ---------------------- FooterViewHolder ----------------------
    public class FooterViewHolder extends RecyclerView.ViewHolder {
        ImageButton addNewItemButton;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            addNewItemButton = itemView.findViewById(R.id.it_meal_add_button_btn_eated_meal);
        }

        public void bind() {
            addNewItemButton.setOnClickListener(v -> {
                if (listener != null) listener.onMealAddButtonClick();
            });
        }
    }

    public void setMeals(List<MealFullData> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public void setOnMealClickListener(OnMealClickListener listener) {
        this.listener = listener;
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < meals.size() && toPosition < meals.size()) {
            Collections.swap(meals, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == meals.size()) ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        if (position < meals.size()) {
            return meals.get(position).meal.getMealId();
        } else {
            return -1; // footer
        }
    }

    public List<MealFullData> getCurrentItems() {
        return meals;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_meal, parent, false);
            return new ViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_meal_add_button, parent, false);
            return new FooterViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            MealFullData meal = meals.get(position);
            ((ViewHolder) holder).bind(listener, position, meal, (ViewHolder) holder);
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).bind();
        }

    }

    @Override
    public int getItemCount() {
        return meals.size() + 1;
    }
}