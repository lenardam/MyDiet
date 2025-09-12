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
import com.lenardam.mydiet.database.model.ShoppingList;
import com.lenardam.mydiet.database.model.Units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>  {

    private List<ShoppingList> allShoppingList = new ArrayList<>();
    private Map<Long, String> unitMap = new HashMap<>();
    private OnShoppingListItemClickListener listener;
    private boolean isBought;

    public interface OnShoppingListItemClickListener {
        void onShoppingItemCheckboxClicked(int position, ShoppingList shoppingList, boolean isChecked);  // Nowa metoda obsługująca checkbox
        void onShoppingItemClick(int position, ShoppingList shoppingList);
        void onShoppingItemLongClick(int position, ShoppingList shoppingList, View v);

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

        public void bind(ShoppingListAdapter.OnShoppingListItemClickListener listener, int position, ShoppingList shoppingList) {

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShoppingItemClick(position, shoppingList);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onShoppingItemLongClick(position, shoppingList, v);
                }
                return true;
            });
        }
    }

    public void setAllShoppingList(List<ShoppingList> allShoppingList) {
        this.allShoppingList = allShoppingList;
        notifyDataSetChanged();
    }

    public void setUnits(List<Units> units){
        unitMap.clear();
        for (Units u : units){
            unitMap.put(u.getUnitId(), u.getName());
        }
        notifyDataSetChanged();
    }

    public void setOnShoppingListItemClickListener(OnShoppingListItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ingrediengName = allShoppingList.get(position).getItemName();
        ShoppingList shoppingList = allShoppingList.get(position);

        String ingredientAmount = doubleToStringFormat(shoppingList.getAmount());
        String unitName = unitMap.get(shoppingList.getUnitId());
        holder.shoppingIngredientNameTextView.setText(ingrediengName);
        holder.shoppingIngredientAmountTextView.setText(ingredientAmount + " " + unitName);

        if (allShoppingList.get(position).isBought() == true){
            holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.isBoughtCheckBox.setChecked(true);
            holder.shoppingListLayout.setBackgroundResource(R.color.lightGrey);
        }
        else {
            holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.isBoughtCheckBox.setChecked(false);
            holder.shoppingListLayout.setBackgroundResource(R.color.white);
        }

        // Usunięcie poprzedniego listenera
        holder.isBoughtCheckBox.setOnCheckedChangeListener(null);

        // Listener dla zmiany stanu checkboxa
        holder.isBoughtCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Obsługuje zmianę stanu checkboxa
            if (isChecked == true) {
                holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.shoppingListLayout.setBackgroundResource(R.color.lightGrey);
            } else {
                holder.shoppingIngredientNameTextView.setPaintFlags(holder.shoppingIngredientNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.shoppingIngredientAmountTextView.setPaintFlags(holder.shoppingIngredientAmountTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.shoppingListLayout.setBackgroundResource(R.color.white);
            }

            // Przekazanie pozycji i stanu checkboxa do listenera
            if (listener != null) {
                listener.onShoppingItemCheckboxClicked(holder.getBindingAdapterPosition(), shoppingList, isChecked);
            }
        });

        holder.bind(listener, position, shoppingList);
    }

    @Override
    public int getItemCount() {
        return allShoppingList.size();
    }
}