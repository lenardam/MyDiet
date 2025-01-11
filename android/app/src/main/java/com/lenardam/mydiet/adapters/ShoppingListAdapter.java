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

    private ArrayList<ShoppingItem> shopping_items;
    private OnShoppingListCheckboxClickListener listener;
    private boolean isBought;

    public interface OnShoppingListCheckboxClickListener {
        void onCheckboxClicked(int position, boolean isChecked);  // Nowa metoda obsługująca checkbox
    }

    public ShoppingListAdapter(ArrayList<ShoppingItem> ingredients, OnShoppingListCheckboxClickListener listener) {
        this.shopping_items = ingredients;
        this.listener = listener;
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
        RecipeIngredient ingredient = shopping_items.get(position).getIngredient_to_buy();
        String ingredieng_name = ingredient.getName();
        String ingredient_amount = doubleToStringFormat(ingredient.getAmount());
        String unit = String.valueOf(ingredient.getUnit());
        holder.rv_shopping_ingredient_name.setText(ingredieng_name);
        holder.rv_shopping_ingredient_amount.setText(ingredient_amount + " " + unit);

        if (shopping_items.get(position).isIs_bought() == true){
            holder.rv_shopping_ingredient_name.setPaintFlags(holder.rv_shopping_ingredient_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.rv_shopping_ingredient_amount.setPaintFlags(holder.rv_shopping_ingredient_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.rv_shoppingCheckBox.setChecked(true);
        }
        else {
            holder.rv_shopping_ingredient_name.setPaintFlags(holder.rv_shopping_ingredient_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.rv_shopping_ingredient_amount.setPaintFlags(holder.rv_shopping_ingredient_amount.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.rv_shoppingCheckBox.setChecked(false);
        }

//        // Usunięcie poprzedniego listenera
        holder.rv_shoppingCheckBox.setOnCheckedChangeListener(null);

        // Listener dla zmiany stanu checkboxa
        holder.rv_shoppingCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Obsługuje zmianę stanu checkboxa
            if (isChecked == true) {
                holder.rv_shopping_ingredient_name.setPaintFlags(holder.rv_shopping_ingredient_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.rv_shopping_ingredient_amount.setPaintFlags(holder.rv_shopping_ingredient_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.rv_shopping_ingredient_name.setPaintFlags(holder.rv_shopping_ingredient_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.rv_shopping_ingredient_amount.setPaintFlags(holder.rv_shopping_ingredient_amount.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            // Przekazanie pozycji i stanu checkboxa do listenera
            if (listener != null) {
                listener.onCheckboxClicked(holder.getBindingAdapterPosition(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopping_items.size();
    }

    // Metoda do przenoszenia składnika
    public void moveItem(int fromPosition, int toPosition) {
        // Sprawdzamy, czy pozycje są różne
        if (fromPosition != toPosition) {
            ShoppingItem item = shopping_items.remove(fromPosition);  // Usuwamy element z bieżącej pozycji
            shopping_items.add(toPosition, item);  // Dodajemy go na nową pozycję
            notifyItemMoved(fromPosition, toPosition);  // Powiadamiamy adapter, że element został przeniesiony
        }
    }
}