package com.univ.tours.apa;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddStructureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStructureFragment extends DialogFragment {

    EditText structureNameEditText, structureDescriptionEditText, structurePathologiesEditText;
    Button addStructureButton;
    ProgressBar loadingProgressBar;

    public AddStructureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddStructureFragment.
     */
    public static AddStructureFragment newInstance() {
        AddStructureFragment fragment = new AddStructureFragment();
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
        structureDescriptionEditText = view.findViewById(R.id.structureDescriptionEditText);
        structurePathologiesEditText = view.findViewById(R.id.structurePathologiesEditText);
        addStructureButton = view.findViewById(R.id.addStructureButton);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (structureNameEditText.getText().toString().isEmpty()
                        || structureDescriptionEditText.getText().toString().isEmpty()
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
        structureDescriptionEditText.addTextChangedListener(textWatcher);
        structurePathologiesEditText.addTextChangedListener(textWatcher);

        addStructureButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                loadingProgressBar.setVisibility(View.GONE);
                // Do stuff
                getDialog().dismiss();
            }, 1000);
        });

        return view;
    }
}