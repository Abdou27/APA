package com.univ.tours.apa.fragments.collaborator;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.CollaboratorActivitiesRecyclerViewAdapter;
import com.univ.tours.apa.adapters.CollaboratorSessionsRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorActivityEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorActivityEditFragment extends DialogFragment {
    FragmentManager fm;
    CollaboratorActivitiesRecyclerViewAdapter parent;
    Activity activity;

    public List<Session> sessions = new ArrayList<>();
    public List<Session> sessionsToDelete = new ArrayList<>();

    public RecyclerView mRecyclerView;
    public CollaboratorSessionsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView noSessionsTextView;
    EditText titleEditText, descriptionEditText;
    Button addSessionButton, editActivityButton;
    private ProgressDialog loadingDialog;

    public CollaboratorActivityEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollaboratorEditActivityFragment.
     */
    public static CollaboratorActivityEditFragment newInstance(Activity activity, FragmentManager fm, CollaboratorActivitiesRecyclerViewAdapter collaboratorActivitiesRecyclerViewAdapter) {
        CollaboratorActivityEditFragment fragment = new CollaboratorActivityEditFragment();
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

        noSessionsTextView = view.findViewById(R.id.noSessionsTextView);
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        addSessionButton = view.findViewById(R.id.addSessionButton);
        editActivityButton = view.findViewById(R.id.editActivityButton);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        titleEditText.setText(activity.getTitle());
        descriptionEditText.setText(activity.getDescription());
        addSessionButton.setOnClickListener(v -> {
            CollaboratorSessionAddFragment collaboratorSessionAddFragment = CollaboratorSessionAddFragment.newInstance(this, activity, fm);
            collaboratorSessionAddFragment.show(fm, "collaboratorAddSessionFragment");
        });

        setupSessions();

        editActivityButton.setOnClickListener(v -> {
            checkInputAndSave();
        });

        return view;
    }

    private void setupSessions() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            List<Session> sessionsToShow = MainActivity.db.sessionDao().findByActivity(activity);
            getActivity().runOnUiThread(() -> {
                sessions.clear();
                sessions.addAll(sessionsToShow);
                if (mAdapter == null) {
                    mAdapter = new CollaboratorSessionsRecyclerViewAdapter(fm, sessions, this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                refreshEmptyTextView();
                loadingDialog.dismiss();
            });
        }).start();
    }

    private void checkInputAndSave() {
        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        String inputTitle = titleEditText.getText().toString().trim();
        String inputDescription = descriptionEditText.getText().toString().trim();
        boolean valid = true;

        if (TextUtils.isEmpty(inputTitle)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_title), Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(inputDescription)) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.empty_description), Toast.LENGTH_LONG).show();
        }
        if (sessions.isEmpty()) {
            valid = false;
            Toast.makeText(getContext(), getContext().getString(R.string.no_sessions_defined), Toast.LENGTH_LONG).show();
        }
        for (Session session : sessionsToDelete) {
            if (session.getDateTime().isBefore(LocalDateTime.now())) {
                valid = false;
                Toast.makeText(getContext(), getContext().getString(R.string.session_invalid_time_delete), Toast.LENGTH_LONG).show();
                setupSessions();
                break;
            }
        }

        if (valid) {
            activity.setTitle(inputTitle);
            activity.setDescription(inputDescription);
            new Thread(() -> {
                User user = MainActivity.db.userDao().findById(MainActivity.getLoggedInUserId());
                activity.setCollaborator(user);
                MainActivity.db.activityDao().update(activity);
                for (Session session : sessions) {
                    session.setActivity(activity);
                    if (session.getId() != null)
                        MainActivity.db.sessionDao().update(session);
                    else
                        MainActivity.db.sessionDao().insert(session);
                }
                for (Session session : sessionsToDelete) {
                    MainActivity.db.sessionDao().delete(session);
                }
                parent.parent.mRecyclerView.post(() -> parent.parent.mAdapter.notifyDataSetChanged());
                getActivity().runOnUiThread(() -> {
                    refreshEmptyTextView();
                    loadingDialog.dismiss();
                    dismiss();
                });
            }).start();
        } else {
            loadingDialog.dismiss();
        }
    }

    public void refreshEmptyTextView() {
        if (sessions.isEmpty()) {
            noSessionsTextView.setVisibility(View.VISIBLE);
        } else {
            noSessionsTextView.setVisibility(View.GONE);
        }
    }
}