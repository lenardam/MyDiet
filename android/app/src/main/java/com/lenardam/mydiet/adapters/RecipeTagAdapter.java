package com.lenardam.mydiet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenardam.mydiet.R;
import com.lenardam.mydiet.database.model.Tags;

import java.util.ArrayList;
import java.util.List;

public class RecipeTagAdapter extends RecyclerView.Adapter<RecipeTagAdapter.ViewHolder> {

    private List<Tags> tags = new ArrayList<>();
    private OnRecipeTagClickListener listener;
    private boolean canEdit;
    private ArrayList<Integer> selectedPositions = new ArrayList<>();

    public interface OnRecipeTagClickListener {
        void onRecipeTagClick(int position, Tags tag,  View view);
        void onRecipeTagLongClick(int position, Tags tag, View view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.it_tag_tv_tag_name);
        }

        public void bind(OnRecipeTagClickListener listener, Tags tag, int position) {

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRecipeTagClick(position, tag, v);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onRecipeTagLongClick(position, tag, v);
                }
                return true;
            });

        }
    }

    public void setOnRecipeTagClickListener(OnRecipeTagClickListener listener) {
        this.listener = listener;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }

    public Tags getTags(int position){
        return tags.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tags tag = tags.get(position);
        String tagName = tags.get(position).getName();
        holder.tagTextView.setText(tagName);

        // Ustawianie tła w zależności od zaznaczenia
        if (!canEdit || selectedPositions.contains(position)) {
            holder.itemView.setBackgroundResource(R.drawable.background_green_rounded);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.background_light_green_rounded);
        }

        holder.bind(listener, tag, position);
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
