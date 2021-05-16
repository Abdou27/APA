package com.univ.tours.apa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Activity;

import java.util.List;

public class DoctorActivitiesRecyclerViewAdapter extends RecyclerView.Adapter<DoctorActivitiesRecyclerViewAdapter.DataContainer> {
    private Context context;
    List<Activity> activities;

    public DoctorActivitiesRecyclerViewAdapter(Context context, List<Activity> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public DoctorActivitiesRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_doctor_activity, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.titleTextView.setText(activities.get(position).getTitle());
        holder.descriptionTextView.setText(activities.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;

        public DataContainer(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
