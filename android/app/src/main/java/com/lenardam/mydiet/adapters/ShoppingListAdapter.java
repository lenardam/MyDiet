package com.lenardam.mydiet.adapters;

import static com.lenardam.mydiet.utils.Utils.doubleToStringFormat;

import android.content.Context;
import android.graphics.Paint;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.database.model.ShoppingList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>  {

    private List<ShoppingList> allShoppingList = new ArrayList<>();
    private OnShoppingListItemClickListener listener;

    public interface OnShoppingListItemClickListener {
        void onShoppingItemCheckboxClicked(int position, ShoppingList shoppingList, boolean isChecked);
        void onShoppingItemTextChanged(int position, ShoppingList shoppingList);
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View shoppingListLayout;
        CheckBox isBoughtCheckBox;
        EditText shoppingIngredientNameTextView;
        ImageButton moveItemButton;

        private TextView.OnEditorActionListener editorActionListener;

        public ViewHolder(@NonNull View itemView, OnShoppingListItemClickListener listener, List<ShoppingList> data) {
            super(itemView);

            isBoughtCheckBox = itemView.findViewById(R.id.it_shopping_list_cb_item_bought);
            shoppingIngredientNameTextView = itemView.findViewById(R.id.it_shopping_list_tv_ingredient_name);
            shoppingListLayout = itemView.findViewById(R.id.it_shopping_list_layout_shopping_item);
            moveItemButton = itemView.findViewById(R.id.it_shopping_list_btn_move_item);

            // inicjalizacja listenera
            editorActionListener = (v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_SEND) {

                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && listener != null) {
                        ShoppingList shoppingList = data.get(pos);
                        shoppingList.setItemName(shoppingIngredientNameTextView.getText().toString());
                        listener.onShoppingItemTextChanged(pos, shoppingList); // zapis do bazy
                    }

                    // zamknięcie klawiatury
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true; // akcja obsłużona
                }
                return false;
            };

        }

        public void bind(ShoppingList shoppingList, OnShoppingListItemClickListener listener, ViewHolder holder) {

            shoppingIngredientNameTextView.setOnEditorActionListener(null);
            isBoughtCheckBox.setOnCheckedChangeListener(null);
            String ingredientToBuy = shoppingList.getItemName();

            // ustaw dane
            if (!ingredientToBuy.equals(shoppingIngredientNameTextView.getText().toString())) {
                shoppingIngredientNameTextView.setText(ingredientToBuy);
            }

            if (shoppingList.isBought()) {
                shoppingIngredientNameTextView.setPaintFlags(
                        shoppingIngredientNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                );
                isBoughtCheckBox.setChecked(true);
                shoppingListLayout.setBackgroundResource(R.color.lightGrey);
            } else {
                shoppingIngredientNameTextView.setPaintFlags(
                        shoppingIngredientNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
                );
                isBoughtCheckBox.setChecked(false);
                shoppingListLayout.setBackgroundResource(R.color.white);
            }

            shoppingIngredientNameTextView.setOnEditorActionListener(editorActionListener);

            // Długie przytrzymanie przycisku "moveItem" uruchamia przeciąganie
            moveItemButton.setOnLongClickListener(v -> {
                // lekkie wibracje dotykowe (systemowe)
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                if (listener != null) {
                    listener.onStartDrag(holder);
                }
                return true;
            });

            // podłączamy listener ponownie
            isBoughtCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    shoppingList.setBought(isChecked); // aktualizacja modelu
                    listener.onShoppingItemCheckboxClicked(pos, shoppingList, isChecked);

                    // aktualizacja UI przy kliknięciu
                    if (isChecked) {
                        shoppingIngredientNameTextView.setPaintFlags(
                                shoppingIngredientNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                        );
                        shoppingListLayout.setBackgroundResource(R.color.lightGrey);
                    } else {
                        shoppingIngredientNameTextView.setPaintFlags(
                                shoppingIngredientNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
                        );
                        shoppingListLayout.setBackgroundResource(R.color.white);
                    }
                }
            });
        }
    }

    public void setAllShoppingList(List<ShoppingList> allShoppingList) {
        this.allShoppingList = allShoppingList;
        notifyDataSetChanged();
    }

    public void setOnShoppingListItemClickListener(OnShoppingListItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(allShoppingList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<ShoppingList> getCurrentItems() {
        return allShoppingList;
    }

    @Override
    public long getItemId(int position) {
        return allShoppingList.get(position).getShoppingListId(); // unikalne id encji
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ViewHolder(view, listener, allShoppingList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingList shoppingList = allShoppingList.get(position);
        holder.bind(shoppingList, listener, holder);
    }

    @Override
    public int getItemCount() {
        return allShoppingList.size();
    }
}
