package com.lenardam.mydiet.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.ShoppingListFragment;

import java.time.LocalDate;
import java.util.ArrayList;


public class ShoppingPeriodAdapter extends RecyclerView.Adapter<ShoppingPeriodAdapter.ViewHolder> {

    private ArrayList<LocalDate> weekDays;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(int position);
    }

    public ShoppingPeriodAdapter(ArrayList<LocalDate> weekDays, OnDateClickListener listener) {
        this.weekDays = weekDays;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalDate date = weekDays.get(position);
        if(date == null)
            holder.dayOfMonthLabelTextView.setText("");
        else
        {
            holder.dayOfMonthLabelTextView.setText(String.valueOf(date.getDayOfMonth()));
            LocalDate shoppingStart = ShoppingListFragment.shoppingStartDate;
            LocalDate shoppingEnd = ShoppingListFragment.shoppingEndDate;


            if (shoppingStart != null && shoppingEnd != null) {
                if ((date.isAfter(shoppingStart) || date.equals(shoppingStart)) &&
                        (date.isBefore(shoppingEnd) || date.equals(shoppingEnd))) {
                    holder.datePlanItem.setBackgroundColor(Color.LTGRAY);  // Zmieniamy tło
                }
                else {
                    holder.datePlanItem.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
                }
            }
            else if (shoppingStart != null && shoppingEnd == null) {
                if (date.equals(shoppingStart)) {
                    holder.datePlanItem.setBackgroundColor(Color.LTGRAY);  // Zmieniamy tło
                }
                else {
                    holder.datePlanItem.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
                }
            }
            else {
                holder.datePlanItem.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
            }
        }

        holder.bind(position, listener);

    }

    @Override
    public int getItemCount() {
        return weekDays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfMonthLabelTextView;
        View datePlanItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonthLabelTextView = itemView.findViewById(R.id.it_date_plan_tv_day_of_month_label);
            datePlanItem = itemView.findViewById(R.id.date_plan_item);


        }
        public void bind(int position, OnDateClickListener listener) {
            itemView.setOnClickListener(v -> listener.onDateClick(position));
        }
    }
}