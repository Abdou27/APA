package com.univ.tours.apa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewAdapter.DataContainer> {
    private Context context;
    ArrayList<LinkedHashMap<String, String>> patients;

    public PatientsRecyclerViewAdapter(Context context, ArrayList<LinkedHashMap<String, String>> activities) {
        this.context = context;
        this.patients = activities;
    }

    @NonNull
    @Override
    public PatientsRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_activity, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.titleTextView.setText(patients.get(position).get("name"));
        holder.durationTextView.setText(patients.get(position).get("age") + " " + context.getString(R.string.years));
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        TextView titleTextView, durationTextView;

        public DataContainer(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            durationTextView = (TextView) itemView.findViewById(R.id.durationTextView);
        }
    }
}
