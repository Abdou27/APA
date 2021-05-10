package com.univ.tours.apa;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseActivityFragment extends DialogFragment {
    FragmentManager fm;

    public RecyclerView mRecyclerView;
    private ActivitiesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ChooseActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChooseActivityFragment.
     */
    public static ChooseActivityFragment newInstance(FragmentManager fm) {
        ChooseActivityFragment fragment = new ChooseActivityFragment();
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
        View view = inflater.inflate(R.layout.fragment_choose_activity, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ActivitiesRecyclerViewAdapter(getContext(), getDummyData());
        mRecyclerView.setAdapter(mAdapter);

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

        LinkedHashMap<String, String> activity3 = new LinkedHashMap<>();
        activity3.put("title", getString(R.string.title) + " 3");
        activity3.put("duration", "3");
        activities.add(activity3);

        LinkedHashMap<String, String> activity4 = new LinkedHashMap<>();
        activity4.put("title", getString(R.string.title) + " 4");
        activity4.put("duration", "4");
        activities.add(activity4);

        LinkedHashMap<String, String> activity5 = new LinkedHashMap<>();
        activity5.put("title", getString(R.string.title) + " 5");
        activity5.put("duration", "1");
        activities.add(activity5);

        return activities;
    }
}