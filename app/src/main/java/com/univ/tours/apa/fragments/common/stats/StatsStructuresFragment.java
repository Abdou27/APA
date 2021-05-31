package com.univ.tours.apa.fragments.common.stats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsStructuresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsStructuresFragment extends Fragment {

	private List<Structure> structures = new ArrayList<>();
	private List<Integer> structureSessionCounts = new ArrayList<>();

	BarChart barChart;
	ProgressBar loadingProgressBar;

	public StatsStructuresFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment StatsStructuresFragment.
	 */
	public static StatsStructuresFragment newInstance() {
		StatsStructuresFragment fragment = new StatsStructuresFragment();
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
		View view = inflater.inflate(R.layout.fragment_stats_structures, container, false);

		barChart = view.findViewById(R.id.barChart);
		barChart.setDrawBarShadow(true);
		barChart.setDrawValueAboveBar(false);
		barChart.getDescription().setEnabled(false);
		barChart.setMaxVisibleValueCount(60);
		barChart.setDrawGridBackground(false);
		barChart.setMinOffset(0);
		barChart.setExtraTopOffset(10);
		barChart.setPinchZoom(false);


		XAxis xl = barChart.getXAxis();
		xl.setGranularity(1f);
		xl.setAxisMinimum(0);
		xl.setCenterAxisLabels(true);

		YAxis leftAxis = barChart.getAxisLeft();
		leftAxis.setGranularity(1f);
		leftAxis.setDrawGridLines(true);
		leftAxis.setSpaceTop(50f);
		leftAxis.setAxisMinimum(0);
		barChart.getAxisRight().setEnabled(false);

		loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

		setupData();

		return view;
	}

	private void setupData() {
		barChart.setVisibility(View.INVISIBLE);
		loadingProgressBar.setVisibility(View.VISIBLE);
		structures.clear();
		new Thread(() -> {
			structures.addAll(MainActivity.db.structureDao().getAll());
			List<Session> sessions = MainActivity.db.sessionDao().getAll();
			getActivity().runOnUiThread(() -> {
				ArrayList<BarEntry> values = new ArrayList<>();
				ArrayList<String> labels = new ArrayList<String>();
				Integer maxCount = 0;
				ArrayList<IBarDataSet> dataSets = new ArrayList<>();
				for (Structure structure : structures) {
					Integer count = 0;
					for (Session session : sessions) {
						if (session.getStructure().getId().equals(structure.getId())) {
							count++;
						}
					}
					structureSessionCounts.add(count);
					values.add(new BarEntry(structures.indexOf(structure) + 1, count));
					labels.add(structure.getName());
					if (count > maxCount)
						maxCount = count;
				}

				BarDataSet set1 = new BarDataSet(values, "");
				set1.setDrawIcons(false);
				set1.setColors(getColors());
				dataSets.add(set1);

				BarData data = new BarData(dataSets);
				data.setBarWidth(0.5f);
				data.setDrawValues(false);
				barChart.setData(data);
				barChart.setScaleEnabled(false);
				barChart.setScaleXEnabled(false);
				barChart.setScaleYEnabled(false);
				barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
				barChart.getAxisLeft().setAxisMaximum(maxCount + 1);
				barChart.notifyDataSetChanged();
				barChart.invalidate();

				// for (int i = (int) start; i < start + count; i++) {
				// 	float val = (float) (Math.random() * (range + 1));
				//
				// 	if (Math.random() * 100 < 25) {
				// 		values.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.star)));
				// 	} else {
				// 		values.add(new BarEntry(i, val));
				// 	}
				// }

				barChart.setVisibility(View.VISIBLE);
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