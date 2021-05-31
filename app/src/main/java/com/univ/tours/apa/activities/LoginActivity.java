package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.User;

public class LoginActivity extends AppCompatActivity {
    private static final String APA = "apa";

    EditText usernameEditText, passwordEditText;
    Button login, register;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (usernameEditText.getText().toString().isEmpty()
                        || passwordEditText.getText().toString().isEmpty()) {
                    login.setEnabled(false);
                } else {
                    login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);

        register.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            tryToLoginUser();
        });
    }

    private void tryToLoginUser() {
        loadingDialog = ProgressDialog.show(this, "",
                getString(R.string.loading_progress_bar_text), true);

        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        new Thread((Runnable) () -> {
            User user = MainActivity.db.userDao().findByEmail(email.toLowerCase());
            boolean valid = false;
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    valid = true;
                }
            }
            Looper.prepare();
            if (valid) {
                getSharedPreferences(APA, Context.MODE_PRIVATE).edit().putLong("user_id", user.getId()).apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                loadingDialog.dismiss();
                finish();
            } else {
                loadingDialog.dismiss();
                Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
            }
            Looper.loop();
        }).start();
    }
}