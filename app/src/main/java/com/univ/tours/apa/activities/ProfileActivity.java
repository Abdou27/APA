package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends BaseToolbarActivity {
    private static final String APA = "apa";

    EditText lastNameEditText, firstNameEditText, birthdayEditText, phoneNumberEditText, passwordEditText, passwordConfirmEditText;
    Button okButton, cancelButton;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lastNameEditText = findViewById(R.id.lastNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        birthdayEditText = findViewById(R.id.birthdayEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordConfirmEditText);
        okButton = findViewById(R.id.okButton);
        cancelButton = findViewById(R.id.cancelButton);
        loading = findViewById(R.id.loading);

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
            birthdayEditText.setText(sdf.format(myCalendar.getTime()));
        };
        birthdayEditText.setOnClickListener(v -> {
            new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

//        TextWatcher textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (lastNameEditText.getText().toString().isEmpty()
//                        || firstNameEditText.getText().toString().isEmpty()
//                        || birthdayEditText.getText().toString().isEmpty()
//                        || phoneNumberEditText.getText().toString().isEmpty()
//                        || passwordEditText.getText().toString().isEmpty()
//                        || passwordConfirmEditText.getText().toString().isEmpty()) {
//                    okButton.setEnabled(false);
//                } else {
//                    okButton.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        };
//        lastNameEditText.addTextChangedListener(textWatcher);
//        firstNameEditText.addTextChangedListener(textWatcher);
//        birthdayEditText.addTextChangedListener(textWatcher);
//        phoneNumberEditText.addTextChangedListener(textWatcher);
//        passwordEditText.addTextChangedListener(textWatcher);
//        passwordConfirmEditText.addTextChangedListener(textWatcher);

        okButton.setOnClickListener(v -> {
            String lastName = lastNameEditText.getText().toString().trim();
            String firstName = firstNameEditText.getText().toString().trim();
            String birthday = birthdayEditText.getText().toString();
            LocalDate birthdayLocalDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String confirmation = passwordConfirmEditText.getText().toString();

            if (password.matches("^.{6,}$")) {
                if (password.equals(confirmation)) {
                    if (lastName.matches("^[a-zA-Z-']{2,}([ ]?[a-zA-Z-'])*$") && firstName.matches("^[a-zA-Z-']{2,}([ ]?[a-zA-Z-'])*$")) {
                        if (phoneNumber.matches("^((0033)|(\\+33)|0)[0-9]{9}$")) {
                            Log.d("TAG", "birthdayLocalDate: " + birthdayLocalDate.toString());
                            Log.d("TAG", "LocalDate.now().minusYears(18): " + LocalDate.now().minusYears(18).toString());
                            if (birthdayLocalDate.isBefore(LocalDate.now().minusYears(18))) {
                                updateUser(lastName, firstName, birthdayLocalDate, phoneNumber, password);
                            } else {
                                Toast.makeText(this, getString(R.string.invalid_age), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.invalid_phone_number), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.invalid_name), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.passwords_dont_match), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.password_must_be_6_characters_or_more), Toast.LENGTH_LONG).show();
            }
        });

        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        setupEditTexts();
    }

    private void updateUser(String lastName, String firstName, LocalDate birthdayLocalDate, String phoneNumber, String password) {
        SharedPreferences settings;
        settings = getSharedPreferences(APA, Context.MODE_PRIVATE);
        Long user_id = settings.getLong("user_id", 0);
        new Thread(() -> {
            User user = MainActivity.db.userDao().findById(user_id);
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setBirthday(birthdayLocalDate);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(password);
            MainActivity.db.userDao().update(user);
            Looper.prepare();
            Toast.makeText(this, getString(R.string.success), Toast.LENGTH_LONG).show();
            Looper.loop();
        }).start();
        runOnUiThread(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupEditTexts() {
        SharedPreferences settings;
        settings = getSharedPreferences(APA, Context.MODE_PRIVATE);
        Long user_id = settings.getLong("user_id", 0);
        new Thread(() -> {
            User user = MainActivity.db.userDao().findById(user_id);
            lastNameEditText.setText(user.getLastName());
            firstNameEditText.setText(user.getFirstName());
            birthdayEditText.setText(user.getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            phoneNumberEditText.setText(user.getPhoneNumber());
            passwordEditText.setText(user.getPassword());
            passwordConfirmEditText.setText(user.getPassword());
        }).start();
    }
}