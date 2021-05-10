package com.univ.tours.apa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private DemoPagerAdapter demoPagerAdapter;
    private ViewPager viewPager;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        demoPagerAdapter = new DemoPagerAdapter(this, fm);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(demoPagerAdapter);
    }
}