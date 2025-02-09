package com.lenardam.mydiet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;

import java.util.ArrayList;

public class RecipeTagAdapter extends RecyclerView.Adapter<RecipeTagAdapter.ViewHolder> {

    private ArrayList<String> tags ;
    private OnRecipeTagClickListener listener;
    private boolean canEdit;
    private ArrayList<Integer> selectedPositions = new ArrayList<>();

    public interface OnRecipeTagClickListener {
        void onRecipeTagClick(int position, View view);
        void onRecipeTagLongClick(int position, View view);
    }

    public RecipeTagAdapter(ArrayList<String> tags, OnRecipeTagClickListener listener, boolean canEdit) {
        this.tags = tags;
        this.listener = listener;
        this.canEdit = canEdit;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.it_tag_tv_tag_name);
        }

        public void bind(OnRecipeTagClickListener listener, int position) {

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRecipeTagClick(position, v);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onRecipeTagLongClick(position, v);
                }
                return true;
            });

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tag = tags.get(position);
        holder.tagTextView.setText(tag);

        // Ustawianie tła w zależności od zaznaczenia
        if (!canEdit || selectedPositions.contains(position)) {
            holder.itemView.setBackgroundResource(R.drawable.green_rounded_background);
            holder.itemView.setElevation(10f);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.light_green_rounded_background);
            holder.itemView.setElevation(0f);
        }

        holder.bind(listener, position);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    // Zaznacza element
    public void setSelectedItem(int position) {
        if (!selectedPositions.contains(position)) {
            selectedPositions.add(position);
            notifyItemChanged(position);
        }
    }

    // Usuwa zaznaczenie elementu
    public void setUnselectedItem(int position) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(Integer.valueOf(position));
            notifyItemChanged(position);
        }
    }


}
