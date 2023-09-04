package com.gisfy.unauthorizedlayouts.Util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gisfy.unauthorizedlayouts.FirstFragment;
import com.gisfy.unauthorizedlayouts.SecondFragment;
import com.gisfy.unauthorizedlayouts.ThirdFragment;

public  class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    public MyPagerAdapter(FragmentManager fragmentManager) {

        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }


    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FirstFragment();

            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
            default:
                return null;
        }
    }
}
