package com.univ.tours.apa;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends DialogFragment {

    EditText lastNameEditText, firstNameEditText, usernameEditText, passwordEditText, passwordConfirmEditText;
    Button register;
    ProgressBar loading;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegisterFragment.
     */
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        passwordConfirmEditText = view.findViewById(R.id.passwordConfirmEditText);
        register = view.findViewById(R.id.register);
        loading = view.findViewById(R.id.loading);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lastNameEditText.getText().toString().isEmpty()
                        || firstNameEditText.getText().toString().isEmpty()
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
        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        passwordConfirmEditText.addTextChangedListener(textWatcher);

        register.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                loading.setVisibility(View.GONE);
                // Do stuff
                getDialog().dismiss();
            }, 1000);
        });

        return view;
    }
}