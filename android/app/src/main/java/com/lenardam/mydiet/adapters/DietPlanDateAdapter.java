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

import java.time.LocalDate;
import java.util.ArrayList;


public class DietPlanDateAdapter extends RecyclerView.Adapter<DietPlanDateAdapter.ViewHolder> {

    private ArrayList<LocalDate> weekDays;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(int position);
    }

    public DietPlanDateAdapter(ArrayList<LocalDate> weekDays, OnDateClickListener listener) {
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

            // Zmiana tła dla wybranej daty
            if (date.equals(DietFragment.selectedDate)) {
                holder.datePlanItem.setBackgroundColor(Color.LTGRAY);  // Zmieniamy tło
            } else {
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