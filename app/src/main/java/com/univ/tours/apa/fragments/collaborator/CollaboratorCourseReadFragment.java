package com.univ.tours.apa.fragments.collaborator;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.CollaboratorActivitiesRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorCourseReadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorCourseReadFragment extends DialogFragment {
    FragmentManager fm;
    Course course;

    public RecyclerView mRecyclerView;
    public CollaboratorActivitiesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog loadingDialog;

    TextView noActivitiesTextView, courseTitleTextView, courseDescriptionTextView, categoryTextView, patientTextView;
    List<Activity> retainedActivities;

    public CollaboratorCourseReadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment collaboratorReadCourseFragment.
     */
    public static CollaboratorCourseReadFragment newInstance(Course course, FragmentManager fm) {
        CollaboratorCourseReadFragment fragment = new CollaboratorCourseReadFragment();
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

        noActivitiesTextView = view.findViewById(R.id.noActivitiesTextView);
        courseTitleTextView = view.findViewById(R.id.courseTitleTextView);
        courseDescriptionTextView = view.findViewById(R.id.courseDescriptionTextView);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        patientTextView = view.findViewById(R.id.patientTextView);

        courseTitleTextView.setText(course.getTitle());
        courseDescriptionTextView.setText(course.getDescription());
        categoryTextView.setText(course.getCategory());
        patientTextView.setText(course.getPatient().toString());

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setupActivities();

        return view;
    }

    private void setupActivities() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            List<Activity> activities = MainActivity.db.activityDao().findByCourse(course);
            retainedActivities = new ArrayList<>();
            for (Activity activity : activities) {
                if (activity.getCollaborator() == null || activity.getCollaborator().getId().equals(MainActivity.getLoggedInUserId())) {
                    retainedActivities.add(activity);
                }
            }
            getActivity().runOnUiThread(() -> {
                mAdapter = new CollaboratorActivitiesRecyclerViewAdapter(getContext(), retainedActivities, fm, this);
                mRecyclerView.setAdapter(mAdapter);
                refreshEmptyTextView();
                loadingDialog.dismiss();
            });
        }).start();
    }

    public void refreshEmptyTextView() {
        if (retainedActivities.isEmpty()) {
            noActivitiesTextView.setVisibility(View.VISIBLE);
        } else {
            noActivitiesTextView.setVisibility(View.GONE);
        }
    }
}