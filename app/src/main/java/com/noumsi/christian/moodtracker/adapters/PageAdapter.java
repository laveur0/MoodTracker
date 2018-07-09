package com.noumsi.christian.moodtracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.noumsi.christian.moodtracker.controller.fragments.MoodPageFragment;

/**
 * Created by christian-noumsi on 09/07/2018.
 */
public class PageAdapter extends FragmentPagerAdapter {

    private int[] mColors;
    private int[] mMoods;
    private int mPrimaryMood;

    public PageAdapter(FragmentManager fm, int[] moods, int primaryMood, int[] colors) {
        super(fm);
        mMoods = moods;
        mColors = colors;
        mPrimaryMood = primaryMood;
    }

    /*
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, mPrimaryMood, object);
    }
    */

    @Override
    public Fragment getItem(int position) {
        return MoodPageFragment.newInstance(position, mMoods[position], mColors[position]);
    }

    @Override
    public int getCount() {
        return mMoods.length;
    }
}
