package com.univ.tours.apa.fragments.doctor;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorActivityAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorActivityAddFragment extends DialogFragment {
    DoctorCourseAddFragment doctorCourseAddFragment;
    DoctorCourseEditFragment doctorCourseEditFragment;
    Course course;
    Activity activity;
    FragmentManager fm;

    EditText titleEditText, descriptionEditText;
    Button addActivityButton;


    public DoctorActivityAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddActivityFragment.
     */
    public static DoctorActivityAddFragment newInstance(Fragment parentFragment, Course course, FragmentManager fm) {
        DoctorActivityAddFragment fragment = new DoctorActivityAddFragment();
        if (parentFragment instanceof DoctorCourseAddFragment)
            fragment.doctorCourseAddFragment = (DoctorCourseAddFragment) parentFragment;
        else if (parentFragment instanceof DoctorCourseEditFragment)
            fragment.doctorCourseEditFragment = (DoctorCourseEditFragment) parentFragment;
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

        addActivityButton.setOnClickListener(v -> {
            checkInputAndSave();
        });

        return view;
    }

    private void checkInputAndSave() {
        ProgressDialog loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        String inputTitle = titleEditText.getText().toString().trim();
        String inputDescription = descriptionEditText.getText().toString().trim();
        boolean valid = true;

        if (TextUtils.isEmpty(inputTitle)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_title), Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(inputDescription)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_description), Toast.LENGTH_LONG).show();
        }

        if (valid) {
            activity = new Activity();
            activity.setTitle(inputTitle);
            activity.setDescription(inputDescription);
            activity.setCourse(course);
            new Thread(() -> {
                if (doctorCourseAddFragment != null) {
                    doctorCourseAddFragment.activities.add(activity);
                    doctorCourseAddFragment.mRecyclerView.post(() -> doctorCourseAddFragment.mAdapter.notifyDataSetChanged());
                    getActivity().runOnUiThread(() -> doctorCourseAddFragment.refreshEmptyTextView());
                } else if (doctorCourseEditFragment != null) {
                    doctorCourseEditFragment.activities.add(activity);
                    doctorCourseEditFragment.mRecyclerView.post(() -> doctorCourseEditFragment.mAdapter.notifyDataSetChanged());
                    getActivity().runOnUiThread(() -> doctorCourseEditFragment.refreshEmptyTextView());
                }
                getActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }
}