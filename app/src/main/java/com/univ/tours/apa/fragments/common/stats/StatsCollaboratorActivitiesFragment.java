package com.univ.tours.apa.fragments.common.stats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.adapters.StatsPatientsCompletionRateRecyclerViewAdapter;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsCollaboratorActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsCollaboratorActivitiesFragment extends Fragment {

	private List<Activity> nonUserActivities = new ArrayList<>();
	private List<Activity> userActivitites = new ArrayList<>();

	PieChart pieChart;
	ProgressBar loadingProgressBar;

	public StatsCollaboratorActivitiesFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment StatsCollaboratorActivitiesFragment.
	 */
	public static StatsCollaboratorActivitiesFragment newInstance() {
		StatsCollaboratorActivitiesFragment fragment = new StatsCollaboratorActivitiesFragment();
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
		View view = inflater.inflate(R.layout.fragment_stats_collaborator_activities, container, false);

		loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

		pieChart = view.findViewById(R.id.pieChart);

		setupData();

		//

		return view;
	}

	private void setupData() {
		pieChart.setVisibility(View.INVISIBLE);
		loadingProgressBar.setVisibility(View.VISIBLE);
		new Thread(() -> {
			User user = MainActivity.db.userDao().findById(MainActivity.getLoggedInUserId());
			List<Activity> allActivities = MainActivity.db.activityDao().getAll();
			getActivity().runOnUiThread(() -> {
				for (Activity activity : allActivities) {
					if (activity.getCollaborator() != null && activity.getCollaborator().getId().equals(user.getId())) {
						userActivitites.add(activity);
					} else {
						nonUserActivities.add(activity);
					}
				}

				List<PieEntry> pieEntries = new ArrayList<>();
				pieEntries.add(new PieEntry((float) userActivitites.size() / allActivities.size(),
						getString(R.string.your_activities)));
				pieEntries.add(new PieEntry((float) nonUserActivities.size() / allActivities.size(),
						getString(R.string.other_activities)));

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