package com.univ.tours.apa.fragments.common;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends DialogFragment {

    EditText lastNameEditText, firstNameEditText, birthdayEditText, phoneNumberEditText, passwordEditText, passwordConfirmEditText;
    Button okButton, cancelButton;
    private ProgressDialog loadingDialog;

    private Calendar myCalendar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        birthdayEditText = view.findViewById(R.id.birthdayEditText);
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        passwordConfirmEditText = view.findViewById(R.id.passwordConfirmEditText);
        okButton = view.findViewById(R.id.okButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
            birthdayEditText.setText(sdf.format(myCalendar.getTime()));
        };
        birthdayEditText.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        okButton.setOnClickListener(v -> checkInputAndSave());

        cancelButton.setOnClickListener(v -> dismiss());

        setupEditTexts();

        return view;
    }

    private void setupEditTexts() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            User user = MainActivity.db.userDao().findById(MainActivity.getLoggedInUserId());
            getActivity().runOnUiThread(() -> {
                lastNameEditText.setText(user.getLastName());
                firstNameEditText.setText(user.getFirstName());
                LocalDate birthday = user.getBirthday();
                birthdayEditText.setText(birthday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                myCalendar.set(Calendar.YEAR, birthday.getYear());
                myCalendar.set(Calendar.MONTH, birthday.getMonthValue() - 1);
                myCalendar.set(Calendar.DAY_OF_MONTH, birthday.getDayOfMonth());
                phoneNumberEditText.setText(user.getPhoneNumber());
                passwordEditText.setText(user.getPassword());
                passwordConfirmEditText.setText(user.getPassword());
                loadingDialog.dismiss();
            });
        }).start();
    }

    private void checkInputAndSave() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        String lastName = lastNameEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String birthday = birthdayEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmation = passwordConfirmEditText.getText().toString();
        boolean valid = true;

        if (!password.matches("^.{6,}$")) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.password_must_be_6_characters_or_more), Toast.LENGTH_LONG).show();
        }

        if (!password.equals(confirmation)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.passwords_dont_match), Toast.LENGTH_LONG).show();
        }

        if (!lastName.matches("^[a-zA-Z-']{2,}([ ]?[a-zA-Z-'])*$") && firstName.matches("^[a-zA-Z-']{2,}([ ]?[a-zA-Z-'])*$")) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.invalid_name), Toast.LENGTH_LONG).show();
        }

        if (!phoneNumber.matches("^((0033)|(\\+33)|0)[0-9]{9}$")) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.invalid_phone_number), Toast.LENGTH_LONG).show();
        }

        LocalDate birthdayLocalDate = null;
        if (TextUtils.isEmpty(birthday)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_age), Toast.LENGTH_LONG).show();
        } else {
            birthdayLocalDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (!birthdayLocalDate.isBefore(LocalDate.now().minusYears(18))) {
                valid = false;
                Toast.makeText(getContext(), getContext().getString(R.string.invalid_age), Toast.LENGTH_LONG).show();
            }
        }

        if (valid) {
            LocalDate finalBirthdayLocalDate = birthdayLocalDate;
            new Thread(() -> {
                User user = MainActivity.db.userDao().findById(MainActivity.getLoggedInUserId());
                user.setLastName(lastName);
                user.setFirstName(firstName);
                user.setBirthday(finalBirthdayLocalDate);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(password);
                MainActivity.db.userDao().update(user);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.success), Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }

    }
}