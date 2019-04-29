package com.andreimesina.kitesurfingworldwide.activity.list;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andreimesina.kitesurfingworldwide.R;

public class SpotViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewName;
    private TextView textViewCountry;

    public SpotViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewName = itemView.findViewById(R.id.textView_name_item);
        textViewCountry = itemView.findViewById(R.id.textView_country_item);
    }
}
