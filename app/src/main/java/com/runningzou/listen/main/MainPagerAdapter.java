package com.runningzou.listen.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by zouzhihao on 2017/10/19.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles;
    private Fragment[] mFragments;

    public MainPagerAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
        super(fm);
        mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        if (mTitles == null) {
            return 0;
        }
        return mTitles.length;
    }
}
