package com.example.mahmoud.fcisquare.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer.CheckinPlacesFragment;
import com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer.FollowersFragment;
import com.example.mahmoud.fcisquare.view_controller.fragments.nivigationDrawer.SavedPlacesFragment;

/**
 * Created by Mahmoud on 5/6/2016.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"MyPlaces", "Checkins", "Followers"};
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SavedPlacesFragment();
        } else if (position == 1) {
            return new FollowersFragment();
        } else if (position == 2) {
            return new CheckinPlacesFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}