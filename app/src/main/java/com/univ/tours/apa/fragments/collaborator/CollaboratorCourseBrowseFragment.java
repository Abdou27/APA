package com.univ.tours.apa.fragments.collaborator;

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

import static android.content.ContentValues.TAG;
import static com.univ.tours.apa.activities.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollaboratorCourseBrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaboratorCourseBrowseFragment extends Fragment {
    FragmentManager fm;
    public List<Course> retainedCourses = new ArrayList<>();

    public RecyclerView mRecyclerView;
    public CollaboratorCoursesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView noCoursesTextView;

    String searchQuery = "";
    ProgressBar loadingProgressBar;
    private ProgressDialog loadingDialog;
    private SearchView searchView;
    private SearchView.OnQueryTextListener textListener;

    public CollaboratorCourseBrowseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollaboratorBrowseCoursesFragment.
     */
    public static CollaboratorCourseBrowseFragment newInstance(FragmentManager fm) {
        CollaboratorCourseBrowseFragment fragment = new CollaboratorCourseBrowseFragment();
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

        searchView = view.findViewById(R.id.searchView);
        textListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                updateCourses();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchQuery.equals(newText))
                    return false;
                searchQuery = newText;
                updateCourses();
                return false;
            }
        };
        searchView.setOnQueryTextListener(textListener);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setupCourses();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // textListener.onQueryTextChange(searchQuery);
    }

    private void setupCourses() {
//        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        loadingProgressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            // Find courses that have unattached activities (free for the collaborator to take)
            List<Course> courseList = db.courseDao().getAll();
            retainedCourses.clear();
            for (Course course : courseList) {
                List<Activity> activities = db.activityDao().findByCourse(course);
                boolean flag = false;
                for (Activity activity : activities) {
                    if (activity.getCollaborator() == null || activity.getCollaborator().getId().equals(MainActivity.getLoggedInUserId())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    boolean contains = false;
                    for (Course retainedCourse : retainedCourses) {
                        if (retainedCourse.getId().equals(course.getId())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        retainedCourses.add(course);
                    }
                }
            }
            getActivity().runOnUiThread(() -> {
                Log.d("TAG", "setupCourses: " + retainedCourses.size());
                if (mRecyclerView.getAdapter() == null) {
                    mAdapter = new CollaboratorCoursesRecyclerViewAdapter(getContext(), retainedCourses, fm);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
                refreshEmptyTextView();
                loadingProgressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    private void updateCourses() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Course> courseList = db.courseDao().getAll();
            retainedCourses.clear();
            for (Course course : courseList) {
                if (isNotInQuery(course)) {
                    continue;
                }
                List<Activity> activities = db.activityDao().findByCourse(course);
                boolean flag = false;
                for (Activity activity : activities) {
                    if (activity.getCollaborator() == null || activity.getCollaborator().getId().equals(MainActivity.getLoggedInUserId())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    boolean contains = false;
                    for (Course retainedCourse : retainedCourses) {
                        if (retainedCourse.getId().equals(course.getId())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        retainedCourses.add(course);
                    }
                }
            }
            getActivity().runOnUiThread(() -> {
                Log.d("TAG", "updateCourses: " + retainedCourses.size());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                refreshEmptyTextView();
                loadingProgressBar.setVisibility(View.GONE);
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