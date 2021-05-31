package com.univ.tours.apa.adapters;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.univ.tours.apa.R;
import com.univ.tours.apa.fragments.common.stats.StatsCollaboratorActivitiesFragment;
import com.univ.tours.apa.fragments.common.stats.StatsCoursesCompletionRateFragment;
import com.univ.tours.apa.fragments.common.stats.StatsDoctorCoursesFragment;
import com.univ.tours.apa.fragments.common.stats.StatsPatientSessionsFragment;
import com.univ.tours.apa.fragments.common.stats.StatsStructuresFragment;

import java.util.Locale;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class StatisticsPagerAdapter extends FragmentPagerAdapter {

    private final android.app.Activity parentActivity;
    private int contentType;

    public StatisticsPagerAdapter(android.app.Activity parentActivity, FragmentManager fm, int contentType) {
        super(fm);
        this.parentActivity = parentActivity;
        this.contentType = contentType;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (contentType) {
            // ROLE_DOCTOR
            case 1:
                switch (position) {
                    case 0:
                        return StatsDoctorCoursesFragment.newInstance();
                    case 1:
                        return StatsCoursesCompletionRateFragment.newInstance();
                    case 2:
                        return StatsStructuresFragment.newInstance();
                }
                break;
            // ROLE_COLLABORATOR
            case 2:
                switch (position) {
                    case 0:
                        return StatsCollaboratorActivitiesFragment.newInstance();
                    case 1:
                        return StatsCoursesCompletionRateFragment.newInstance();
                    case 2:
                        return StatsStructuresFragment.newInstance();
                }
                break;
            // ROLE_PATIENT
            case 3:
                switch (position) {
                    case 0:
                        return StatsPatientSessionsFragment.newInstance();
                }
                break;
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (contentType) {
            // ROLE_DOCTOR
            case 1:
                switch (position) {
                    case 0:
                        return parentActivity.getString(R.string.Courses).toUpperCase(l);
                    case 1:
                        return parentActivity.getString(R.string.Patients).toUpperCase(l);
                    case 2:
                        return parentActivity.getString(R.string.Structures).toUpperCase(l);
                }
                break;
            // ROLE_COLLABORATOR
            case 2:
                switch (position) {
                    case 0:
                        return parentActivity.getString(R.string.Activities).toUpperCase(l);
                    case 1:
                        return parentActivity.getString(R.string.Patients).toUpperCase(l);
                    case 2:
                        return parentActivity.getString(R.string.Structures).toUpperCase(l);
                }
                break;
            // ROLE_PATIENT
            case 3:
                switch (position) {
                    case 0:
                        return parentActivity.getString(R.string.Sessions).toUpperCase(l);
                }
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 1 total pages.
        switch (contentType) {
            // ROLE_DOCTOR
            case 1:
                return 3;
            // ROLE_COLLABORATOR
            case 2:
                return 3;
            // ROLE_PATIENT
            case 3:
                return 1;
            default:
                break;
        }
        return 0;
    }
}