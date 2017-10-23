package com.costa.alex.evolutionsimulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Alex on 10/21/2017.
 * PagerAdapter for main ViewPager
 */
class PagerAdapter extends FragmentStatePagerAdapter {

    PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0 :
                fragment = new HomeFragment();
                Bundle hArgs = new Bundle();
                hArgs.putString("Page Title", "Home");
                fragment.setArguments(hArgs);
                break;
            case 1 :
                fragment = new PopulationFragment();
                Bundle pArgs = new Bundle();
                pArgs.putString("Page Title", "Population");
                fragment.setArguments(pArgs);
                break;
            default :
                fragment = new HomeFragment();
                Bundle dArgs = new Bundle();
                dArgs.putString("Page Title", "Home");
                fragment.setArguments(dArgs);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        switch (position) {
            case 0 :
                title = "Home";
                break;
            case 1 :
                title = "Population";
                break;
            default:
                title = "Home";
                break;
        }
        return title;
    }
}
