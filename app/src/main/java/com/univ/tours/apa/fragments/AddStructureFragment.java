package com.univ.tours.apa.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Structure;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorAddStructureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorAddStructureFragment extends DialogFragment {
    FragmentManager fm;
    CollaboratorBrowseStructuresFragment parent;

    EditText structureNameEditText, structureDisciplineEditText, structurePathologiesEditText;
    Button addStructureButton;

    public CollaboratorAddStructureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment CollaboratorAddStructureFragment.
     * @param collaboratorBrowseStructuresFragment
     * @param fm
     */
    public static CollaboratorAddStructureFragment newInstance(CollaboratorBrowseStructuresFragment collaboratorBrowseStructuresFragment, FragmentManager fm) {
        CollaboratorAddStructureFragment fragment = new CollaboratorAddStructureFragment();
        fragment.parent = collaboratorBrowseStructuresFragment;
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
        View view = inflater.inflate(R.layout.fragment_collaborator_add_structure, container, false);

        structureNameEditText = view.findViewById(R.id.structureNameEditText);
        structureDisciplineEditText = view.findViewById(R.id.structureDisciplineEditText);
        structurePathologiesEditText = view.findViewById(R.id.structurePathologiesEditText);
        addStructureButton = view.findViewById(R.id.addStructureButton);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (structureNameEditText.getText().toString().isEmpty()
                        || structureDisciplineEditText.getText().toString().isEmpty()
                        || structurePathologiesEditText.getText().toString().isEmpty()) {
                    addStructureButton.setEnabled(false);
                } else {
                    addStructureButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        structureNameEditText.addTextChangedListener(textWatcher);
        structureDisciplineEditText.addTextChangedListener(textWatcher);
        structurePathologiesEditText.addTextChangedListener(textWatcher);

        addStructureButton.setOnClickListener(v -> {
            ProgressDialog loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
            new Thread((Runnable) () -> {
                Structure structure = new Structure();
                structure.setName(structureNameEditText.getText().toString().trim());
                structure.setDiscipline(structureDisciplineEditText.getText().toString().trim());
                structure.setPathologyList(structurePathologiesEditText.getText().toString().trim());
                Long id = MainActivity.db.structureDao().insert(structure);
                structure = MainActivity.db.structureDao().findById(id);
                parent.retainedStructures.add(structure);
                parent.mRecyclerView.post(() -> parent.mAdapter.notifyDataSetChanged());
                loadingDialog.dismiss();
            }).start();
            dismiss();
        });

        return view;
    }
}