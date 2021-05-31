package com.univ.tours.apa.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.DoctorActivitiesRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorEditCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorEditCourseFragment extends DialogFragment {
    Course course;
    List<Activity> activities, activitiesToDelete;
    DoctorReadPatientFragment doctorReadPatientFragment;

    public RecyclerView mRecyclerView;
    public DoctorActivitiesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView noActivitiesTextView;
    EditText titleEditText, descriptionEditText, categoryEditText;
    Button addActivityButton, deleteButton, cancelButton, saveButton;
    ProgressDialog loadingDialog;

    public DoctorEditCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DoctorEditCourseFragment.
     */
    public static DoctorEditCourseFragment newInstance(Course course, DoctorReadPatientFragment doctorReadPatientFragment) {
        DoctorEditCourseFragment fragment = new DoctorEditCourseFragment();
        fragment.course = course;
        fragment.doctorReadPatientFragment = doctorReadPatientFragment;
        fragment.activitiesToDelete = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_doctor_edit_course, container, false);

        noActivitiesTextView = view.findViewById(R.id.noActivitiesTextView);
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        categoryEditText = view.findViewById(R.id.categoryEditText);
        deleteButton = view.findViewById(R.id.deleteButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);
        addActivityButton = view.findViewById(R.id.addActivityButton);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        addActivityButton.setOnClickListener(v -> {
            DoctorAddActivityFragment doctorAddActivityFragment = DoctorAddActivityFragment.newInstance(this, course, getFragmentManager());
            doctorAddActivityFragment.show(getFragmentManager(), "doctorAddActivityFragment");
        });

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.course_delete_confirmation_text)
                    .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> deleteCourse())
                    .setNegativeButton(android.R.string.cancel, null).show();
        });

        saveButton.setOnClickListener(v -> {
            checkInputAndSaveCourse();
        });

        titleEditText.setText(course.getTitle());
        descriptionEditText.setText(course.getDescription());
        categoryEditText.setText(course.getCategory());
        setupActivities();

        return view;
    }

    private void setupActivities() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            activities = MainActivity.db.activityDao().findByCourseAndNoCollaborator(course);
            Log.d("TAG", "setupActivities: " + activities.size());
            mAdapter = new DoctorActivitiesRecyclerViewAdapter(getContext(), activities, this, getFragmentManager());
            mRecyclerView.setAdapter(mAdapter);
            refreshEmptyTextView();
            loadingDialog.dismiss();
        }).start();
    }

    private void checkInputAndSaveCourse() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        String inputTitle = titleEditText.getText().toString().trim();
        String inputDescription = descriptionEditText.getText().toString().trim();
        String inputCategory = categoryEditText.getText().toString().trim();
        boolean valid = true;

        if (TextUtils.isEmpty(inputTitle)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_title), Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(inputDescription)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_description), Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(inputCategory)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_category), Toast.LENGTH_LONG).show();
        }
        if (activities.isEmpty()) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_activities), Toast.LENGTH_LONG).show();
        }

        if (valid) {
            course.setTitle(inputTitle);
            course.setDescription(inputDescription);
            course.setCategory(inputCategory);
            new Thread(() -> {
                MainActivity.db.courseDao().update(course);
                for (Activity activity : activities) {
                    activity.setCourse(course);
                    if (activity.getId() != null) {
                        MainActivity.db.activityDao().update(activity);
                    } else {
                        MainActivity.db.activityDao().insert(activity);
                    }
                }
                for (Activity activity : activitiesToDelete) {
                    if (activity.getId() != null) {
                        MainActivity.db.activityDao().delete(activity);
                    }
                }
                activitiesToDelete.clear();
                doctorReadPatientFragment.mRecyclerView.post(() -> doctorReadPatientFragment.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }

    private void deleteCourse() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            List<Course> courses = new ArrayList<>();
            courses.add(course);
            MainActivity.db.activityDao().deleteByCourses(courses);
            MainActivity.db.courseDao().delete(course);
            doctorReadPatientFragment.courses.remove(course);
            doctorReadPatientFragment.mRecyclerView.post(() -> doctorReadPatientFragment.mAdapter.notifyDataSetChanged());
            getActivity().runOnUiThread(() -> {
                doctorReadPatientFragment.refreshEmptyTextView();
                loadingDialog.dismiss();
                dismiss();
            });
        }).start();
    }

    public void refreshEmptyTextView() {
        if (activities.isEmpty()) {
            noActivitiesTextView.setVisibility(View.VISIBLE);
        } else {
            noActivitiesTextView.setVisibility(View.GONE);
        }
    }
}