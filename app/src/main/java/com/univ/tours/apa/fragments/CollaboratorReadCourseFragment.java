package com.univ.tours.apa.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.CollaboratorActivity;
import com.univ.tours.apa.activities.DoctorActivity;
import com.univ.tours.apa.activities.LoginActivity;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.activities.PatientActivity;
import com.univ.tours.apa.adapters.CollaboratorActivitiesRecyclerViewAdapter;
import com.univ.tours.apa.adapters.CollaboratorCoursesRecyclerViewAdapter;
import com.univ.tours.apa.adapters.DoctorActivitiesRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorReadCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorReadCourseFragment extends DialogFragment {
    FragmentManager fm;
    Course course;

    public RecyclerView mRecyclerView;
    public CollaboratorActivitiesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CollaboratorReadCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment collaboratorReadCourseFragment.
     */
    public static CollaboratorReadCourseFragment newInstance(Course course, FragmentManager fm) {
        CollaboratorReadCourseFragment fragment = new CollaboratorReadCourseFragment();
        fragment.course = course;
        fragment.fm = fm;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collaborator_read_course, container, false);

        TextView courseTitleTextView = view.findViewById(R.id.courseTitleTextView);
        courseTitleTextView.setText(course.getTitle());
        TextView courseDescriptionTextView = view.findViewById(R.id.courseDescriptionTextView);
        courseDescriptionTextView.setText(course.getDescription());
        TextView categoryTextView = view.findViewById(R.id.categoryTextView);
        categoryTextView.setText(course.getCategory());
        TextView patientTextView = view.findViewById(R.id.patientTextView);
        patientTextView.setText(course.getPatient().toString());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        new Thread((Runnable) () -> {
            List<Activity> activities = MainActivity.db.activityDao().findByCourses(courses);
            List<Activity> retainedActivities = new ArrayList<>();
            for (Activity activity : activities) {
                if (activity.getCollaborator() == null) {
                    retainedActivities.add(activity);
                }
            }
            getActivity().runOnUiThread(() -> {
                mAdapter = new CollaboratorActivitiesRecyclerViewAdapter(getContext(), retainedActivities, fm, this);
                mRecyclerView.setAdapter(mAdapter);
            });
        }).start();

        return view;
    }
}