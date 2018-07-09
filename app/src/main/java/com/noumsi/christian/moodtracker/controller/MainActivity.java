package com.noumsi.christian.moodtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.adapters.PageAdapter;
import com.noumsi.christian.moodtracker.view.VerticalViewPager;

public class MainActivity extends AppCompatActivity {

    private int[] mMoods;
    private int[] mColors;
    private int mFirstMood;
    //private int mFirstColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoods = new int[]{
                R.drawable.smiley_sad,
                R.drawable.smiley_disappointed,
                R.drawable.smiley_normal,
                R.drawable.smiley_happy,
                R.drawable.smiley_super_happy
        };
        mColors = new int[]{
                R.color.faded_red,
                R.color.warm_grey,
                R.color.cornflower_blue_65,
                R.color.light_sage,
                R.color.banana_yellow
        };

        // TODO: Verify if we don't have saved data (Mood) and loaded it
        mFirstMood = R.drawable.smiley_happy;
        //mFirstColor = R.color.light_sage;

        configureVerticalViewPager();
    }

    // Initialise view pager widget and set adapter to it
    private void configureVerticalViewPager(){
        VerticalViewPager viewPager = findViewById(R.id.activity_main_view_pager_vertical);

        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), mMoods, mFirstMood, mColors));
    }
}
