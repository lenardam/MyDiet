package com.lenardam.mydiet.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.DietFragment;
import com.lenardam.mydiet.R;
import com.lenardam.mydiet.ShoppingListFragment;

import java.time.LocalDate;
import java.util.ArrayList;


public class ShoppingPeriodAdapter extends RecyclerView.Adapter<ShoppingPeriodAdapter.ViewHolder> {

    private ArrayList<LocalDate> week_days;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(int position);
    }

    public ShoppingPeriodAdapter(ArrayList<LocalDate> week_days, OnDateClickListener listener) {
        this.week_days = week_days;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalDate date = week_days.get(position);
        if(date == null)
            holder.rv_day_of_month_label.setText("");
        else
        {
            holder.rv_day_of_month_label.setText(String.valueOf(date.getDayOfMonth()));
            LocalDate shopping_start = ShoppingListFragment.shopping_start_date;
            LocalDate shopping_end = ShoppingListFragment.shopping_end_date;


            if (shopping_start != null && shopping_end != null) {
                if ((date.isAfter(shopping_start) || date.equals(shopping_start)) &&
                        (date.isBefore(shopping_end) || date.equals(shopping_end))) {
                    holder.date_plan_item.setBackgroundColor(Color.LTGRAY);  // Zmieniamy tło
                }
                else {
                    holder.date_plan_item.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
                }
            }
            else if (shopping_start != null && shopping_end == null) {
                if (date.equals(shopping_start)) {
                    holder.date_plan_item.setBackgroundColor(Color.LTGRAY);  // Zmieniamy tło
                }
                else {
                    holder.date_plan_item.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
                }
            }
            else {
                holder.date_plan_item.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
            }
        }

        holder.bind(position, listener);

    }

    @Override
    public int getItemCount() {
        return week_days.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rv_day_of_month_label;
        View date_plan_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_day_of_month_label = itemView.findViewById(R.id.rv_day_of_month_label);
            date_plan_item = itemView.findViewById(R.id.date_plan_item);


        }
        public void bind(int position, OnDateClickListener listener) {
            itemView.setOnClickListener(v -> listener.onDateClick(position));
        }
    }
}