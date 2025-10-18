package com.lenardam.mydiet.adapters;

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
        void onShoppingRemoveItemButtonClick(int position, ShoppingList shoppingList);

        void onEditingStateChanged(boolean isEditing);
    }

    // ---------------------- ViewHolder dla zwykłych elementów ----------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View shoppingListLayout;
        CheckBox isBoughtCheckBox;
        EditText shoppingIngredientNameTextView;
        ImageButton moveItemButton;
        ImageButton removeItemButton;

        private final EditText.OnEditorActionListener editorActionListener;

        public ViewHolder(@NonNull View itemView, OnShoppingListItemClickListener listener, List<ShoppingList> data) {
            super(itemView);

            isBoughtCheckBox = itemView.findViewById(R.id.it_shopping_list_cb_item_bought);
            shoppingIngredientNameTextView = itemView.findViewById(R.id.it_shopping_list_tv_ingredient_name);
            shoppingListLayout = itemView.findViewById(R.id.it_shopping_list_layout_shopping_item);
            moveItemButton = itemView.findViewById(R.id.it_shopping_list_btn_move_item);
            removeItemButton = itemView.findViewById(R.id.it_shopping_list_btn_remove_item);

            editorActionListener = (v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND) {

                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && pos < data.size() && listener != null) {
                        ShoppingList shoppingList = data.get(pos);
                        shoppingList.setItemName(shoppingIngredientNameTextView.getText().toString());
                        listener.onShoppingItemTextChanged(pos, shoppingList);
                    }

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    shoppingIngredientNameTextView.clearFocus();
                    return true;
                }
                return false;
            };
        }

        public void bind(ShoppingList shoppingList, OnShoppingListItemClickListener listener, ViewHolder holder, List<ShoppingList> data) {
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

                //zablokuj edycję
                shoppingIngredientNameTextView.setEnabled(false);
                shoppingIngredientNameTextView.setFocusable(false);
                shoppingIngredientNameTextView.setFocusableInTouchMode(false);
                shoppingIngredientNameTextView.setCursorVisible(false);

            } else {
                shoppingIngredientNameTextView.setPaintFlags(
                        shoppingIngredientNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
                );
                isBoughtCheckBox.setChecked(false);
                shoppingListLayout.setBackgroundResource(R.color.white);

                //odblokuj edycję
                shoppingIngredientNameTextView.setEnabled(true);
                shoppingIngredientNameTextView.setFocusable(true);
                shoppingIngredientNameTextView.setFocusableInTouchMode(true);
                shoppingIngredientNameTextView.setCursorVisible(true);
            }

            shoppingIngredientNameTextView.setOnEditorActionListener(editorActionListener);

            moveItemButton.setOnLongClickListener(v -> {
                clearEditTextFocus(v);
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                if (listener != null) listener.onStartDrag(holder);
                return true;
            });

            moveItemButton.setOnClickListener(v -> {
                clearEditTextFocus(v);
            });

            removeItemButton.setOnClickListener(v -> {
                clearEditTextFocus(v);
                if (listener != null) {
                    listener.onShoppingRemoveItemButtonClick(getBindingAdapterPosition(), shoppingList);
                }
            });

            isBoughtCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                clearEditTextFocus(buttonView);
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

            shoppingIngredientNameTextView.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    //Schowaj klawiaturę
                    InputMethodManager imm = (InputMethodManager)
                            v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Usuń focus
                    v.clearFocus();

                    //Zapisz zmiany do modelu (tak samo jak w editorActionListener)
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && pos < data.size() && listener != null) {
                        ShoppingList shoppingListItem = data.get(pos);
                        shoppingListItem.setItemName(shoppingIngredientNameTextView.getText().toString());
                        listener.onShoppingItemTextChanged(pos, shoppingListItem);
                    }
                }

                if (listener != null) {
                    listener.onEditingStateChanged(hasFocus);
                }
            });

        }

        private void clearEditTextFocus(View v) {
            View currentFocus = ((ViewGroup) v.getRootView()).findFocus();
            if (currentFocus instanceof EditText) {
                currentFocus.clearFocus();

                InputMethodManager imm = (InputMethodManager)
                        v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
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
                //usuń focus z ewentualnie edytowanego pola
                View currentFocus = ((ViewGroup) v.getRootView()).findFocus();
                if (currentFocus instanceof EditText) {
                    currentFocus.clearFocus();
                    InputMethodManager imm = (InputMethodManager)
                            v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }

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
            ((ViewHolder) holder).bind(shoppingList, listener, (ViewHolder) holder, allShoppingList);
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return allShoppingList.size() + 1; // +1 dla footer
    }
}
