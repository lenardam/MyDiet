package com.lenardam.mydiet.adapters;

import android.app.AlertDialog;
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
        TextView rv_ingredient_name;
        TextView rv_ingredient_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_ingredient_name = itemView.findViewById(R.id.rv_shopping_ingredient_name);
            rv_ingredient_amount = itemView.findViewById(R.id.rv_shopping_ingredient_amount);


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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.rv_ingredient_name.setText(ingredient.getName());
        String ingredient_amount = ingredient.getAmount().toString() + " " + ingredient.getUnit();
        holder.rv_ingredient_amount.setText(ingredient_amount);

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}