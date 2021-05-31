package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.univ.tours.apa.R;
import com.univ.tours.apa.adapters.CollaboratorPagerAdapter;
import com.univ.tours.apa.adapters.DoctorPagerAdapter;

public class CollaboratorActivity extends BaseToolbarActivity {

    private CollaboratorPagerAdapter collaboratorPagerAdapter;
    private ViewPager viewPager;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborator);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

            fm = getSupportFragmentManager();
            collaboratorPagerAdapter = new CollaboratorPagerAdapter(this, fm);
            viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(collaboratorPagerAdapter);
    }
}