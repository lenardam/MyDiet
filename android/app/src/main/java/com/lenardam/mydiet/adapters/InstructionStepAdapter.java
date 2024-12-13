package com.lenardam.mydiet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.model.RecipeIngredient;

import java.util.ArrayList;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepAdapter.ViewHolder> {

    private ArrayList<String> instruction_steps ;

    public InstructionStepAdapter(ArrayList<String> instruction_steps) {
        this.instruction_steps = instruction_steps;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rv_instruction_step_id;
        TextView rv_instruction_step_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_instruction_step_id = itemView.findViewById(R.id.rv_instruction_step_id);
            rv_instruction_step_text = itemView.findViewById(R.id.rv_instruction_step_text);


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String instruction_step = instruction_steps.get(position);
        String instruction_id = String.valueOf(position+1);
        holder.rv_instruction_step_id.setText(instruction_id);
        holder.rv_instruction_step_text.setText(instruction_step);
    }

    @Override
    public int getItemCount() {
        return instruction_steps.size();
    }
}