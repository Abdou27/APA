package com.univ.tours.apa.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.DoctorPatientsRecyclerViewAdapter;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.User;

import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorBrowsePatientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorBrowsePatientsFragment extends Fragment {
    FragmentManager fm;
    android.app.Activity context;
    List<User> retainedUserList;

    public RecyclerView mRecyclerView;
    private DoctorPatientsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String searchQuery = "";

    public DoctorBrowsePatientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DoctorBrowsePatientsFragment.
     */
    public static DoctorBrowsePatientsFragment newInstance(FragmentManager fm, android.app.Activity context) {
        DoctorBrowsePatientsFragment fragment = new DoctorBrowsePatientsFragment();
        fragment.fm = fm;
        fragment.context = context;
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
        View view = inflater.inflate(R.layout.fragment_doctor_browse_patients, container, false);
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                Log.d("TAG", "onQueryTextSubmit: " + searchQuery);
                updateUsers();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                Log.d("TAG", "onQueryTextChange: " + searchQuery);
                updateUsers();
                return false;
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        new Thread((Runnable) () -> {
            retainedUserList = db.userDao().getAllPatients();
            mAdapter = new DoctorPatientsRecyclerViewAdapter(context, retainedUserList, fm);
            mRecyclerView.setAdapter(mAdapter);
        }).start();

        return view;
    }

    private void updateUsers() {
        new Thread((Runnable) () -> {
            retainedUserList.clear();
            List<User> userList = db.userDao().getAllPatients();
            for (User user : userList) {
                if (isNotInQuery(user)) {
                    continue;
                } else {
                    retainedUserList.add(user);
                }
            }
            this.mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
        }).start();
    }

    private boolean isNotInQuery(User user) {
        String a = searchQuery.toLowerCase().trim();
        String b = user.getFullName().toLowerCase().trim();
        boolean ab = !a.contains(b);
        boolean ba = !b.contains(a);
        return ab && ba;
    }
}