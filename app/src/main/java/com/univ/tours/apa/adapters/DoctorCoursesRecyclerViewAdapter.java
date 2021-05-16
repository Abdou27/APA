package com.univ.tours.apa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.fragments.CollaboratorReadCourseFragment;

import java.util.ArrayList;
import java.util.List;

public class DoctorCoursesRecyclerViewAdapter extends RecyclerView.Adapter<DoctorCoursesRecyclerViewAdapter.DataContainer> {
    private Context context;
    List<Course> courses;
    FragmentManager fm;

    public DoctorCoursesRecyclerViewAdapter(Context context, List<Course> courses, FragmentManager fm) {
        this.context = context;
        this.fm = fm;
        if (courses != null)
            this.courses = courses;
        else
            this.courses = new ArrayList<>();
    }

    @NonNull
    @Override
    public DoctorCoursesRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_doctor_course, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.titleTextView.setText(courses.get(position).getTitle());
        holder.descriptionTextView.setText(courses.get(position).getDescription());
        holder.categoryTextView.setText(courses.get(position).getCategory());
        holder.materialCardView.setOnClickListener(v -> {
            // Add functionality to edit courses later
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, categoryTextView;
        MaterialCardView materialCardView;

        public DataContainer(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            categoryTextView = (TextView) itemView.findViewById(R.id.categoryTextView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
        }
    }
}
