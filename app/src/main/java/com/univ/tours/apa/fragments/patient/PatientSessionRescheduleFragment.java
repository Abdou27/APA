package com.univ.tours.apa.fragments.patient;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Session;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientSessionRescheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientSessionRescheduleFragment extends DialogFragment {
    private Session session;
    EditText dateEditText, timeEditText;
    Button cancelButton, saveButton;
    ProgressDialog loadingDialog;

    TextView noActivitiesTextView, courseTitleTextView, courseDescriptionTextView, categoryTextView, patientTextView;
    List<Activity> retainedActivities;

    public PatientSessionRescheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PatientRescheduleSessionFragment.
     */
    public static PatientSessionRescheduleFragment newInstance(Session session) {
        PatientSessionRescheduleFragment fragment = new PatientSessionRescheduleFragment();
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
        View view = inflater.inflate(R.layout.fragment_patient_reschedule_session, container, false);

        dateEditText = view.findViewById(R.id.dateEditText);
        timeEditText = view.findViewById(R.id.timeEditText);
        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);

        // DatePicker dialog setup
        final Calendar dateCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            dateCalendar.set(Calendar.YEAR, year);
            dateCalendar.set(Calendar.MONTH, month);
            dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
            dateEditText.setText(sdf.format(dateCalendar.getTime()));
        };
        dateEditText.setOnClickListener(v -> new DatePickerDialog(getContext(), date,
                dateCalendar.get(Calendar.YEAR),
                dateCalendar.get(Calendar.MONTH),
                dateCalendar.get(Calendar.DAY_OF_MONTH)).show());

        // TimePicker Dialog setup
        final Calendar timeCalendar = Calendar.getInstance();
        timeEditText.setOnClickListener(v -> {
            int mHour = timeCalendar.get(Calendar.HOUR_OF_DAY);
            int mMinute = timeCalendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (view12, hourOfDay, minute) -> {
                        String hourString, minuteString;
                        hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                        minuteString = minute < 10 ? "0" + minute : "" + minute;
                        String outputString = hourString + ":" + minuteString;
                        timeEditText.setText(outputString);
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        });

        setupEditTexts();

        cancelButton.setOnClickListener(v -> dismiss());
        saveButton.setOnClickListener(v -> checkInputAndSave());

        return view;
    }

    private void checkInputAndSave() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        boolean valid = true;
        String inputDate = dateEditText.getText().toString().trim();
        String inputTime = timeEditText.getText().toString().trim();

        LocalDate localDate = null;
        if (TextUtils.isEmpty(inputDate)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_date), Toast.LENGTH_LONG).show();
        } else {
            localDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        LocalTime localTime = null;
        if (TextUtils.isEmpty(inputTime)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_time), Toast.LENGTH_LONG).show();
        } else {
            localTime = LocalTime.parse(inputTime);
            if (localTime.isBefore(LocalTime.of(8, 0))) {
                valid = false;
                Toast.makeText(getContext(), getContext().getString(R.string.time_cant_be_before_8am), Toast.LENGTH_LONG).show();
            }
            if (localTime.plusMinutes(session.getDuration()).isAfter(LocalTime.of(18, 0))) {
                valid = false;
                Toast.makeText(getContext(), getContext().getString(R.string.session_cant_go_past_6pm), Toast.LENGTH_LONG).show();
            }
        }
        LocalDateTime localDateTime = null;
        if ((localDate != null) && (localTime != null)) {
            localDateTime = localDate.atTime(localTime);
            if (localDateTime.isBefore(LocalDateTime.now())) {
                valid = false;
                Toast.makeText(getContext(), getContext().getString(R.string.session_invalid_time), Toast.LENGTH_LONG).show();
            }
        }

        if (valid) {
            LocalDateTime finalLocalDateTime = localDateTime;
            session.setRescheduledDateTime(finalLocalDateTime);
            new Thread(() -> {
                MainActivity.db.sessionDao().update(session);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.session_reschedule_request_send), Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }

    private void setupEditTexts() {
        dateEditText.setText(session.getDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        timeEditText.setText(session.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}