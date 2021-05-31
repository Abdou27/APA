package com.univ.tours.apa.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Session;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientReadSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientReadSessionFragment extends DialogFragment {

    private Session session;

    TextView activityTitleTextView, dateTimeDescriptionTextView, durationTextView, structureTextView, collaboratorTextView;
    Button rescheduleButton, feedbackButton;

    public PatientReadSessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReadSessionFragment.
     */
    public static PatientReadSessionFragment newInstance(Session session) {
        PatientReadSessionFragment fragment = new PatientReadSessionFragment();
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
        View view = inflater.inflate(R.layout.fragment_patient_read_session, container, false);

        activityTitleTextView = view.findViewById(R.id.activityTitleTextView);
        dateTimeDescriptionTextView = view.findViewById(R.id.dateTimeDescriptionTextView);
        durationTextView = view.findViewById(R.id.durationTextView);
        structureTextView = view.findViewById(R.id.structureTextView);
        collaboratorTextView = view.findViewById(R.id.collaboratorTextView);
        rescheduleButton = view.findViewById(R.id.rescheduleButton);
        feedbackButton = view.findViewById(R.id.feedbackButton);


        setupTextViews();

        hideCorrectButton();
        rescheduleButton.setOnClickListener(v -> rescheduleSession());
        feedbackButton.setOnClickListener(v -> giveFeedback());

        return view;
    }

    private void hideCorrectButton() {
        if (session.getDateTime().isBefore(LocalDateTime.now())) {
            feedbackButton.setVisibility(View.VISIBLE);
            rescheduleButton.setVisibility(View.GONE);
        } else {
            feedbackButton.setVisibility(View.GONE);
            rescheduleButton.setVisibility(View.VISIBLE);
        }
    }

    private void giveFeedback() {
        PatientSessionFeedbackFragment patientSessionFeedbackFragment = PatientSessionFeedbackFragment.newInstance(session);
        patientSessionFeedbackFragment.show(getFragmentManager(), "patientSessionFeedbackFragment");
    }

    private void rescheduleSession() {
        PatientRescheduleSessionFragment patientRescheduleSessionFragment = PatientRescheduleSessionFragment.newInstance(session);
        patientRescheduleSessionFragment.show(getFragmentManager(), "patientRescheduleSessionFragment");
    }

    public void setupTextViews() {
        Activity activity = session.getActivity();
        activityTitleTextView.setText(activity.getTitle());
        String date = session.getDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String time = session.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String datetime = date + " " + getContext().getString(R.string.at) + " " + time;
        dateTimeDescriptionTextView.setText(datetime);
        String duration = session.getDuration() + " " + getContext().getString(R.string.minutes);
        durationTextView.setText(duration);
        String structure = session.getStructure().getName();
        structureTextView.setText(structure);
        String collaborator = session.getActivity().getCollaborator().getFullName();
        collaboratorTextView.setText(collaborator);
    }
}