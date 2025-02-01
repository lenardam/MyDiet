package com.lenardam.mydiet.adapters;

import static com.lenardam.mydiet.utils.Utils.doubleToStringFormat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private ArrayList<RecipeIngredient> ingredients;
    private OnRecipeIngredientClickListener listener;

    public interface OnRecipeIngredientClickListener {
        void onRecipeIngredientClick(int position);
        void onRecipeIngredientLongClick(int position, View v);
    }

    public IngredientAdapter(ArrayList<RecipeIngredient> ingredients, OnRecipeIngredientClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientNameTextView;
        TextView ingredientAmountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.it_ingredient_tv_ingredient_name);
            ingredientAmountTextView = itemView.findViewById(R.id.it_ingredient_tv_ingredient_amount);


        }

        public void bind(OnRecipeIngredientClickListener listener, int position) {

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRecipeIngredientClick(position);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onRecipeIngredientLongClick(position, v);
                }
                return true;
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.ingredientNameTextView.setText(ingredient.getName());
        String ingredient_amount = doubleToStringFormat(ingredient.getAmount()) + " " + ingredient.getUnit();
        holder.ingredientAmountTextView.setText(ingredient_amount);

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}