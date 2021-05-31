package com.univ.tours.apa.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.BrowseStructuresRecyclerViewAdapter;
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
    public CollaboratorEditSessionFragment collaboratorEditSessionFragment;

    public RecyclerView mRecyclerView;
    private StructurePickerStructuresRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<Structure> retainedStructures;
    String searchQuery = "";
    TextView noStructuresTextView;
    private SearchView searchView;
    private ProgressDialog loadingDialog;

    public StructurePickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StructurePickerFragment.
     */
    public static StructurePickerFragment newInstance(EditText parent, FragmentManager fm, Fragment parentFragment) {
        StructurePickerFragment fragment = new StructurePickerFragment();
        fragment.parent = parent;
        if (parentFragment instanceof CollaboratorAddSessionFragment)
            fragment.collaboratorAddSessionFragment = (CollaboratorAddSessionFragment) parentFragment;
        else if (parentFragment instanceof CollaboratorEditSessionFragment)
            fragment.collaboratorEditSessionFragment = (CollaboratorEditSessionFragment) parentFragment;
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

        noStructuresTextView = view.findViewById(R.id.noStructuresTextView);
        searchView = view.findViewById(R.id.searchView);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

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

        setupStructures();

        return view;
    }

    private void setupStructures() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            retainedStructures = db.structureDao().getAll();
            getActivity().runOnUiThread(() -> {
                mAdapter = new StructurePickerStructuresRecyclerViewAdapter(this, parent, retainedStructures);
                mRecyclerView.setAdapter(mAdapter);
                refreshEmptyTextView();
                loadingDialog.dismiss();
            });
        }).start();
    }

    public void updateStructures() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        retainedStructures.clear();
        new Thread(() -> {
            List<Structure> structureList = db.structureDao().getAll();
            for (Structure structure : structureList) {
                if (isNotInQuery(structure)) {
                    continue;
                } else {
                    retainedStructures.add(structure);
                }
            }
            mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
            getActivity().runOnUiThread(() -> {
                refreshEmptyTextView();
                loadingDialog.dismiss();
            });
        }).start();
    }

    private boolean isNotInQuery(Structure structure) {
        String a = searchQuery.toLowerCase().trim();
        String b = structure.getName().toLowerCase().trim();
        boolean ab = !a.contains(b);
        boolean ba = !b.contains(a);
        return ab && ba;
    }

    public void refreshEmptyTextView() {
        if (retainedStructures.isEmpty()) {
            noStructuresTextView.setVisibility(View.VISIBLE);
        } else {
            noStructuresTextView.setVisibility(View.GONE);
        }
    }

}