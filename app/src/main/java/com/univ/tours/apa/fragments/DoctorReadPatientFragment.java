package com.univ.tours.apa.fragments;

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
import com.univ.tours.apa.adapters.CollaboratorCoursesRecyclerViewAdapter;
import com.univ.tours.apa.adapters.DoctorCoursesRecyclerViewAdapter;
import com.univ.tours.apa.adapters.DoctorPatientsRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.User;

import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorReadPatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorReadPatientFragment extends DialogFragment {
    FragmentManager fm;
    android.app.Activity context;
    User patient;

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

        TextView lastNameTextView = view.findViewById(R.id.lastNameTextView);
        lastNameTextView.setText(patient.getLastName());
        TextView firstNameTextView = view.findViewById(R.id.firstNameTextView);
        firstNameTextView.setText(patient.getFirstName());
        TextView ageTextView = view.findViewById(R.id.ageTextView);
        ageTextView.setText(patient.getAge() + " " + getString(R.string.years));
        TextView birthdayTextView = view.findViewById(R.id.birthdayTextView);
        birthdayTextView.setText(patient.getBirthday().toString());
        TextView phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView);
        phoneNumberTextView.setText(patient.getPhoneNumber());
        Button assignCourseButton = view.findViewById(R.id.assignCourseButton);
        assignCourseButton.setOnClickListener(v -> {
            DoctorAssignCourseFragment doctorAssignCourseFragment = DoctorAssignCourseFragment.newInstance(patient, fm, context);
            doctorAssignCourseFragment.show(fm, "doctorAddCourseFragment");
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        new Thread((Runnable) () -> {
            List<Course> courses = MainActivity.db.courseDao().findByPatient(patient);
            getActivity().runOnUiThread(() -> {
                mAdapter = new DoctorCoursesRecyclerViewAdapter(context, courses, fm);
                mRecyclerView.setAdapter(mAdapter);
            });
        }).start();

        return view;
    }
}