package com.univ.tours.apa.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Session;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientSessionFeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientSessionFeedbackFragment extends DialogFragment {
    Session session;

    SeekBar seekBar;
    EditText feedbackEditText;
    Button submitButton;
    private ProgressDialog loadingDialog;

    public PatientSessionFeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PatientSessionFeedbackFragment.
     */
    public static PatientSessionFeedbackFragment newInstance(Session session) {
        PatientSessionFeedbackFragment fragment = new PatientSessionFeedbackFragment();
        fragment.session = session;
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
        View view = inflater.inflate(R.layout.fragment_patient_session_feedback, container, false);

        seekBar = view.findViewById(R.id.seekBar);
        feedbackEditText = view.findViewById(R.id.feedbackEditText);
        submitButton = view.findViewById(R.id.submitButton);

        setup();

        submitButton.setOnClickListener(v -> checkInputAndSave());

        return view;
    }

    private void checkInputAndSave() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        boolean valid = true;
        String inputFeedback = feedbackEditText.getText().toString().trim();
        Integer inputCompletionRate = seekBar.getProgress();

        if (valid) {
            session.setCompletionRate(inputCompletionRate);
            session.setPatientFeedback(inputFeedback);
            new Thread(() -> {
                MainActivity.db.sessionDao().update(session);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.feedback_submitted), Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }

    private void setup() {
        if (session.getCompletionRate() != null) {
            seekBar.setProgress(session.getCompletionRate());
        } else {
            seekBar.setProgress(0);
        }
        if (!TextUtils.isEmpty(session.getPatientFeedback())) {
            feedbackEditText.setText(session.getPatientFeedback());
        }
    }
}