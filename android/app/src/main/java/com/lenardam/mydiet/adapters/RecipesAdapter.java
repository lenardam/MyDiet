package com.lenardam.mydiet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.Recipe;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private ArrayList<Recipe> recipes;

    public RecipesAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rv_recipe_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_recipe_name = itemView.findViewById(R.id.rv_recipe_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        String recipe_name = recipe.getName();
        holder.rv_recipe_name.setText(recipe_name);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}