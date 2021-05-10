package com.univ.tours.apa;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class DemoPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    FragmentManager fm;

    public DemoPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position) {
            case 0:
                return DemoFragment.newInstance(fm);
            case 1:
                return AddCourseFragment.newInstance(fm);
            case 2:
                return CalendarFragment.newInstance(fm);
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_demo_fragment).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.add_course).toUpperCase(l);
            case 2:
                return mContext.getString(R.string.calendar).toUpperCase(l);

        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 1 total pages.
        return 3;
    }
}