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

public class ActivitiesRecyclerViewAdapter extends RecyclerView.Adapter<ActivitiesRecyclerViewAdapter.DataContainer> {
    private Context context;
    ArrayList<LinkedHashMap<String, String>> activities;

    public ActivitiesRecyclerViewAdapter(Context context, ArrayList<LinkedHashMap<String, String>> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivitiesRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_activity, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.titleTextView.setText(activities.get(position).get("title"));
        holder.durationTextView.setText(activities.get(position).get("duration") + " " + context.getString(R.string.hours));
    }

    @Override
    public int getItemCount() {
        return activities.size();
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
