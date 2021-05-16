package com.univ.tours.apa.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String APA = "apa";
    CollaboratorEditActivityFragment parentFragment;
    FragmentManager fm;
    Activity activity;
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

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (dateEditText.getText().toString().isEmpty()
                        || timeEditText.getText().toString().isEmpty()
                        || structureEditText.getText().toString().isEmpty()
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
        timeEditText.addTextChangedListener(textWatcher);
        structureEditText.addTextChangedListener(textWatcher);
        durationEditText.addTextChangedListener(textWatcher);

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

        // Launch Time Picker Dialog
        timeEditText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (TimePickerDialog.OnTimeSetListener) (view12, hourOfDay, minute) -> {
                        String hourString, minuteString;
                        if (hourOfDay < 10) {
                            hourString = "0" + hourOfDay;
                        } else {
                            hourString = "" + hourOfDay;
                        }
                        if (minute < 10) {
                            minuteString = "0" + minute;
                        } else {
                            minuteString = "" + minute;
                        }
                        timeEditText.setText(hourString + ":" + minuteString);
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        });

        structureEditText.setOnClickListener(v -> {
            StructurePickerFragment structurePickerFragment = StructurePickerFragment.newInstance(structureEditText, fm, this);
            structurePickerFragment.show(fm, "structurePickerFragment");
        });

        addSessionButton.setOnClickListener(v -> {
            LocalDate localDate = LocalDate.parse(dateEditText.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime localTime = LocalTime.parse(timeEditText.getText().toString());
            LocalDateTime localDateTime = localDate.atTime(localTime);
            Integer duration = Integer.parseInt(durationEditText.getText().toString());
            new Thread((Runnable) () -> {
                SharedPreferences settings;
                settings = getActivity().getSharedPreferences(APA, Context.MODE_PRIVATE);
                Long user_id = settings.getLong("user_id", 0);
                User user = MainActivity.db.userDao().findById(user_id);
                Session session = new Session();
                activity.setCollaborator(user);
                MainActivity.db.activityDao().update(activity);
                session.setActivity(activity);
                session.setDateTime(localDateTime);
                session.setDuration(duration);
                session.setStructure(structure);
                Long id = MainActivity.db.sessionDao().insert(session);
                Session finalSession = MainActivity.db.sessionDao().findById(id);
                getActivity().runOnUiThread(() -> {
                    parentFragment.sessions.add(finalSession);
                    parentFragment.mRecyclerView.post(() -> parentFragment.mAdapter.notifyDataSetChanged());
                    dismiss();
                });
            }).start();
        });

        return view;
    }
}