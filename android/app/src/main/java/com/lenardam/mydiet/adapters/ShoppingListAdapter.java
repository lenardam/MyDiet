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
    private OnShoppingListItemClickListener listener;
    private boolean isBought;

    public interface OnShoppingListItemClickListener {
        void onShoppingItemCheckboxClicked(int position, boolean isChecked);  // Nowa metoda obsługująca checkbox
        void onShoppingItemClick(int position);
        void onShoppingItemLongClick(int position, View v);

    }

    public ShoppingListAdapter(ArrayList<ShoppingItem> ingredients, OnShoppingListItemClickListener listener) {
        this.shoppingItems = ingredients;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View shoppingListLayout;
        CheckBox isBoughtCheckBox;
        TextView shoppingIngredientNameTextView;
        TextView shoppingIngredientAmountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            isBoughtCheckBox = itemView.findViewById(R.id.it_shopping_list_cb_item_bought);
            shoppingIngredientNameTextView = itemView.findViewById(R.id.it_shopping_list_tv_ingredient_name);
            shoppingIngredientAmountTextView = itemView.findViewById(R.id.it_shopping_list_tv_ingredient_amount);
            shoppingListLayout = itemView.findViewById(R.id.it_shopping_list_layout_shopping_item);
        }

        public void bind(ShoppingListAdapter.OnShoppingListItemClickListener listener, int position) {

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShoppingItemClick(position);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onShoppingItemLongClick(position, v);
                }
                return true;
            });
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
            holder.shoppingListLayout.setBackgroundResource(R.color.lightGreenGrey);
        }
        else {
            holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.isBoughtCheckBox.setChecked(false);
            holder.shoppingListLayout.setBackgroundResource(android.R.color.transparent);
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
                listener.onShoppingItemCheckboxClicked(holder.getBindingAdapterPosition(), isChecked);
            }
        });

        holder.bind(listener, position);
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