package com.univ.tours.apa.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Structure;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddStructureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStructureFragment extends DialogFragment {
    FragmentManager fm;
    BrowseStructuresFragment parent;

    EditText structureNameEditText, structureDisciplineEditText, structurePathologiesEditText;
    Button addStructureButton;

    public AddStructureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment CollaboratorAddStructureFragment.
     * @param browseStructuresFragment
     * @param fm
     */
    public static AddStructureFragment newInstance(BrowseStructuresFragment browseStructuresFragment, FragmentManager fm) {
        AddStructureFragment fragment = new AddStructureFragment();
        fragment.parent = browseStructuresFragment;
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
        View view = inflater.inflate(R.layout.fragment_add_structure, container, false);

        structureNameEditText = view.findViewById(R.id.structureNameEditText);
        structureDisciplineEditText = view.findViewById(R.id.structureDisciplineEditText);
        structurePathologiesEditText = view.findViewById(R.id.structurePathologiesEditText);
        addStructureButton = view.findViewById(R.id.addStructureButton);

        addStructureButton.setOnClickListener(v -> {
            checkInputAndSave();
        });

        return view;
    }

    private void checkInputAndSave() {
        ProgressDialog loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        String inputName = structureNameEditText.getText().toString().trim();
        String inputDiscipline = structureDisciplineEditText.getText().toString().trim();
        String inputPathologies = structurePathologiesEditText.getText().toString().trim();
        boolean valid = true;

        if (TextUtils.isEmpty(inputName)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_name), Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(inputDiscipline)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_discipline), Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(inputPathologies)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_pathology_list), Toast.LENGTH_LONG).show();
        }

        if (valid) {
            new Thread(() -> {
                Structure structure = new Structure();
                structure.setName(inputName);
                structure.setDiscipline(inputDiscipline);
                structure.setPathologyList(inputPathologies);
                Long id = MainActivity.db.structureDao().insert(structure);
                structure = MainActivity.db.structureDao().findById(id);
                parent.retainedStructures.add(structure);
                parent.mRecyclerView.post(() -> parent.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }
}