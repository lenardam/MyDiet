package com.lenardam.mydiet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;

import java.util.ArrayList;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepAdapter.ViewHolder> {

    private ArrayList<String> instructionSteps;
    private OnInstructionStepClickListener listener;

    public interface OnInstructionStepClickListener {
        void onInstructionStepClick(int position);
        void onInstructionStepLongClick(int position, View v);
    }

    public InstructionStepAdapter(ArrayList<String> instructionSteps, OnInstructionStepClickListener listener) {
        this.instructionSteps = instructionSteps;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView instructionStepTextTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            instructionStepTextTextView = itemView.findViewById(R.id.it_instr_step_tv_step_text);


        }

        public void bind(OnInstructionStepClickListener listener, int position) {

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onInstructionStepClick(position);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onInstructionStepLongClick(position, v);
                }
                return true;
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instruction_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String instruction_step = instructionSteps.get(position);
//        String instruction_id = String.valueOf(position+1);
        String instruction_id = String.format("%d. ",position+1);
        holder.instructionStepTextTextView.setText(instruction_id + instruction_step);

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return instructionSteps.size();
    }
}