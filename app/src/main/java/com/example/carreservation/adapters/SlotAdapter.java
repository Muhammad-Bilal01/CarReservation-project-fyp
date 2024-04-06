package com.example.carreservation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carreservation.R;

import java.util.ArrayList;

public class SlotAdapter extends ArrayAdapter<String> {

public SlotAdapter(@NonNull Context context, ArrayList<String> slotsArrayList) {
        super(context, 0, slotsArrayList);
        }

@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
        // Layout Inflater inflates each item to be displayed in GridView. 
        listitemView = LayoutInflater.from(getContext()).inflate(R.layout.slot_item, parent, false);
        }

        String title = getItem(position);
        TextView txtTitle = listitemView.findViewById(R.id.txtSlotTitle);

        txtTitle.setText(title);
        return listitemView;
        }
        }