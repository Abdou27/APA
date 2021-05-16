package com.univ.tours.apa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.fragments.CollaboratorEditActivityFragment;
import com.univ.tours.apa.fragments.CollaboratorReadCourseFragment;

import java.util.ArrayList;
import java.util.List;

public class CollaboratorActivitiesRecyclerViewAdapter extends RecyclerView.Adapter<CollaboratorActivitiesRecyclerViewAdapter.DataContainer> {
    private Context context;
    public List<Activity> activities;
    public CollaboratorReadCourseFragment parent;
    FragmentManager fm;

    public CollaboratorActivitiesRecyclerViewAdapter(Context context, List<Activity> activities, FragmentManager fm, CollaboratorReadCourseFragment collaboratorReadCourseFragment) {
        this.context = context;
        this.parent = collaboratorReadCourseFragment;
        this.fm = fm;
        if (activities != null)
            this.activities = activities;
        else
            this.activities = new ArrayList<>();
    }

    @NonNull
    @Override
    public CollaboratorActivitiesRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_doctor_activity, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.titleTextView.setText(activities.get(position).getTitle());
        holder.descriptionTextView.setText(activities.get(position).getDescription());
        holder.materialCardView.setOnClickListener(v -> {
            CollaboratorEditActivityFragment collaboratorEditActivityFragment = CollaboratorEditActivityFragment.newInstance(activities.get(position), fm, this);
            collaboratorEditActivityFragment.show(fm, "collaboratorEditActivityFragment");
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
