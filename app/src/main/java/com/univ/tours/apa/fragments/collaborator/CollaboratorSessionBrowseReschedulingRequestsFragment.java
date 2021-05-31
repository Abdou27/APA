package com.univ.tours.apa.fragments.collaborator;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.CollaboratorRequestsRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorSessionBrowseReschedulingRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorSessionBrowseReschedulingRequestsFragment extends Fragment {
    FragmentManager fm;

    public RecyclerView mRecyclerView;
    public CollaboratorRequestsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog loadingDialog;
    ProgressBar loadingProgressBar;

    TextView noRequestsTextView;
    public List<Session> requests = new ArrayList<>();

    public CollaboratorSessionBrowseReschedulingRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollaboratorBrowseReschedulingRequestsFragment.
     * @param fm
     */
    public static CollaboratorSessionBrowseReschedulingRequestsFragment newInstance(FragmentManager fm) {
        CollaboratorSessionBrowseReschedulingRequestsFragment fragment = new CollaboratorSessionBrowseReschedulingRequestsFragment();
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
        View view = inflater.inflate(R.layout.fragment_collaborator_browse_rescheduling_requests, container, false);

        noRequestsTextView = view.findViewById(R.id.noRequestsTextView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        
        setupRequests();

        return view;
    }

    private void setupRequests() {
//        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        loadingProgressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            User collaborator = MainActivity.db.userDao().findById(MainActivity.getLoggedInUserId());
            List<Activity> activities = MainActivity.db.activityDao().findByCollaborator(collaborator);
            List<Session> sessions = MainActivity.db.sessionDao().findByActivitiesAndRescheduled(activities);
            requests.clear();
            requests.addAll(sessions);
            for (Session session : sessions) {
                if (session.getDateTime().isBefore(LocalDateTime.now())) {
                    session.setRescheduledDateTime(null);
                    MainActivity.db.sessionDao().update(session);
                    requests.remove(session);
                }
            }
            getActivity().runOnUiThread(() -> {
                mAdapter = new CollaboratorRequestsRecyclerViewAdapter(getContext(), requests, fm, this);
                mRecyclerView.setAdapter(mAdapter);
                refreshEmptyTextView();
//                loadingDialog.dismiss();
                loadingProgressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    public void refreshEmptyTextView() {
        if (requests.isEmpty()) {
            noRequestsTextView.setVisibility(View.VISIBLE);
        } else {
            noRequestsTextView.setVisibility(View.GONE);
        }
    }
}