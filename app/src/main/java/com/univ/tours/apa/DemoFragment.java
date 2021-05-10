package com.univ.tours.apa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoFragment extends Fragment {

    Button loginButton, registerButton, addStructureButton, addSessionButton, addActivityButton;
    FragmentManager fm;

    public DemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DemoFragment.
     */
    public static DemoFragment newInstance(FragmentManager fm) {
        DemoFragment fragment = new DemoFragment();
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
        View view = inflater.inflate(R.layout.fragment_demo, container, false);

        loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            LoginFragment loginFragment = LoginFragment.newInstance();
            loginFragment.show(fm, "loginFragment");
        });

        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            RegisterFragment registerFragment = RegisterFragment.newInstance();
            registerFragment.show(fm, "registerFragment");
        });

        addStructureButton = view.findViewById(R.id.addStructureButton);
        addStructureButton.setOnClickListener(v -> {
            AddStructureFragment addStructureFragment = AddStructureFragment.newInstance();
            addStructureFragment.show(fm, "addStructureFragment");
        });

        addSessionButton = view.findViewById(R.id.addSessionButton);
        addSessionButton.setOnClickListener(v -> {
            AddSessionFragment addSessionFragment = AddSessionFragment.newInstance();
            addSessionFragment.show(fm, "addSessionFragment");
        });

        addActivityButton = view.findViewById(R.id.addActivityButton);
        addActivityButton.setOnClickListener(v -> {
            AddActivityFragment addActivityFragment = AddActivityFragment.newInstance(fm);
            addActivityFragment.show(fm, "addActivityFragment");
        });

        return view;
    }
}