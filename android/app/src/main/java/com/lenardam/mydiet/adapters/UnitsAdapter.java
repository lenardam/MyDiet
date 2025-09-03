package com.lenardam.mydiet.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lenardam.mydiet.database.model.Units;

import java.util.List;

public class UnitsAdapter extends ArrayAdapter<Units> {
    public UnitsAdapter(@NonNull Context context, @NonNull List<Units> unitsList) {
        super(context, android.R.layout.simple_spinner_item, unitsList);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        Units unit = getItem(position);
        if (unit != null) {
            label.setText(unit.getName()); // 👈 wyświetlamy nazwę jednostki
        }
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        Units unit = getItem(position);
        if (unit != null) {
            label.setText(unit.getName());
        }
        return label;
    }
}
