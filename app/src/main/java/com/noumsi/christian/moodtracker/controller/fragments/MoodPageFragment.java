package com.noumsi.christian.moodtracker.controller.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.model.Mood;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoodPageFragment extends Fragment {

    private static final String KEY_MOOD = "mood";

    public MoodPageFragment() {
        // Required empty public constructor
    }

    /**
     * Method will able to create an instance of the fragment with some parameters
     * necessary for display correct smile and color
     * @param mood A Mood object
     * @return moodPageFragment
     */
    public static MoodPageFragment newInstance(Mood mood){
        MoodPageFragment moodPageFragment = new MoodPageFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_MOOD, mood);

        moodPageFragment.setArguments(args);

        return moodPageFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood_page, container, false);

        // Get widgets from layout and serialise it
        ImageView smileImg = view.findViewById(R.id.fragment_mood_page_smile_img);

        // Get data from bundle
        assert getArguments() != null;
        Mood mood = (Mood) getArguments().getSerializable(KEY_MOOD);

        // Update widgets with data
        assert mood != null;
        view.setBackgroundResource(mood.getColorRef());
        smileImg.setImageResource(mood.getImageRef());

        return view;
    }

}
