package com.univ.tours.apa.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.BrowseStructuresRecyclerViewAdapter;
import com.univ.tours.apa.entities.Structure;

import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorBrowseStructuresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorBrowseStructuresFragment extends Fragment {
    FragmentManager fm;
    List<Structure> retainedStructures;

    public RecyclerView mRecyclerView;
    public BrowseStructuresRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String searchQuery = "";

    private FloatingActionButton floatingActionButton;

    public CollaboratorBrowseStructuresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollaboratorBrowseStructuresFragment.
     */
    public static CollaboratorBrowseStructuresFragment newInstance(FragmentManager fm) {
        CollaboratorBrowseStructuresFragment fragment = new CollaboratorBrowseStructuresFragment();
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
        View view = inflater.inflate(R.layout.fragment_collaborator_browse_structures, container, false);

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                Log.d("TAG", "onQueryTextSubmit: " + searchQuery);
                updateStructures();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                Log.d("TAG", "onQueryTextChange: " + searchQuery);
                updateStructures();
                return false;
            }
        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        new Thread((Runnable) () -> {
            retainedStructures = db.structureDao().getAll();
            mAdapter = new BrowseStructuresRecyclerViewAdapter(fm, retainedStructures);
            mRecyclerView.setAdapter(mAdapter);
        }).start();

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            AddStructureFragment addStructureFragment = AddStructureFragment.newInstance(this, fm);
            addStructureFragment.show(fm, "collaboratorAddStructureFragment");
        });

        return view;
    }

    private void updateStructures() {
        new Thread((Runnable) () -> {
            retainedStructures.clear();
            List<Structure> structureList = db.structureDao().getAll();
            for (Structure structure : structureList) {
                if (isNotInQuery(structure)) {
                    continue;
                } else {
                    retainedStructures.add(structure);
                }
            }
            mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
        }).start();
    }

    private boolean isNotInQuery(Structure structure) {
        String a = searchQuery.toLowerCase().trim();
        String b = structure.getName().toLowerCase().trim();
        boolean ab = !a.contains(b);
        boolean ba = !b.contains(a);
        return ab && ba;
    }
}