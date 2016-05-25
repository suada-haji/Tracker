package com.checkpoint.andela.mytracker.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.checkpoint.andela.mytracker.slidingTab.locations.Movement;
import com.checkpoint.andela.mytracker.slidingTab.movement.Locations;
import com.google.android.gms.location.places.Place;

/**
 * Created by suadahaji.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Title[];
    int NumbofTabs;

    public ViewPagerAdapter(FragmentManager fragmentManager, CharSequence titles[], int numbofTabs) {
        super(fragmentManager);
        this.Title =titles;
        this.NumbofTabs = numbofTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Movement();
            case 1:
                return new Locations();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Title[position];
    }
    @Override
    public int getCount() {
        return NumbofTabs;
    }
}
