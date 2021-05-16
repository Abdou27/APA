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
import android.widget.EditText;

import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.StructurePickerStructuresRecyclerViewAdapter;
import com.univ.tours.apa.entities.Structure;

import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StructurePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StructurePickerFragment extends DialogFragment {
    EditText parent;
    FragmentManager fm;
    public CollaboratorAddSessionFragment collaboratorAddSessionFragment;

    public RecyclerView mRecyclerView;
    private StructurePickerStructuresRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public StructurePickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StructurePickerFragment.
     */
    public static StructurePickerFragment newInstance(EditText parent, FragmentManager fm, CollaboratorAddSessionFragment collaboratorAddSessionFragment) {
        StructurePickerFragment fragment = new StructurePickerFragment();
        fragment.parent = parent;
        fragment.collaboratorAddSessionFragment = collaboratorAddSessionFragment;
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
        View view = inflater.inflate(R.layout.fragment_structure_picker, container, false);
        new Thread((Runnable) () -> {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(false);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            List<Structure> structureList = db.structureDao().getAll();
            mAdapter = new StructurePickerStructuresRecyclerViewAdapter(this, parent, structureList);
            mRecyclerView.setAdapter(mAdapter);
        }).start();
        return view;
    }
}