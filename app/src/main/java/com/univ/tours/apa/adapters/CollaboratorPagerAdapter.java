package com.univ.tours.apa.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.univ.tours.apa.R;
import com.univ.tours.apa.fragments.collaborator.CollaboratorCourseBrowseFragment;
import com.univ.tours.apa.fragments.common.StructureBrowseFragment;
import com.univ.tours.apa.fragments.collaborator.CollaboratorSessionBrowseReschedulingRequestsFragment;

import java.util.Locale;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class CollaboratorPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    FragmentManager fm;

    public CollaboratorPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position) {
            case 0:
                return CollaboratorCourseBrowseFragment.newInstance(fm);
            case 1:
                return StructureBrowseFragment.newInstance(fm);
            case 2:
                return CollaboratorSessionBrowseReschedulingRequestsFragment.newInstance(fm);
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.Courses).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.Structures).toUpperCase(l);
            case 2:
                return mContext.getString(R.string.rescheduling_requests).toUpperCase(l);
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 1 total pages.
        return 3;
    }
}