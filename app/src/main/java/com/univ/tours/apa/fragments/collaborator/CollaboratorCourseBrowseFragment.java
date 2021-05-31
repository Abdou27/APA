package com.univ.tours.apa.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.CollaboratorCoursesRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;

import java.util.ArrayList;
import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorBrowseCoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorBrowseCoursesFragment extends Fragment {
    FragmentManager fm;
    public List<Course> retainedCourses = new ArrayList<>();

    public RecyclerView mRecyclerView;
    public CollaboratorCoursesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView noCoursesTextView;

    String searchQuery = "";
    ProgressBar loadingProgressBar;
    private ProgressDialog loadingDialog;

    public CollaboratorBrowseCoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollaboratorBrowseCoursesFragment.
     */
    public static CollaboratorBrowseCoursesFragment newInstance(FragmentManager fm) {
        CollaboratorBrowseCoursesFragment fragment = new CollaboratorBrowseCoursesFragment();
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
        View view = inflater.inflate(R.layout.fragment_collaborator_browse_courses, container, false);

        noCoursesTextView = view.findViewById(R.id.noCoursesTextView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                Log.d("TAG", "onQueryTextSubmit: " + searchQuery);
                updateCourses();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                Log.d("TAG", "onQueryTextChange: " + searchQuery);
                updateCourses();
                return false;
            }
        });
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setupCourses();

        return view;
    }

    private void setupCourses() {
//        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        loadingProgressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            // Find courses that have unattached activities (free for the collaborator to take)
            List<Course> courseList = db.courseDao().getAll();
            retainedCourses.clear();
            for (Course course : courseList) {
                List<Activity> activities = MainActivity.db.activityDao().findByCourse(course);
                boolean flag = false;
                for (Activity activity : activities) {
                    if (activity.getCollaborator() == null || activity.getCollaborator().getId().equals(MainActivity.getLoggedInUserId())) {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    retainedCourses.add(course);
            }
            getActivity().runOnUiThread(() -> {
                mAdapter = new CollaboratorCoursesRecyclerViewAdapter(getContext(), retainedCourses, fm);
                mRecyclerView.setAdapter(mAdapter);
                refreshEmptyTextView();
//                loadingDialog.dismiss();
                loadingProgressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    private void updateCourses() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            Log.d("TAG", "updateActivities: " + searchQuery);
            List<Course> courseList = db.courseDao().getAll();
            Log.d("TAG", "courseList: " + courseList.size());
            retainedCourses.clear();
            for (Course course : courseList) {
                if (isNotInQuery(course)) {
                    continue;
                }
                List<Activity> activities = MainActivity.db.activityDao().findByCourse(course);
                boolean flag = false;
                for (Activity activity : activities) {
                    if (activity.getCollaborator() == null || activity.getCollaborator().getId().equals(MainActivity.getLoggedInUserId())) {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    retainedCourses.add(course);
            }
//            this.mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
            getActivity().runOnUiThread(() -> {
                mAdapter.notifyDataSetChanged();
                refreshEmptyTextView();
                loadingProgressBar.setVisibility(View.GONE);
                Log.d("TAG", "retainedCourses: " + retainedCourses.size());
            });
        }).start();
    }

    private boolean isNotInQuery(Course course) {
        String a = searchQuery.toLowerCase().trim();
        String b = course.getTitle().toLowerCase().trim();
        boolean ab = !a.contains(b);
        boolean ba = !b.contains(a);
        return ab && ba;
    }

    public void refreshEmptyTextView() {
        if (retainedCourses.isEmpty()) {
            noCoursesTextView.setVisibility(View.VISIBLE);
        } else {
            noCoursesTextView.setVisibility(View.GONE);
        }
    }
}