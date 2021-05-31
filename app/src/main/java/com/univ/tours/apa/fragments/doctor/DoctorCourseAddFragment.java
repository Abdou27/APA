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

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Use the {@link DoctorAddCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorAddCourseFragment extends DialogFragment {
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

    public DoctorAddCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddCourseFragment.
     */
    public static DoctorAddCourseFragment newInstance(User patient, FragmentManager fm, android.app.Activity context, DoctorReadPatientFragment parent) {
        DoctorAddCourseFragment fragment = new DoctorAddCourseFragment();
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
        View view = inflater.inflate(R.layout.fragment_doctor_add_course, container, false);
        course = new Course();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DoctorActivitiesRecyclerViewAdapter(getContext(), activities, this, fm);
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
            checkInputAndSave();
        });

        refreshEmptyTextView();

        return view;
    }

    private void checkInputAndSave() {
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
            course.setPatient(patient);
            SharedPreferences settings;
            settings = this.getActivity().getSharedPreferences(APA, Context.MODE_PRIVATE);
            Long user_id = settings.getLong("user_id", 0);
            new Thread(() -> {
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
                getActivity().runOnUiThread(() -> {
                    parent.refreshEmptyTextView();
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
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