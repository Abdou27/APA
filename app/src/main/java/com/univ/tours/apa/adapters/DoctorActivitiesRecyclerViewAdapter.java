package com.univ.tours.apa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.fragments.doctor.DoctorActivityEditFragment;

import java.util.List;

public class DoctorActivitiesRecyclerViewAdapter extends RecyclerView.Adapter<DoctorActivitiesRecyclerViewAdapter.DataContainer> {
    private Context context;
    List<Activity> activities;
    Fragment parentFragment;
    FragmentManager fm;

    public DoctorActivitiesRecyclerViewAdapter(Context context, List<Activity> activities, Fragment parentFragment, FragmentManager fm) {
        this.context = context;
        this.activities = activities;
        this.parentFragment = parentFragment;
        this.fm = fm;
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
        holder.materialCardView.setOnClickListener(v -> {
            DoctorActivityEditFragment doctorActivityEditFragment = DoctorActivityEditFragment.newInstance(activities.get(position), parentFragment);
            doctorActivityEditFragment.show(fm, "doctorEditActivityFragment");
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        MaterialCardView materialCardView;

        public DataContainer(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
        }
    }
}
