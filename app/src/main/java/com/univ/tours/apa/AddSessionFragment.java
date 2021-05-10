package com.univ.tours.apa;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSessionFragment extends DialogFragment {

    EditText dateEditText, durationEditText;
    Button addSessionButton;
    ProgressBar loadingProgressBar;

    public AddSessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddSessionFragment.
     */
    public static AddSessionFragment newInstance() {
        AddSessionFragment fragment = new AddSessionFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_session, container, false);

        dateEditText = view.findViewById(R.id.dateEditText);
        durationEditText = view.findViewById(R.id.durationEditText);
        addSessionButton = view.findViewById(R.id.addSessionButton);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
            dateEditText.setText(sdf.format(myCalendar.getTime()));
        };
        dateEditText.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (dateEditText.getText().toString().isEmpty()
                        || durationEditText.getText().toString().isEmpty()) {
                    addSessionButton.setEnabled(false);
                } else {
                    addSessionButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        dateEditText.addTextChangedListener(textWatcher);
        durationEditText.addTextChangedListener(textWatcher);

        addSessionButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                loadingProgressBar.setVisibility(View.GONE);
                getDialog().dismiss();
            }, 1000);
        });

        return view;
    }
}