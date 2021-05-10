package com.univ.tours.apa;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
 * Use the {@link AddActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddActivityFragment extends DialogFragment {
    FragmentManager fm;

    EditText titleEditText, descriptionEditText, durationEditText, structureEditText;
    Button addActivityButton;
    ProgressBar loadingProgressBar;


    public AddActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddActivityFragment.
     */
    public static AddActivityFragment newInstance(FragmentManager fm) {
        AddActivityFragment fragment = new AddActivityFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_activity, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        durationEditText = view.findViewById(R.id.durationEditText);
        structureEditText = view.findViewById(R.id.structureEditText);
        addActivityButton = view.findViewById(R.id.addActivityButton);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleEditText.getText().toString().isEmpty()
                        || descriptionEditText.getText().toString().isEmpty()
                        || durationEditText.getText().toString().isEmpty()) {
                    addActivityButton.setEnabled(false);
                } else {
                    addActivityButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        titleEditText.addTextChangedListener(textWatcher);
        descriptionEditText.addTextChangedListener(textWatcher);
        durationEditText.addTextChangedListener(textWatcher);

        structureEditText.setOnClickListener(v -> {
            ChooseStructureFragment chooseStructureFragment = ChooseStructureFragment.newInstance(fm);
            chooseStructureFragment.show(fm, "chooseStructureFragment");
        });

        addActivityButton.setOnClickListener(v -> {
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