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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.database.model.ShoppingList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<ShoppingList> allShoppingList = new ArrayList<>();
    private OnShoppingListItemClickListener listener;

    public interface OnShoppingListItemClickListener {
        void onShoppingItemCheckboxClicked(int position, ShoppingList shoppingList, boolean isChecked);
        void onShoppingItemTextChanged(int position, ShoppingList shoppingList);
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
        void onShoppingItemAddButtonClick();
    }

    // ---------------------- ViewHolder dla zwykłych elementów ----------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View shoppingListLayout;
        CheckBox isBoughtCheckBox;
        EditText shoppingIngredientNameTextView;
        ImageButton moveItemButton;

        private final EditText.OnEditorActionListener editorActionListener;

        public ViewHolder(@NonNull View itemView, OnShoppingListItemClickListener listener, List<ShoppingList> data) {
            super(itemView);

            isBoughtCheckBox = itemView.findViewById(R.id.it_shopping_list_cb_item_bought);
            shoppingIngredientNameTextView = itemView.findViewById(R.id.it_shopping_list_tv_ingredient_name);
            shoppingListLayout = itemView.findViewById(R.id.it_shopping_list_layout_shopping_item);
            moveItemButton = itemView.findViewById(R.id.it_shopping_list_btn_move_item);

            editorActionListener = (v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_SEND) {

                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && listener != null) {
                        ShoppingList shoppingList = data.get(pos);
                        shoppingList.setItemName(shoppingIngredientNameTextView.getText().toString());
                        listener.onShoppingItemTextChanged(pos, shoppingList);
                    }

                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            };
        }

        public void bind(ShoppingList shoppingList, OnShoppingListItemClickListener listener, ViewHolder holder) {
            shoppingIngredientNameTextView.setOnEditorActionListener(null);
            isBoughtCheckBox.setOnCheckedChangeListener(null);

            // ustawienie danych
            shoppingIngredientNameTextView.setText(shoppingList.getItemName());
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

            moveItemButton.setOnLongClickListener(v -> {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                if (listener != null) listener.onStartDrag(holder);
                return true;
            });

            isBoughtCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    shoppingList.setBought(isChecked);
                    listener.onShoppingItemCheckboxClicked(pos, shoppingList, isChecked);

                    // update UI
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

    // ---------------------- FooterViewHolder ----------------------
    public class FooterViewHolder extends RecyclerView.ViewHolder {
        ImageButton addNewItemButton;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            addNewItemButton = itemView.findViewById(R.id.it_shopping_list_add_new_item);
        }

        public void bind() {
            addNewItemButton.setOnClickListener(v -> {
                if (listener != null) listener.onShoppingItemAddButtonClick();
            });
        }
    }

    // ---------------------- Adapter ----------------------
    public void setAllShoppingList(List<ShoppingList> allShoppingList) {
        this.allShoppingList = allShoppingList;
        notifyDataSetChanged();
    }

    public void setOnShoppingListItemClickListener(OnShoppingListItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < allShoppingList.size() && toPosition < allShoppingList.size()) {
            Collections.swap(allShoppingList, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    public List<ShoppingList> getCurrentItems() {
        return allShoppingList;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == allShoppingList.size()) ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        if (position < allShoppingList.size()) {
            return allShoppingList.get(position).getShoppingListId();
        } else {
            return -1; // footer
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_shopping_list, parent, false);
            return new ViewHolder(view, listener, allShoppingList);
        } else {
            View view = inflater.inflate(R.layout.item_shopping_list_add_button, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ShoppingList shoppingList = allShoppingList.get(position);
            ((ViewHolder) holder).bind(shoppingList, listener, (ViewHolder) holder);
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return allShoppingList.size() + 1; // +1 dla footer
    }
}
