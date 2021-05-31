package com.univ.tours.apa.fragments.doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.DoctorCoursesRecyclerViewAdapter;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorReadPatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorReadPatientFragment extends DialogFragment {
    private static final String APA = "apa";
    FragmentManager fm;
    android.app.Activity context;
    User patient;
    public List<Course> courses;
    private ProgressDialog loadingDialog;

    TextView noCoursesTextView;
    public RecyclerView mRecyclerView;
    public DoctorCoursesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public DoctorReadPatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DoctorReadPatientFragment.
     */
    public static DoctorReadPatientFragment newInstance(User patient, FragmentManager fm, android.app.Activity context) {
        DoctorReadPatientFragment fragment = new DoctorReadPatientFragment();
        fragment.patient = patient;
        fragment.context = context;
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
        View view = inflater.inflate(R.layout.fragment_doctor_read_patient, container, false);

        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);

        noCoursesTextView = view.findViewById(R.id.noCoursesTextView);
        TextView lastNameTextView = view.findViewById(R.id.lastNameTextView);
        TextView firstNameTextView = view.findViewById(R.id.firstNameTextView);
        TextView ageTextView = view.findViewById(R.id.ageTextView);
        TextView birthdayTextView = view.findViewById(R.id.birthdayTextView);
        TextView phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView);
        Button assignCourseButton = view.findViewById(R.id.assignCourseButton);
        assignCourseButton.setOnClickListener(v -> {
            DoctorCourseAddFragment doctorCourseAddFragment = DoctorCourseAddFragment.newInstance(patient, fm, context, this);
            doctorCourseAddFragment.show(fm, "doctorAddCourseFragment");
        });

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        lastNameTextView.setText(patient.getLastName());
        firstNameTextView.setText(patient.getFirstName());
        String ageText = patient.getAge() + " " + getString(R.string.years);
        ageTextView.setText(ageText);
        birthdayTextView.setText(patient.getBirthday().toString());
        phoneNumberTextView.setText(patient.getPhoneNumber());

        SharedPreferences settings;
        settings = getActivity().getSharedPreferences(APA, Context.MODE_PRIVATE);
        Long user_id = settings.getLong("user_id", 0);
        new Thread(() -> {
            User doctor = MainActivity.db.userDao().findById(user_id);
            courses = MainActivity.db.courseDao().findByPatientForDoctor(patient, doctor);
            getActivity().runOnUiThread(() -> {
                mAdapter = new DoctorCoursesRecyclerViewAdapter(context, courses, fm, this);
                mRecyclerView.setAdapter(mAdapter);
                refreshEmptyTextView();
                loadingDialog.dismiss();
            });
        }).start();

        return view;
    }

    public void refreshEmptyTextView() {
        if (courses.isEmpty()) {
            noCoursesTextView.setVisibility(View.VISIBLE);
        } else {
            noCoursesTextView.setVisibility(View.GONE);
        }
    }
}