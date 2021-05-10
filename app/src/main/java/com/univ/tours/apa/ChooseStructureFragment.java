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

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseStructureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseStructureFragment extends DialogFragment {
    FragmentManager fm;

    public RecyclerView mRecyclerView;
    private StructuresRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ChooseStructureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChooseStructureFragment.
     */
    public static ChooseStructureFragment newInstance(FragmentManager fm) {
        ChooseStructureFragment fragment = new ChooseStructureFragment();
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
        View view = inflater.inflate(R.layout.fragment_choose_structure, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StructuresRecyclerViewAdapter(getContext(), getDummyData(), this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private ArrayList<LinkedHashMap<String, String>> getDummyData() {
        ArrayList<LinkedHashMap<String, String>> structures = new ArrayList<>();

        LinkedHashMap<String, String> structure1 = new LinkedHashMap<>();
        structure1.put("title", getString(R.string.title) + " 1");
        structure1.put("discipline", getString(R.string.discipline) + " 1");
        structures.add(structure1);

        LinkedHashMap<String, String> structure2 = new LinkedHashMap<>();
        structure2.put("title", getString(R.string.title) + " 2");
        structure2.put("discipline", getString(R.string.discipline) + " 2");
        structures.add(structure2);

        LinkedHashMap<String, String> structure3 = new LinkedHashMap<>();
        structure3.put("title", getString(R.string.title) + " 3");
        structure3.put("discipline", getString(R.string.discipline) + " 3");
        structures.add(structure3);

        LinkedHashMap<String, String> structure4 = new LinkedHashMap<>();
        structure4.put("title", getString(R.string.title) + " 4");
        structure4.put("discipline", getString(R.string.discipline) + " 4");
        structures.add(structure4);

        LinkedHashMap<String, String> structure5 = new LinkedHashMap<>();
        structure5.put("title", getString(R.string.title) + " 5");
        structure5.put("discipline", getString(R.string.discipline) + " 5");
        structures.add(structure5);

        return structures;
    }
}