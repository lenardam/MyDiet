package com.lenardam.mydiet.adapters;

import static com.lenardam.mydiet.utils.Utils.doubleToStringFormat;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.RecipeIngredient;
import com.lenardam.mydiet.model.ShoppingItem;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>  {

    private ArrayList<ShoppingItem> shoppingItems;
    private OnShoppingListCheckboxClickListener listener;
    private boolean isBought;

    public interface OnShoppingListCheckboxClickListener {
        void onCheckboxClicked(int position, boolean isChecked);  // Nowa metoda obsługująca checkbox
    }

    public ShoppingListAdapter(ArrayList<ShoppingItem> ingredients, OnShoppingListCheckboxClickListener listener) {
        this.shoppingItems = ingredients;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox isBoughtCheckBox;
        TextView shoppingIngredientNameTextView;
        TextView shoppingIngredientAmountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            isBoughtCheckBox = itemView.findViewById(R.id.it_shopping_list_cb_item_bought);
            shoppingIngredientNameTextView = itemView.findViewById(R.id.it_shopping_list_tv_ingredient_name);
            shoppingIngredientAmountTextView = itemView.findViewById(R.id.it_shopping_list_tv_ingredient_amount);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredient ingredient = shoppingItems.get(position).getIngredientToBuy();
        String ingrediengName = ingredient.getName();
        String ingredientAmount = doubleToStringFormat(ingredient.getAmount());
        String unit = String.valueOf(ingredient.getUnit());
        holder.shoppingIngredientNameTextView.setText(ingrediengName);
        holder.shoppingIngredientAmountTextView.setText(ingredientAmount + " " + unit);

        if (shoppingItems.get(position).isBought() == true){
            holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.isBoughtCheckBox.setChecked(true);
        }
        else {
            holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.isBoughtCheckBox.setChecked(false);
        }

//        // Usunięcie poprzedniego listenera
        holder.isBoughtCheckBox.setOnCheckedChangeListener(null);

        // Listener dla zmiany stanu checkboxa
        holder.isBoughtCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Obsługuje zmianę stanu checkboxa
            if (isChecked == true) {
                holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            // Przekazanie pozycji i stanu checkboxa do listenera
            if (listener != null) {
                listener.onCheckboxClicked(holder.getBindingAdapterPosition(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    // Metoda do przenoszenia składnika
    public void moveItem(int fromPosition, int toPosition) {
        // Sprawdzamy, czy pozycje są różne
        if (fromPosition != toPosition) {
            ShoppingItem item = shoppingItems.remove(fromPosition);  // Usuwamy element z bieżącej pozycji
            shoppingItems.add(toPosition, item);  // Dodajemy go na nową pozycję
            notifyItemMoved(fromPosition, toPosition);  // Powiadamiamy adapter, że element został przeniesiony
        }
    }
}