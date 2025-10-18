package com.lenardam.mydiet.adapters;

import static com.lenardam.mydiet.utils.CalendarUtils.getDayName;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.ShoppingListFragment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ShoppingPeriodAdapter extends RecyclerView.Adapter<ShoppingPeriodAdapter.ViewHolder> {

    private List<LocalDate> weekDays = new ArrayList<>();
    private OnDateClickListener listener;
    private LocalDate shoppingStartDate;
    private LocalDate shoppingEndDate;

    public interface OnDateClickListener {
        void onDateClick(int position);
    }

    public ShoppingPeriodAdapter() {
    }

    public void setWeekDays(List<LocalDate> weekDays) {
        this.weekDays = weekDays;
        notifyDataSetChanged();
    }

    public void setShoppingStartDate(LocalDate shoppingStartDate) {
        this.shoppingStartDate = shoppingStartDate;
        notifyDataSetChanged();
    }

    public void setShoppingEndDate(LocalDate shoppingEndDate) {
        this.shoppingEndDate = shoppingEndDate;
        notifyDataSetChanged();
    }

    public void setListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public LocalDate getShoppingStartDate() {
        return shoppingStartDate;
    }

    public LocalDate getShoppingEndDate() {
        return shoppingEndDate;
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
            holder.dayOfWeekNameLabelTextView.setText(getDayName(date, holder.itemView.getContext()));

            if (shoppingStartDate != null && shoppingEndDate != null) {
                if ((date.isAfter(shoppingStartDate) || date.equals(shoppingStartDate)) &&
                        (date.isBefore(shoppingEndDate) || date.equals(shoppingEndDate))) {
                    holder.datePlanItem.setBackgroundResource(R.color.colorSecondary);  // Zmieniamy tło
                    holder.dayOfWeekNameLabelTextView.setBackgroundColor(Color.TRANSPARENT);
                    holder.dayOfWeekNameLabelTextView.setTypeface(null, Typeface.BOLD);
                    holder.dayOfMonthLabelTextView.setTypeface(null, Typeface.BOLD);
                }
                else {
                    holder.datePlanItem.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
                    holder.dayOfWeekNameLabelTextView.setBackgroundResource(R.color.lightGrey);
                    holder.dayOfWeekNameLabelTextView.setTypeface(null, Typeface.NORMAL);
                    holder.dayOfMonthLabelTextView.setTypeface(null, Typeface.NORMAL);
                }
            }
            else if (shoppingStartDate != null && shoppingEndDate == null) {
                if (date.equals(shoppingStartDate)) {
                    holder.datePlanItem.setBackgroundResource(R.color.colorSecondary);  // Zmieniamy tło
                    holder.dayOfWeekNameLabelTextView.setBackgroundColor(Color.TRANSPARENT);
                    holder.dayOfWeekNameLabelTextView.setTypeface(null, Typeface.BOLD);
                    holder.dayOfMonthLabelTextView.setTypeface(null, Typeface.BOLD);
                }
                else {
                    holder.datePlanItem.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
                    holder.dayOfWeekNameLabelTextView.setBackgroundResource(R.color.lightGrey);
                    holder.dayOfWeekNameLabelTextView.setTypeface(null, Typeface.NORMAL);
                    holder.dayOfMonthLabelTextView.setTypeface(null, Typeface.NORMAL);
                }
            }
            else {
                holder.datePlanItem.setBackgroundColor(Color.TRANSPARENT);  // Przywracamy tło
                holder.dayOfWeekNameLabelTextView.setBackgroundResource(R.color.lightGrey);
                holder.dayOfWeekNameLabelTextView.setTypeface(null, Typeface.NORMAL);
                holder.dayOfMonthLabelTextView.setTypeface(null, Typeface.NORMAL);
            }

            // Zmiana koloru dla niedzieli
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                holder.dayOfWeekNameLabelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
                holder.dayOfMonthLabelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
            } else {
                // Przywrócenie domyślnego koloru dla dni roboczych
                holder.dayOfWeekNameLabelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                holder.dayOfMonthLabelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            }
        }

        holder.bind(position, listener);

    }

    @Override
    public int getItemCount() {
        return weekDays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeekNameLabelTextView;
        TextView dayOfMonthLabelTextView;
        View datePlanItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeekNameLabelTextView = itemView.findViewById(R.id.it_date_plan_tv_day_of_week_name_label);
            dayOfMonthLabelTextView = itemView.findViewById(R.id.it_date_plan_tv_day_of_month_label);
            datePlanItem = itemView.findViewById(R.id.date_plan_item);


        }
        public void bind(int position, OnDateClickListener listener) {
            itemView.setOnClickListener(v -> listener.onDateClick(position));
        }
    }
}