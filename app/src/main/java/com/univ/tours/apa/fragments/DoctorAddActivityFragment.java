package com.univ.tours.apa.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorAddActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorAddActivityFragment extends DialogFragment {
    DoctorAssignCourseFragment parentFragment;
    Course course;
    Activity activity;
    FragmentManager fm;

    EditText titleEditText, descriptionEditText;
    Button addActivityButton;
    ProgressBar loadingProgressBar;


    public DoctorAddActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddActivityFragment.
     */
    public static DoctorAddActivityFragment newInstance(DoctorAssignCourseFragment doctorAssignCourseFragment, Course course, FragmentManager fm) {
        DoctorAddActivityFragment fragment = new DoctorAddActivityFragment();
        fragment.parentFragment = doctorAssignCourseFragment;
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
        View view = inflater.inflate(R.layout.fragment_doctor_add_activity, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        addActivityButton = view.findViewById(R.id.addActivityButton);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleEditText.getText().toString().isEmpty()
                        || descriptionEditText.getText().toString().isEmpty()) {
                    addActivityButton.setEnabled(false);
                } else {
                    addActivityButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        titleEditText.addTextChangedListener(textWatcher);
        descriptionEditText.addTextChangedListener(textWatcher);

        addActivityButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            new Thread((Runnable) () -> {
                activity = new Activity();
                activity.setTitle(titleEditText.getText().toString().trim());
                activity.setDescription(descriptionEditText.getText().toString().trim());
                activity.setCourse(course);
                parentFragment.activities.add(activity);
                parentFragment.mRecyclerView.post(() -> parentFragment.mAdapter.notifyDataSetChanged());
                dismiss();
            }).start();
        });

        return view;
    }
}