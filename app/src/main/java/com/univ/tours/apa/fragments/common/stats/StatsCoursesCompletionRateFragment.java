package com.univ.tours.apa.fragments.common.stats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.DoctorPatientsRecyclerViewAdapter;
import com.univ.tours.apa.adapters.StatsPatientsCompletionRateRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.User;

import java.util.ArrayList;
import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsCoursesCompletionRateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  StatsCoursesCompletionRateFragment extends Fragment {

	List<User> patients = new ArrayList<>();

	public RecyclerView mRecyclerView;
	private StatsPatientsCompletionRateRecyclerViewAdapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	ProgressBar loadingProgressBar;
	TextView noPatientsTextView;

	public StatsCoursesCompletionRateFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment StatsCoursesCompletionRateFragment.
	 */
	public static StatsCoursesCompletionRateFragment newInstance() {
		StatsCoursesCompletionRateFragment fragment = new StatsCoursesCompletionRateFragment();
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
		View view = inflater.inflate(R.layout.fragment_stats_courses_completion_rate, container, false);

		noPatientsTextView = view.findViewById(R.id.noPatientsTextView);
		loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
		
		mRecyclerView = view.findViewById(R.id.recyclerView);
		mRecyclerView.setHasFixedSize(false);
		mLayoutManager = new LinearLayoutManager(getContext());
		mRecyclerView.setLayoutManager(mLayoutManager);

		setupPatients();

		return view;
	}

	private void setupPatients() {
		loadingProgressBar.setVisibility(View.VISIBLE);
		new Thread(() -> {
			List<Session> sessions = db.sessionDao().getAll();
			patients.clear();
			for (Session session : sessions) {
				User patient = session.getActivity().getCourse().getPatient();
				boolean contains = false;
				for (User p : patients) {
					if (p.getId().equals(patient.getId())) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					patients.add(patient);
				}
			}
			List<Integer> rates = new ArrayList<>();
			for (User p : patients) {
				float denominator = 0;
				float numerator = 0;
				List<Course> courses = db.courseDao().findByPatient(p);
				List<Activity> activites = db.activityDao().findByCourses(courses);
				sessions = db.sessionDao().findByActivities(activites);
				for (Session s : sessions) {
					if (s.getCompletionRate() != null) {
						numerator += (float) s.getDuration() * s.getCompletionRate() / 10;
					}
					denominator += s.getDuration();
				}
				Integer rate = Math.round(numerator / denominator * 100);
				rates.add(rate);
			}
			getActivity().runOnUiThread(() -> {
				mAdapter = new StatsPatientsCompletionRateRecyclerViewAdapter(getActivity(), patients, rates);
				mRecyclerView.setAdapter(mAdapter);
				refreshEmptyTextView();
				loadingProgressBar.setVisibility(View.GONE);
			});
		}).start();
	}

	public void refreshEmptyTextView() {
		if (patients.isEmpty()) {
			noPatientsTextView.setVisibility(View.VISIBLE);
		} else {
			noPatientsTextView.setVisibility(View.GONE);
		}
	}
}