package com.univ.tours.apa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCourseFragment extends Fragment {
    FragmentManager fm;

    public RecyclerView mRecyclerView;
    private ActivitiesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    EditText titleEditText, descriptionEditText, categoryEditText;
    Button addActivityButton, addCourseButton;
    ProgressBar loadingProgressBar;

    public AddCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddCourseFragment.
     */
    public static AddCourseFragment newInstance(FragmentManager fm) {
        AddCourseFragment fragment = new AddCourseFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ActivitiesRecyclerViewAdapter(getContext(), getDummyData());
        mRecyclerView.setAdapter(mAdapter);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        categoryEditText = view.findViewById(R.id.categoryEditText);
        addActivityButton = view.findViewById(R.id.addActivityButton);
        addCourseButton = view.findViewById(R.id.addCourseButton);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleEditText.getText().toString().isEmpty()
                        || descriptionEditText.getText().toString().isEmpty()
                        || categoryEditText.getText().toString().isEmpty()) {
                    addCourseButton.setEnabled(false);
                } else {
                    addCourseButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        titleEditText.addTextChangedListener(textWatcher);
        descriptionEditText.addTextChangedListener(textWatcher);
        categoryEditText.addTextChangedListener(textWatcher);

        addActivityButton.setOnClickListener(v -> {
            ChooseActivityFragment chooseActivityFragment = ChooseActivityFragment.newInstance(fm);
            chooseActivityFragment.show(fm, "chooseActivityFragment");
        });

        addCourseButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                loadingProgressBar.setVisibility(View.GONE);
                // Do stuff
            }, 1000);
        });

        return view;
    }

    private ArrayList<LinkedHashMap<String, String>> getDummyData() {
        ArrayList<LinkedHashMap<String, String>> activities = new ArrayList<>();

        LinkedHashMap<String, String> activity1 = new LinkedHashMap<>();
        activity1.put("title", getString(R.string.title) + " 1");
        activity1.put("duration", "2");
        activities.add(activity1);

        LinkedHashMap<String, String> activity2 = new LinkedHashMap<>();
        activity2.put("title", getString(R.string.title) + " 2");
        activity2.put("duration", "3");
        activities.add(activity2);

        return activities;
    }
}