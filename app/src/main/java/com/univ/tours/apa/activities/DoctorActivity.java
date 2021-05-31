package com.univ.tours.apa.activities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.DoctorPagerAdapter;

public class DoctorActivity extends BaseToolbarActivity {

    private DoctorPagerAdapter doctorPagerAdapter;
    private ViewPager viewPager;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        fm = getSupportFragmentManager();
        doctorPagerAdapter = new DoctorPagerAdapter(this, fm);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(doctorPagerAdapter);
    }
}