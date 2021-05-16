package com.univ.tours.apa.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.CollaboratorActivitiesRecyclerViewAdapter;
import com.univ.tours.apa.adapters.CollaboratorSessionsRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.User;

import java.util.ArrayList;
import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorEditActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorEditActivityFragment extends DialogFragment {
    private static final String APA = "apa";
    FragmentManager fm;
    CollaboratorActivitiesRecyclerViewAdapter parent;
    Activity activity;

    public List<Session> sessions;

    public RecyclerView mRecyclerView;
    public CollaboratorSessionsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CollaboratorEditActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollaboratorEditActivityFragment.
     */
    public static CollaboratorEditActivityFragment newInstance(Activity activity, FragmentManager fm, CollaboratorActivitiesRecyclerViewAdapter collaboratorActivitiesRecyclerViewAdapter) {
        CollaboratorEditActivityFragment fragment = new CollaboratorEditActivityFragment();
        fragment.activity = activity;
        fragment.parent = collaboratorActivitiesRecyclerViewAdapter;
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
        View view = inflater.inflate(R.layout.fragment_collaborator_edit_activity, container, false);

        EditText titleEditText = view.findViewById(R.id.titleEditText);
        titleEditText.setText(activity.getTitle());
        EditText descriptionEditText = view.findViewById(R.id.descriptionEditText);
        descriptionEditText.setText(activity.getDescription());
        Button addSessionButton = view.findViewById(R.id.addSessionButton);
        addSessionButton.setOnClickListener(v -> {
            CollaboratorAddSessionFragment collaboratorAddSessionFragment = CollaboratorAddSessionFragment.newInstance(this, activity, fm);
            collaboratorAddSessionFragment.show(fm, "collaboratorAddSessionFragment");
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<Activity> activities = new ArrayList<>();
        activities.add(activity);
        new Thread((Runnable) () -> {
            sessions = db.sessionDao().findByActivities(activities);
            getActivity().runOnUiThread(() -> {
                mAdapter = new CollaboratorSessionsRecyclerViewAdapter(fm, sessions, this);
                mRecyclerView.setAdapter(mAdapter);
            });
        }).start();

        Button editActivityButton = view.findViewById(R.id.editActivityButton);
        editActivityButton.setOnClickListener(v -> {
            new Thread(() -> {
                SharedPreferences settings;
                settings = getActivity().getSharedPreferences(APA, Context.MODE_PRIVATE);
                Long user_id = settings.getLong("user_id", 0);
                User user = MainActivity.db.userDao().findById(user_id);
                activity.setTitle(titleEditText.getText().toString().trim());
                activity.setDescription(descriptionEditText.getText().toString().trim());
                activity.setCollaborator(user);
                MainActivity.db.activityDao().update(activity);
                parent.parent.mRecyclerView.post(() -> parent.parent.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> {
                    dismiss();
                });
            }).start();
        });

        return view;
    }
}