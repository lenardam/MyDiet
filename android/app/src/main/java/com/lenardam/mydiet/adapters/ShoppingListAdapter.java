package com.lenardam.mydiet.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.Recipe;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>  {

    private ArrayList<RecipeIngredient> ingredients;
    private OnShoppingListCheckboxClickListener listener;
    private boolean isBought;

    public interface OnShoppingListCheckboxClickListener {
        void onCheckboxClicked(int position, boolean isChecked);  // Nowa metoda obsługująca checkbox
    }

    public ShoppingListAdapter(ArrayList<RecipeIngredient> ingredients, boolean isBought, OnShoppingListCheckboxClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
        this.isBought = isBought;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox rv_shoppingCheckBox;
        TextView rv_shopping_ingredient_name;
        TextView rv_shopping_ingredient_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_shoppingCheckBox = itemView.findViewById(R.id.rv_shoppingCheckBox);
            rv_shopping_ingredient_name = itemView.findViewById(R.id.rv_shopping_ingredient_name);
            rv_shopping_ingredient_amount = itemView.findViewById(R.id.rv_shopping_ingredient_amount);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        String ingredieng_name = ingredient.getName();
        String ingredient_amount = String.valueOf(ingredient.getAmount());
        holder.rv_shopping_ingredient_name.setText(ingredieng_name);
        holder.rv_shopping_ingredient_amount.setText(ingredient_amount);
        holder.rv_shoppingCheckBox.setChecked(isBought);

        if (isBought) {
            holder.rv_shopping_ingredient_name.setPaintFlags(holder.rv_shopping_ingredient_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.rv_shopping_ingredient_amount.setPaintFlags(holder.rv_shopping_ingredient_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.rv_shopping_ingredient_name.setPaintFlags(holder.rv_shopping_ingredient_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.rv_shopping_ingredient_amount.setPaintFlags(holder.rv_shopping_ingredient_amount.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Listener dla zmiany stanu checkboxa
        holder.rv_shoppingCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onCheckboxClicked(position, isChecked);  // Przekazanie pozycji i stanu checkboxa
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}