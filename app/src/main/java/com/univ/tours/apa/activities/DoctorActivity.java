package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.DoctorPagerAdapter;

import android.os.Bundle;

public class DoctorActivity extends BaseToolbarActivity {

    private DoctorPagerAdapter doctorPagerAdapter;
    private ViewPager viewPager;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        fm = getSupportFragmentManager();
        doctorPagerAdapter = new DoctorPagerAdapter(this, fm);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(doctorPagerAdapter);
    }
}