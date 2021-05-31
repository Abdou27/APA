package com.univ.tours.apa.fragments.collaborator;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.fragments.common.StructurePickFragment;

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
 * Use the {@link CollaboratorSessionEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorSessionEditFragment extends DialogFragment {

    private Session session;
    private List<Session> sessions;
    private CollaboratorActivityEditFragment collaboratorActivityEditFragment;
    public Structure structure;

    EditText dateEditText, timeEditText, durationEditText, structureEditText;
    Button deleteButton, cancelButton, saveButton;
    ProgressDialog loadingDialog;

    public CollaboratorSessionEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollaboratorEditSessionFragment.
     */
    public static CollaboratorSessionEditFragment newInstance(Session session, List<Session> sessions, Fragment parentFragment) {
        CollaboratorSessionEditFragment fragment = new CollaboratorSessionEditFragment();
        fragment.session = session;
        fragment.sessions = sessions;
        if (parentFragment instanceof CollaboratorActivityEditFragment)
            fragment.collaboratorActivityEditFragment = (CollaboratorActivityEditFragment) parentFragment;
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
        View view = inflater.inflate(R.layout.fragment_collaborator_edit_session, container, false);

        dateEditText = view.findViewById(R.id.dateEditText);
        timeEditText = view.findViewById(R.id.timeEditText);
        durationEditText = view.findViewById(R.id.durationEditText);
        structureEditText = view.findViewById(R.id.structureEditText);
        deleteButton = view.findViewById(R.id.deleteButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);

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
            StructurePickFragment structurePickFragment = StructurePickFragment.newInstance(structureEditText, getFragmentManager(), this);
            structurePickFragment.show(getFragmentManager(), "structurePickerFragment");
        });

        cancelButton.setOnClickListener(v -> dismiss());
        deleteButton.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                .setMessage(R.string.session_delete_confirmation_text)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> delete())
                .setNegativeButton(android.R.string.cancel, null).show());
        saveButton.setOnClickListener(v -> checkInputAndSave());

        setupEditTexts();

        return view;
    }

    private void checkInputAndSave() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
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
                collaboratorActivityEditFragment.mRecyclerView.post(() -> collaboratorActivityEditFragment.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }

    private void delete() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        boolean valid = true;
        if (session.getDateTime().isBefore(LocalDateTime.now())) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.session_invalid_time_delete), Toast.LENGTH_LONG).show();
        }

        if (valid) {
            new Thread(() -> {
                if (collaboratorActivityEditFragment != null) {
                    collaboratorActivityEditFragment.sessions.remove(session);
                    collaboratorActivityEditFragment.sessionsToDelete.add(session);
                    collaboratorActivityEditFragment.mRecyclerView.post(() -> collaboratorActivityEditFragment.mAdapter.notifyDataSetChanged());
                    getActivity().runOnUiThread(() -> collaboratorActivityEditFragment.refreshEmptyTextView());
                }
                getActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }

    private void setupEditTexts() {
        String date = session.getDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        dateEditText.setText(date);
        String time = session.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        timeEditText.setText(time);
        String duration = "" + session.getDuration();
        durationEditText.setText(duration);
        Structure structure = session.getStructure();
        this.structure = structure;
        structureEditText.setText(structure.getName());
    }
}