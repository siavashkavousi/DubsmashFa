package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by sia on 10/3/15.
 */
public class AdapterPager extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public AdapterPager(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override public int getCount() {
        return mFragments.size();
    }

    @Override public CharSequence getPageTitle(int position) {
        if (mFragments.get(position) instanceof FragmentSounds){
            return "صدا ها";
        } else if (mFragments.get(position) instanceof FragmentVideos){
            return "داب ها";
        }
        return null;
    }
}
