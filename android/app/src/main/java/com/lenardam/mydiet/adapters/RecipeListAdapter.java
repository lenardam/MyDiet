package com.lenardam.mydiet.adapters;

import android.app.AlertDialog;
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
        private final RecyclerView rv_recipeTagRecyclerView;
        TextView rv_recipe_name;
        TextView rv_caloriesAmountTextView;
        TextView rv_proteinAmountTextView;
        TextView rv_fatAmountTextView;
        TextView rv_carvsAmountTextView;
        TextView carbsLabel;
        TextView fatLabel;
        TextView proteinLabel;
        ImageButton recieDeleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_recipe_name = itemView.findViewById(R.id.rv_recipe_name);
            rv_recipe_name = (TextView) itemView.findViewById(R.id.rv_recipe_name);
            rv_caloriesAmountTextView = (TextView) itemView.findViewById(R.id.rv_caloriesAmountTextView);
            rv_proteinAmountTextView = (TextView) itemView.findViewById(R.id.rv_proteinAmountTextView);
            rv_fatAmountTextView = (TextView) itemView.findViewById(R.id.rv_fatAmountTextView);
            rv_carvsAmountTextView = (TextView) itemView.findViewById(R.id.rv_carvsAmountTextView);
            carbsLabel = (TextView) itemView.findViewById(R.id.carbsLabel);
            fatLabel = (TextView) itemView.findViewById(R.id.fatLabel);
            proteinLabel = (TextView) itemView.findViewById(R.id.proteinLabel);
            recieDeleteButton = (ImageButton) itemView.findViewById(R.id.recipeDeleteButton);
            rv_recipeTagRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_recipeTagRecyclerView);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        String recipe_name = recipe.getName();
        String calories_amount = String.valueOf(recipe.getCalories_amount()) + " kcal";
        String protein_amount = String.valueOf(recipe.getProtein_amount()) + " g";
        String fat_amount = String.valueOf(recipe.getFat_amount()) + " g";
        String carbs_amount = String.valueOf(recipe.getCarbs_amount()) + " g";

        holder.rv_recipe_name.setText(recipe_name);
        holder.rv_caloriesAmountTextView.setText(calories_amount);
        holder.rv_proteinAmountTextView.setText(protein_amount);
        holder.rv_fatAmountTextView.setText(fat_amount);
        holder.rv_carvsAmountTextView.setText(carbs_amount);

        holder.recieDeleteButton.setVisibility(View.INVISIBLE);
        holder.rv_recipeTagRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeTagListAdapter = new RecipeTagAdapter(recipe.getTags(), null, false);
        holder.rv_recipeTagRecyclerView.setAdapter(recipeTagListAdapter);

        holder.bind(listener, position, canEdit);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}