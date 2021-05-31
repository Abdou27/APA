package com.univ.tours.apa.fragments.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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
 * Use the {@link StructureEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StructureEditFragment extends DialogFragment {
    Structure structure;
    StructureBrowseFragment structureBrowseFragment;

    EditText structureNameEditText, structureDisciplineEditText, structurePathologiesEditText;
    Button deleteButton, cancelButton, saveButton;
    ProgressDialog loadingDialog;

    public StructureEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditStructureFragment.
     */
    public static StructureEditFragment newInstance(Structure structure, StructureBrowseFragment structureBrowseFragment) {
        StructureEditFragment fragment = new StructureEditFragment();
        fragment.structure = structure;
        fragment.structureBrowseFragment = structureBrowseFragment;
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
        View view = inflater.inflate(R.layout.fragment_edit_structure, container, false);

        structureNameEditText = view.findViewById(R.id.structureNameEditText);
        structureDisciplineEditText = view.findViewById(R.id.structureDisciplineEditText);
        structurePathologiesEditText = view.findViewById(R.id.structurePathologiesEditText);
        deleteButton = view.findViewById(R.id.deleteButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);

        structureNameEditText.setText(structure.getName());
        structureDisciplineEditText.setText(structure.getDiscipline());
        structurePathologiesEditText.setText(structure.getPathologyList());

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
        
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.structure_delete_confirmation_text)
                    .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> deleteStructure())
                    .setNegativeButton(android.R.string.cancel, null).show();
        });

        saveButton.setOnClickListener(v -> {
            checkInputAndSaveStructure();
        });

        return view;
    }

    private void checkInputAndSaveStructure() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
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
            structure.setName(inputName);
            structure.setDiscipline(inputDiscipline);
            structure.setPathologyList(inputPathologies);
            new Thread(() -> {
                MainActivity.db.structureDao().update(structure);
                structureBrowseFragment.mRecyclerView.post(() -> structureBrowseFragment.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }

    private void deleteStructure() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            MainActivity.db.structureDao().delete(structure);
            getActivity().runOnUiThread(() -> {
                structureBrowseFragment.updateStructures();
                loadingDialog.dismiss();
                dismiss();
            });
        }).start();
    }
}