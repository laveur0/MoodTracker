package com.noumsi.christian.moodtracker.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.model.Mood;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoodPageFragment extends Fragment {

    private static final String KEY_POSITION = "position";
    private static final String KEY_MOOD = "mood";

    public MoodPageFragment() {
        // Required empty public constructor
    }

    /**
     * Method will able to create an instance of the fragment with some parameters
     * necessary for display correct smile and color
     * @param position the position of smile in array of smiles
     * @param mood A Mood object
     * @return
     */
    public static MoodPageFragment newInstance(int position, Mood mood){

        MoodPageFragment moodPageFragment = new MoodPageFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_MOOD, mood);
        args.putInt(KEY_POSITION, position);

        moodPageFragment.setArguments(args);

        return moodPageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood_page, container, false);

        // Get widgets from layout and serialise it
        ImageView smileImg = view.findViewById(R.id.fragment_mood_page_smile_img);

        // Get data from bundle
        int position = getArguments().getInt(KEY_POSITION, -1);
        Mood mood = (Mood) getArguments().getSerializable(KEY_MOOD);

        // Update widgets with data
        view.setBackgroundResource(mood.getColorRef());
        smileImg.setImageResource(mood.getImageRef());

        return view;
    }

}
