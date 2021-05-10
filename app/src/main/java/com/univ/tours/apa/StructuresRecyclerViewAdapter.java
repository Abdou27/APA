package com.univ.tours.apa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StructuresRecyclerViewAdapter extends RecyclerView.Adapter<StructuresRecyclerViewAdapter.DataContainer> {
    private Context context;
    ArrayList<LinkedHashMap<String, String>> structures;
    ChooseStructureFragment fragment;

    public StructuresRecyclerViewAdapter(Context context, ArrayList<LinkedHashMap<String, String>> structures, ChooseStructureFragment fragment) {
        this.context = context;
        this.structures = structures;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public StructuresRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_activity, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.titleTextView.setText(structures.get(position).get("title"));
        holder.durationTextView.setText(structures.get(position).get("discipline"));
        holder.materialCardView.setOnClickListener(v -> {
            // Pass the data here
            fragment.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return structures.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;
        TextView titleTextView, durationTextView;

        public DataContainer(View itemView) {
            super(itemView);
            materialCardView = (MaterialCardView) itemView.findViewById(R.id.materialCardView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            durationTextView = (TextView) itemView.findViewById(R.id.durationTextView);
        }
    }
}
