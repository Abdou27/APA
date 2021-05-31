package com.univ.tours.apa.fragments.doctor;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.DoctorPatientsRecyclerViewAdapter;
import com.univ.tours.apa.entities.User;

import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorPatientsBrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorPatientsBrowseFragment extends Fragment {
    FragmentManager fm;
    android.app.Activity context;
    List<User> retainedUserList;

    public RecyclerView mRecyclerView;
    private DoctorPatientsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog loadingDialog;
    ProgressBar loadingProgressBar;

    String searchQuery = "";

    public DoctorPatientsBrowseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DoctorBrowsePatientsFragment.
     */
    public static DoctorPatientsBrowseFragment newInstance(FragmentManager fm, android.app.Activity context) {
        DoctorPatientsBrowseFragment fragment = new DoctorPatientsBrowseFragment();
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
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
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
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setupUsers();

        return view;
    }

    private void setupUsers() {
//        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        loadingProgressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            retainedUserList = db.userDao().getAllPatients();
            getActivity().runOnUiThread(() -> {
                mAdapter = new DoctorPatientsRecyclerViewAdapter(context, retainedUserList, fm);
                mRecyclerView.setAdapter(mAdapter);
    //            loadingDialog.dismiss();
                loadingProgressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    private void updateUsers() {
//        loadingDialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.loading_progress_bar_text), true);
        loadingProgressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<User> userList = db.userDao().getAllPatients();
            getActivity().runOnUiThread(() -> {
                retainedUserList.clear();
                for (User user : userList) {
                    if (!isNotInQuery(user)) {
                        retainedUserList.add(user);
                    }
                }
                mAdapter.notifyDataSetChanged();
//                this.mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
    //            loadingDialog.dismiss();
                loadingProgressBar.setVisibility(View.GONE);
            });
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