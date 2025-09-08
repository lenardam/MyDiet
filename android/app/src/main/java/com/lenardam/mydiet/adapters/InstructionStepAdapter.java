package com.lenardam.mydiet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.database.model.RecipeInstructions;

import java.util.ArrayList;
import java.util.List;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepAdapter.ViewHolder> {

    private List<RecipeInstructions> recipeInstructions = new ArrayList<>();
    private OnInstructionStepClickListener listener;
    private ArrayList<Integer> selectedPositions = new ArrayList<>();

    public interface OnInstructionStepClickListener {
        void onInstructionStepClick(int position);
        void onInstructionStepLongClick(int position, View v);
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

    public void setRecipeInstructions(List<RecipeInstructions> recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
        notifyDataSetChanged();
    }

    public void setOnInstructionStepClickListener(OnInstructionStepClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instruction_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String instruction_step = recipeInstructions.get(position).getInstruction();
        String instruction_id = String.format("%d. ",position+1);
        holder.instructionStepTextTextView.setText(instruction_id + instruction_step);

        // Ustawianie tła w zależności od zaznaczenia
        if (selectedPositions.contains(position)) {
            holder.itemView.setBackgroundResource(R.color.lightGrey);
        } else {
            holder.itemView.setBackgroundResource(R.color.white);
        }

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return recipeInstructions.size();
    }

    // Zaznacza element
    public void setSelectedItem(int position) {
        // Jeśli element nie jest zaznaczony - zaznaczamy
        if (!selectedPositions.contains(position)) {
            selectedPositions.add(position);
            notifyItemChanged(position);
        }
        else {
            selectedPositions.remove(Integer.valueOf(position));
            notifyItemChanged(position);
        }
    }

}