package com.univ.tours.apa.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.DoctorActivitiesRecyclerViewAdapter;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorAssignCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorAssignCourseFragment extends DialogFragment {
    private static final String APA = "apa";
    User patient;
    FragmentManager fm;
    Course course;
    android.app.Activity context;
    DoctorReadPatientFragment parent;

    public RecyclerView mRecyclerView;
    public DoctorActivitiesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<Activity> activities = new ArrayList<>();

    TextView noActivitiesTextView;
    EditText titleEditText, descriptionEditText, categoryEditText;
    Button addActivityButton, addCourseButton;
    ProgressDialog loadingDialog;

    public DoctorAssignCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddCourseFragment.
     */
    public static DoctorAssignCourseFragment newInstance(User patient, FragmentManager fm, android.app.Activity context, DoctorReadPatientFragment parent) {
        DoctorAssignCourseFragment fragment = new DoctorAssignCourseFragment();
        fragment.patient = patient;
        fragment.fm = fm;
        fragment.context = context;
        fragment.parent = parent;
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
        View view = inflater.inflate(R.layout.fragment_doctor_assign_course, container, false);
        course = new Course();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DoctorActivitiesRecyclerViewAdapter(getContext(), activities);
        mRecyclerView.setAdapter(mAdapter);

        noActivitiesTextView = view.findViewById(R.id.noActivitiesTextView);
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        categoryEditText = view.findViewById(R.id.categoryEditText);
        addActivityButton = view.findViewById(R.id.addActivityButton);
        addCourseButton = view.findViewById(R.id.addCourseButton);

        addActivityButton.setOnClickListener(v -> {
            DoctorAddActivityFragment doctorAddActivityFragment = DoctorAddActivityFragment.newInstance(this, course, fm);
            doctorAddActivityFragment.show(fm, "doctorAddActivityFragment");
        });

        addCourseButton.setOnClickListener(v -> {
            checkInputAndAddCourse();
        });

        refreshEmptyTextView();

        return view;
    }

    private void checkInputAndAddCourse() {
        if (titleEditText.getText().toString().trim().isEmpty()
                || descriptionEditText.getText().toString().trim().isEmpty()
                || categoryEditText.getText().toString().trim().isEmpty()
                || activities.size() == 0) {
            Toast.makeText(getContext(), getString(R.string.incomplete_data), Toast.LENGTH_LONG).show();
        } else {
            loadingDialog = ProgressDialog.show(getContext(), "Loading", getContext().getString(R.string.loading_progress_bar_text), true);
            course.setTitle(titleEditText.getText().toString().trim());
            course.setDescription(descriptionEditText.getText().toString().trim());
            course.setCategory(categoryEditText.getText().toString().trim());
            course.setPatient(patient);
            SharedPreferences settings;
            settings = this.getActivity().getSharedPreferences(APA, Context.MODE_PRIVATE);
            Long user_id = settings.getLong("user_id", 0);
            new Thread((Runnable) () -> {
                User doctor = MainActivity.db.userDao().findById(user_id);
                course.setDoctor(doctor);
                Long id = MainActivity.db.courseDao().insert(course);
                course.setId(id);
                for (Activity activity : activities) {
                    activity.setCourse(course);
                    MainActivity.db.activityDao().insert(activity);
                }
                parent.courses.add(course);
                parent.mRecyclerView.post(() -> parent.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> loadingDialog.dismiss());
            }).start();
            dismiss();
        }
    }

    public void refreshEmptyTextView() {
        if (activities.isEmpty()) {
            noActivitiesTextView.setVisibility(View.VISIBLE);
        } else {
            noActivitiesTextView.setVisibility(View.GONE);
        }
    }
}