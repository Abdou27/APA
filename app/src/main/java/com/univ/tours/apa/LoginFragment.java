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
import android.widget.RadioButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends DialogFragment {

    EditText usernameEditText, passwordEditText;
    Button login;
    ProgressBar loading;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        login = view.findViewById(R.id.login);
        loading = view.findViewById(R.id.loading);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (usernameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()) {
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

        login.setOnClickListener(v -> {
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