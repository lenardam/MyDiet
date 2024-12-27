package com.lenardam.mydiet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class DatePlanAdapter extends RecyclerView.Adapter<DatePlanAdapter.ViewHolder> {

    private ArrayList<DietPlan> diet_plan;

    public DatePlanAdapter(ArrayList<DietPlan> diet_plan) {
        this.diet_plan = diet_plan;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rv_day_of_week_label;
        TextView rv_day_of_month_label;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_day_of_week_label = itemView.findViewById(R.id.rv_day_of_week_label);
            rv_day_of_month_label = itemView.findViewById(R.id.rv_day_of_month_label);


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DietPlan dp = diet_plan.get(position);

        SimpleDateFormat shortDayOfWeekFormatter = new SimpleDateFormat("EEE");
        SimpleDateFormat dayOfMonthFormatter = new SimpleDateFormat("d");

        String shortDayOfWeek = shortDayOfWeekFormatter.format(dp.getDiet_plan_date());
        String dayOfMonth = dayOfMonthFormatter.format(dp.getDiet_plan_date());

        holder.rv_day_of_week_label.setText(shortDayOfWeek);
        holder.rv_day_of_month_label.setText(dayOfMonth);
    }

    @Override
    public int getItemCount() {
        return diet_plan.size();
    }
}