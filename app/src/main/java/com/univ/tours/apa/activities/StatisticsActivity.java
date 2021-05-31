package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.CollaboratorPagerAdapter;
import com.univ.tours.apa.adapters.StatisticsPagerAdapter;
import com.univ.tours.apa.entities.User;

public class StatisticsActivity extends BaseToolbarActivity {

	private StatisticsPagerAdapter statisticsPagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		Toolbar myToolbar = findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		viewPager = findViewById(R.id.view_pager);

		// setupViewPager();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("TAG", "onResume: ");
		setupViewPager();
	}

	private void setupViewPager() {
		Log.d("TAG", "setupViewPager: ");
		new Thread(() -> {
			User user = MainActivity.db.userDao().findById(MainActivity.getLoggedInUserId());
			runOnUiThread(() -> {
				Log.d("TAG", "user.getRole(): " + user.getRole());
				if (user.getRole().equals("ROLE_DOCTOR")) {
					statisticsPagerAdapter = new StatisticsPagerAdapter(this, getSupportFragmentManager(), 1);
				} else if (user.getRole().equals("ROLE_COLLABORATOR")) {
					statisticsPagerAdapter = new StatisticsPagerAdapter(this, getSupportFragmentManager(), 2);
				} else {
					statisticsPagerAdapter = new StatisticsPagerAdapter(this, getSupportFragmentManager(), 3);
				}
				viewPager.setAdapter(statisticsPagerAdapter);
			});
		}).start();
	}
}