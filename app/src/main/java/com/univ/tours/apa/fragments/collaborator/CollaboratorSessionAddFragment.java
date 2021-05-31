package com.univ.tours.apa.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.entities.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorAddSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorAddSessionFragment extends DialogFragment {
    CollaboratorEditActivityFragment parentFragment;
    FragmentManager fm;
    Activity activity;
    Session session = new Session();
    public Structure structure;

    EditText dateEditText, timeEditText, durationEditText, structureEditText;
    Button addSessionButton;

    public CollaboratorAddSessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollaboratorAddSessionFragment.
     */
    public static CollaboratorAddSessionFragment newInstance(CollaboratorEditActivityFragment collaboratorEditActivityFragment, Activity activity, FragmentManager fm) {
        CollaboratorAddSessionFragment fragment = new CollaboratorAddSessionFragment();
        fragment.parentFragment = collaboratorEditActivityFragment;
        fragment.activity = activity;
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
        View view = inflater.inflate(R.layout.fragment_collaborator_add_session, container, false);

        dateEditText = view.findViewById(R.id.dateEditText);
        timeEditText = view.findViewById(R.id.timeEditText);
        durationEditText = view.findViewById(R.id.durationEditText);
        structureEditText = view.findViewById(R.id.structureEditText);
        addSessionButton = view.findViewById(R.id.addSessionButton);

        // DatePicker dialog setup
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
            dateEditText.setText(sdf.format(myCalendar.getTime()));
        };
        dateEditText.setOnClickListener(v -> new DatePickerDialog(getContext(), date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        // TimePicker Dialog setup
        timeEditText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
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

        structureEditText.setOnClickListener(v -> {
            StructurePickerFragment structurePickerFragment = StructurePickerFragment.newInstance(structureEditText, fm, this);
            structurePickerFragment.show(fm, "structurePickerFragment");
        });

        addSessionButton.setOnClickListener(v -> {
            checkInputAndSave();
        });

        return view;
    }

    private void checkInputAndSave() {
        ProgressDialog loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        String inputDate = dateEditText.getText().toString().trim();
        String inputTime = timeEditText.getText().toString().trim();
        String inputDuration = durationEditText.getText().toString().trim();
        String inputStructure = structureEditText.getText().toString().trim();
        boolean valid = true;

        Integer duration = null;
        if (TextUtils.isEmpty(inputDuration)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_duration), Toast.LENGTH_LONG).show();
        } else {
            duration = Integer.parseInt(inputDuration);
            if (duration > 480) {
                valid = false;
                Toast.makeText(getContext(), getContext().getString(R.string.session_duration_too_big), Toast.LENGTH_LONG).show();
            } else if (duration <= 0) {
                valid = false;
                Toast.makeText(getContext(), getContext().getString(R.string.session_duration_cant_be_negative), Toast.LENGTH_LONG).show();
            }
        }
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
            if (localTime.plusMinutes(duration).isAfter(LocalTime.of(18, 0))) {
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
        if (TextUtils.isEmpty(inputStructure) || structure == null) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_structure), Toast.LENGTH_LONG).show();
        }

        if (valid) {
            LocalDateTime finalLocalDateTime = localDateTime;
            Integer finalDuration = duration;
            new Thread(() -> {
                session.setDateTime(finalLocalDateTime);
                session.setDuration(finalDuration);
                session.setStructure(structure);
                parentFragment.sessions.add(session);
                parentFragment.mRecyclerView.post(() -> parentFragment.mAdapter.notifyDataSetChanged());
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