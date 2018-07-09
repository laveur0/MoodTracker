package com.noumsi.christian.moodtracker.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.noumsi.christian.moodtracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoodPageFragment extends Fragment {

    private static final String KEY_POSITION = "position";
    private static final String KEY_MOOD = "mood";
    private static final String KEY_COLOR = "color";

    public MoodPageFragment() {
        // Required empty public constructor
    }

    /**
     * Method will able to create an instance of the fragment with some parameters
     * necessary for display correct smile and color
     * @param position the position of smile in array of smiles
     * @param mood A reference to smile to display
     * @param color Reference to the color of smile
     * @return
     */
    public static MoodPageFragment newInstance(int position, int mood, int color){

        MoodPageFragment moodPageFragment = new MoodPageFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_MOOD, mood);
        args.putInt(KEY_COLOR, color);

        moodPageFragment.setArguments(args);

        return moodPageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood_page, container, false);

        // Get widgets from layout and serialise it
        LinearLayout linearLayout = view.findViewById(R.id.fragment_mood_page_linear_layout);
        ImageView smileImg = view.findViewById(R.id.fragment_mood_page_smile_img);

        // Get data from bundle
        int position = getArguments().getInt(KEY_POSITION, -1);
        int mood = getArguments().getInt(KEY_MOOD, -1);
        int color = getArguments().getInt(KEY_COLOR,-1);

        // Update widgets with data
        linearLayout.setBackgroundColor(color);
        smileImg.setImageResource(mood);

        return view;
    }

}
