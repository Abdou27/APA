package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    EditText lastNameEditText, firstNameEditText, usernameEditText, passwordEditText, passwordConfirmEditText, birthdayEditText, phoneNumberEditText;
    RadioButton patientRadioButton, doctorRadioButton, collaboratorRadioButton;
    Button login, register;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lastNameEditText = findViewById(R.id.lastNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordConfirmEditText);
        birthdayEditText = findViewById(R.id.birthdayEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        patientRadioButton = findViewById(R.id.patientRadioButton);
        doctorRadioButton = findViewById(R.id.doctorRadioButton);
        collaboratorRadioButton = findViewById(R.id.collaboratorRadioButton);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
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


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lastNameEditText.getText().toString().isEmpty()
                        || firstNameEditText.getText().toString().isEmpty()
                        || birthdayEditText.getText().toString().isEmpty()
                        || phoneNumberEditText.getText().toString().isEmpty()
                        || usernameEditText.getText().toString().isEmpty()
                        || passwordEditText.getText().toString().isEmpty()
                        || passwordConfirmEditText.getText().toString().isEmpty()) {
                    register.setEnabled(false);
                } else {
                    register.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        lastNameEditText.addTextChangedListener(textWatcher);
        firstNameEditText.addTextChangedListener(textWatcher);
        birthdayEditText.addTextChangedListener(textWatcher);
        phoneNumberEditText.addTextChangedListener(textWatcher);
        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        passwordConfirmEditText.addTextChangedListener(textWatcher);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            startActivity(intent);
        });

        register.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            String email = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmation = passwordConfirmEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString().trim();
            String firstName = firstNameEditText.getText().toString().trim();
            String birthday = birthdayEditText.getText().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthdayLocalDate = LocalDate.parse(birthday, formatter);
            String phoneNumber = phoneNumberEditText.getText().toString();
            String role = null;
            if (doctorRadioButton.isChecked()) {
                role = "ROLE_DOCTOR";
            } else if (collaboratorRadioButton.isChecked()) {
                role = "ROLE_COLLABORATOR";
            } else {
                role = "ROLE_PATIENT";
            }
            if (isEmailValid(email)) {
                if (password.matches("^.{6,}$")) {
                    if (password.equals(confirmation)) {
                        if (lastName.matches("^[a-zA-Z-']{2,}([ ]?[a-zA-Z-'])*$") && firstName.matches("^[a-zA-Z-']{2,}([ ]?[a-zA-Z-'])*$")) {
                            if (phoneNumber.matches("^((0033)|(\\+33)|0)[0-9]{9}$")) {
                                if (birthdayLocalDate.isBefore(birthdayLocalDate.minusYears(18))) {
                                    String finalRole = role;
                                    new Thread((Runnable) () -> {
                                        User userByEmail = MainActivity.db.userDao().findByEmail(email.toLowerCase());
                                        if (userByEmail == null) {
                                            User user = new User();
                                            user.setEmail(email.toLowerCase());
                                            user.setPassword(password); // Pas de hashage du mot de passe pour le moment
                                            user.setFirstName(firstName);
                                            user.setLastName(lastName);
                                            user.setRole(finalRole);
                                            user.setBirthday(birthdayLocalDate);
                                            user.setPhoneNumber(phoneNumber);
                                            MainActivity.db.userDao().insert(user);
                                            runOnUiThread(() -> {
                                                Toast.makeText(this, getString(R.string.success), Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                                loading.setVisibility(View.INVISIBLE);
                                            });
                                        } else {
                                            Looper.prepare();
                                            Toast.makeText(this, getString(R.string.user_exists), Toast.LENGTH_LONG).show();
                                            Looper.loop();
                                            loading.setVisibility(View.INVISIBLE);
                                        }
                                    }).start();
                                } else {
                                    Toast.makeText(this, getString(R.string.invalid_age), Toast.LENGTH_LONG).show();
                                    loading.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                Toast.makeText(this, getString(R.string.invalid_phone_number), Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.invalid_name), Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.passwords_dont_match), Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.password_must_be_6_characters_or_more), Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.INVISIBLE);
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}