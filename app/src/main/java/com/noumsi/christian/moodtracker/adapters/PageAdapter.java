package com.noumsi.christian.moodtracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.noumsi.christian.moodtracker.controller.fragments.MoodPageFragment;
import com.noumsi.christian.moodtracker.model.Mood;

import java.util.List;

/**
 * Created by christian-noumsi on 09/07/2018.
 */
public class PageAdapter extends FragmentPagerAdapter {

    private List<Mood> mMoodList;

    public PageAdapter(FragmentManager fm, List<Mood> moodList) {
        super(fm);
        mMoodList = moodList;
    }

    @Override
    public Fragment getItem(int position) {
        return MoodPageFragment.newInstance(position, mMoodList.get(position));
    }

    @Override
    public int getCount() {
        return mMoodList.size();
    }
}
