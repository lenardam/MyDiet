package com.lenardam.mydiet.adapters;

import static com.lenardam.mydiet.utils.Utils.doubleToStringFormat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.database.model.RecipeIngredients;
import com.lenardam.mydiet.database.model.Units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<RecipeIngredients> ingredients = new ArrayList<>();
    private Map<Long, String> unitMap = new HashMap<>();
    private OnRecipeIngredientClickListener listener;
    private ArrayList<Integer> selectedPositions = new ArrayList<>();

    public interface OnRecipeIngredientClickListener {
        void onRecipeIngredientClick(int position);
        void onRecipeIngredientLongClick(int position, View v);
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

    public void setIngredients(List<RecipeIngredients> ingredients){
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public void setUnits(List<Units> units){
        unitMap.clear();
        for (Units u : units){
            unitMap.put(u.getUnitId(), u.getName());
        }
        notifyDataSetChanged();
    }

    public void setOnRecipeIngredientClickListener(OnRecipeIngredientClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredients ingredient = ingredients.get(position);
        holder.ingredientNameTextView.setText(ingredient.getName());

        String unitName = unitMap.get(ingredient.getUnitId());

        String ingredient_amount = doubleToStringFormat(ingredient.getAmount()) + " " + unitName;
        holder.ingredientAmountTextView.setText(ingredient_amount);

        // Ustawianie tła w zależności od zaznaczenia
        if (selectedPositions.contains(position)) {
            holder.itemView.setBackgroundResource(R.color.lightGrey);
        } else {
            holder.itemView.setBackgroundResource(R.color.white);
        }


        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    // Zaznacza element
    public void setSelectedItem(int position) {
        // Jeśli element nie jest zaznaczony - zaznaczamy
        if (!selectedPositions.contains(position)) {
            selectedPositions.add(position);
            notifyItemChanged(position);
        }
        else {
            selectedPositions.remove(Integer.valueOf(position));
            notifyItemChanged(position);
        }
    }


}