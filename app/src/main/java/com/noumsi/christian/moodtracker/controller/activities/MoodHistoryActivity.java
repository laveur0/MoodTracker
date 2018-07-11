package com.noumsi.christian.moodtracker.controller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.adapters.MoodListAdapter;
import com.noumsi.christian.moodtracker.model.Mood;

import java.util.ArrayList;
import java.util.List;

public class MoodHistoryActivity extends AppCompatActivity {

    List<Mood> mMoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        getAllMoodHistory();

        configureListView();
    }

    // get the seven last saved mood
    private void getAllMoodHistory(){
        mMoodList = new ArrayList<>();
        mMoodList.add(new Mood(R.drawable.smiley_super_happy, R.color.banana_yellow, "très Heureux pour mon application que je suis en train de réaliser depuis une semaine déjà", "1 / 7 / 2018"));
        mMoodList.add(new Mood(R.drawable.smiley_sad, R.color.faded_red, "", "5 / 7 / 2018"));
        mMoodList.add(new Mood(R.drawable.smiley_happy, R.color.light_sage, "Heureux de la vie", "6 / 7 / 2018"));
        mMoodList.add(new Mood(R.drawable.smiley_normal, R.color.cornflower_blue_65, "", "7 / 7 / 2018"));
        mMoodList.add(new Mood(R.drawable.smiley_disappointed, R.color.warm_grey, "triste", "8 / 7 / 2018"));
        mMoodList.add(new Mood(R.drawable.smiley_super_happy, R.color.banana_yellow, "très Heureux pour ma résussite", "9 / 7 / 2018"));
        mMoodList.add(new Mood(R.drawable.smiley_sad, R.color.faded_red, "", "10 / 7 / 2018"));
    }

    private void configureListView(){
        // serialize list view and set an adapter
        ListView listView = findViewById(R.id.activity_mood_history_list_view);
        listView.setAdapter(new MoodListAdapter(this, mMoodList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.findViewById(R.id.mood_history_item_comment_img).getVisibility() == View.VISIBLE){

                    // We personalise a textView
                    TextView textView = new TextView(MoodHistoryActivity.this);
                    textView.setBackgroundResource(R.color.toast_bg);
                    textView.setText(mMoodList.get(position).getNote());
                    textView.setTextSize(18);
                    textView.setAllCaps(false);
                    textView.setPadding(80, 30, 80, 30);
                    textView.setTextColor(getResources().getColor(R.color.toast_text));

                    // We create a toast and add the textView
                    Toast toast = new Toast(MoodHistoryActivity.this);
                    toast.setView(textView);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
