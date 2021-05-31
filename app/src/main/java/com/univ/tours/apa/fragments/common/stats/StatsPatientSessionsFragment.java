package com.univ.tours.apa.fragments.common.stats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.univ.tours.apa.activities.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsPatientSessionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsPatientSessionsFragment extends Fragment {

	List<Session> sessions = new ArrayList<>();

	PieChart pieChart;
	ProgressBar loadingProgressBar;

	public StatsPatientSessionsFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment StatsPatientSessionsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static StatsPatientSessionsFragment newInstance() {
		StatsPatientSessionsFragment fragment = new StatsPatientSessionsFragment();
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
		View view = inflater.inflate(R.layout.fragment_stats_patient_sessions, container, false);

		loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

		pieChart = view.findViewById(R.id.pieChart);

		setupData();

		return view;
	}

	private void setupData() {
		pieChart.setVisibility(View.INVISIBLE);
		loadingProgressBar.setVisibility(View.VISIBLE);
		new Thread(() -> {
			User user = db.userDao().findById(MainActivity.getLoggedInUserId());
			List<Course> courses = db.courseDao().findByPatient(user);
			List<Activity> activities = db.activityDao().findByCourses(courses);
			sessions.clear();
			sessions.addAll(db.sessionDao().findByActivities(activities));
			getActivity().runOnUiThread(() -> {
				float denominator = 0;
				float numerator = 0;
				for (Session s : sessions) {
					if (s.getCompletionRate() != null) {
						numerator += (float) s.getDuration() * s.getCompletionRate() / 10;
					}
					denominator += s.getDuration();
				}
				Integer rate = Math.round(numerator / denominator * 100);

				Log.d("TAG", "setupData: " + rate);
				Log.d("TAG", "setupData: " + (float) rate / 100);
				Log.d("TAG", "setupData: " + ((float) 1 - ((float) rate / 100)));

				List<PieEntry> pieEntries = new ArrayList<>();
				pieEntries.add(new PieEntry((float) rate / 100,
						getString(R.string.completed)));
				pieEntries.add(new PieEntry(((float) 1 - ((float) rate / 100)),
						getString(R.string.not_completed)));

				PieDataSet dataSet = new PieDataSet(pieEntries, "");

				dataSet.setColors(getColors());

				PieData data = new PieData(dataSet);
				data.setValueFormatter(new PercentFormatter());
				data.setDrawValues(false);
				pieChart.setUsePercentValues(true);
				pieChart.getDescription().setEnabled(false);
				pieChart.setRotationEnabled(false);
				pieChart.setRotationAngle(30);
				pieChart.setHoleRadius(35);
				pieChart.setTransparentCircleRadius(40);
				pieChart.setCenterTextSize(24);
				pieChart.setData(data);
				pieChart.setEntryLabelColor(getResources().getColor(R.color.black));
				pieChart.notifyDataSetChanged();
				pieChart.invalidate();

				pieChart.setVisibility(View.VISIBLE);
				loadingProgressBar.setVisibility(View.GONE);
			});
		}).start();
	}

	@NotNull
	private ArrayList<Integer> getColors() {
		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<>();

		// for (int c : ColorTemplate.VORDIPLOM_COLORS)
		// 	colors.add(c);

		for (int c : ColorTemplate.JOYFUL_COLORS)
			colors.add(c);

		// for (int c : ColorTemplate.COLORFUL_COLORS)
		// 	colors.add(c);
		//
		// for (int c : ColorTemplate.LIBERTY_COLORS)
		// 	colors.add(c);
		//
		// for (int c : ColorTemplate.PASTEL_COLORS)
		// 	colors.add(c);
		//
		// colors.add(ColorTemplate.getHoloBlue());

		// colors.add(getResources().getColor(R.color.design_default_color_primary));
		// colors.add(getResources().getColor(R.color.design_default_color_secondary));

		return colors;
	}
}