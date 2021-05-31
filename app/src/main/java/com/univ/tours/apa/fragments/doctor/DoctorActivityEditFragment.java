package com.univ.tours.apa.fragments.doctor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Activity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorActivityEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorActivityEditFragment extends DialogFragment {
    DoctorCourseAddFragment doctorCourseAddFragment;
    DoctorCourseEditFragment doctorCourseEditFragment;
    Activity activity;

    EditText titleEditText, descriptionEditText;
    Button deleteButton, cancelButton, saveButton;
    ProgressDialog loadingDialog;

    public DoctorActivityEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DoctorEditActivityFragment.
     */
    public static DoctorActivityEditFragment newInstance(Activity activity, Fragment parentFragment) {
        DoctorActivityEditFragment fragment = new DoctorActivityEditFragment();
        fragment.activity = activity;
        if (parentFragment instanceof DoctorCourseAddFragment) {
            fragment.doctorCourseAddFragment = (DoctorCourseAddFragment) parentFragment;
        } else if (parentFragment instanceof DoctorCourseEditFragment) {
            fragment.doctorCourseEditFragment = (DoctorCourseEditFragment) parentFragment;
        }
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
        View view = inflater.inflate(R.layout.fragment_doctor_edit_activity, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        deleteButton = view.findViewById(R.id.deleteButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(v -> dismiss());
        deleteButton.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                .setMessage(R.string.activity_delete_confirmation_text)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> delete())
                .setNegativeButton(android.R.string.cancel, null).show());
        saveButton.setOnClickListener(v -> checkInputAndSave());

        setupEditTexts();

        return view;
    }

    private void checkInputAndSave() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
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
            activity.setTitle(inputTitle);
            activity.setDescription(inputDescription);
            new Thread(() -> {
                if (doctorCourseEditFragment != null) {
                    doctorCourseEditFragment.mRecyclerView.post(() -> doctorCourseEditFragment.mAdapter.notifyDataSetChanged());
                } else if (doctorCourseAddFragment != null) {
                    doctorCourseAddFragment.mRecyclerView.post(() -> doctorCourseAddFragment.mAdapter.notifyDataSetChanged());
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

    private void delete() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            if (doctorCourseEditFragment != null) {
                doctorCourseEditFragment.activities.remove(activity);
                doctorCourseEditFragment.activitiesToDelete.add(activity);
                doctorCourseEditFragment.mRecyclerView.post(() -> doctorCourseEditFragment.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> doctorCourseEditFragment.refreshEmptyTextView());
            } else if (doctorCourseAddFragment != null) {
                doctorCourseAddFragment.activities.remove(activity);
                doctorCourseAddFragment.mRecyclerView.post(() -> doctorCourseAddFragment.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> doctorCourseAddFragment.refreshEmptyTextView());
            }
            getActivity().runOnUiThread(() -> {
                loadingDialog.dismiss();
                dismiss();
            });
        }).start();
    }

    private void setupEditTexts() {
        titleEditText.setText(activity.getTitle());
        descriptionEditText.setText(activity.getDescription());
    }
}