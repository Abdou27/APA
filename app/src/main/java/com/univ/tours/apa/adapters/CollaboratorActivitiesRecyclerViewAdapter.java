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
import com.univ.tours.apa.fragments.collaborator.CollaboratorActivityEditFragment;
import com.univ.tours.apa.fragments.collaborator.CollaboratorCourseReadFragment;

import java.util.ArrayList;
import java.util.List;

public class CollaboratorActivitiesRecyclerViewAdapter extends RecyclerView.Adapter<CollaboratorActivitiesRecyclerViewAdapter.DataContainer> {
    private Context context;
    public List<Activity> activities;
    public CollaboratorCourseReadFragment parent;
    FragmentManager fm;

    public CollaboratorActivitiesRecyclerViewAdapter(Context context, List<Activity> activities, FragmentManager fm, CollaboratorCourseReadFragment collaboratorCourseReadFragment) {
        this.context = context;
        this.parent = collaboratorCourseReadFragment;
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
        Activity activity = activities.get(position);
        String title = activity.getTitle();
        if (activity.getCollaborator() == null) {
            title = title + " " + context.getString(R.string.free_activity);
        }
        holder.titleTextView.setText(title);
        holder.descriptionTextView.setText(activity.getDescription());
        holder.materialCardView.setOnClickListener(v -> {
            CollaboratorActivityEditFragment collaboratorActivityEditFragment = CollaboratorActivityEditFragment.newInstance(activity, fm, this);
            collaboratorActivityEditFragment.show(fm, "collaboratorEditActivityFragment");
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
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
        }
    }
}
