package com.univ.tours.apa;

import android.os.Bundle;

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
 * Use the {@link ChoosePatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoosePatientFragment extends Fragment {
    FragmentManager fm;

    public RecyclerView mRecyclerView;
    private PatientsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ChoosePatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChoosePatientFragment.
     */
    public static ChoosePatientFragment newInstance(FragmentManager fm) {
        ChoosePatientFragment fragment = new ChoosePatientFragment();
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
        View view = inflater.inflate(R.layout.fragment_choose_patient, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PatientsRecyclerViewAdapter(getContext(), getDummyData());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private ArrayList<LinkedHashMap<String, String>> getDummyData() {
        ArrayList<LinkedHashMap<String, String>> patients = new ArrayList<>();

        LinkedHashMap<String, String> patient1 = new LinkedHashMap<>();
        patient1.put("name", getString(R.string.name) + " 1");
        patient1.put("age", "20");
        patients.add(patient1);

        LinkedHashMap<String, String> patient2 = new LinkedHashMap<>();
        patient2.put("name", getString(R.string.name) + " 2");
        patient2.put("age", "33");
        patients.add(patient2);

        LinkedHashMap<String, String> patient3 = new LinkedHashMap<>();
        patient3.put("name", getString(R.string.name) + " 3");
        patient3.put("age", "34");
        patients.add(patient3);

        LinkedHashMap<String, String> patient4 = new LinkedHashMap<>();
        patient4.put("name", getString(R.string.name) + " 4");
        patient4.put("age", "46");
        patients.add(patient4);

        LinkedHashMap<String, String> patient5 = new LinkedHashMap<>();
        patient5.put("name", getString(R.string.name) + " 5");
        patient5.put("age", "68");
        patients.add(patient5);

        return patients;
    }
}