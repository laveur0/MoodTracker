package com.noumsi.christian.moodtracker.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.adapters.PageAdapter;
import com.noumsi.christian.moodtracker.model.Mood;
import com.noumsi.christian.moodtracker.model.MoodFile;
import com.noumsi.christian.moodtracker.view.VerticalViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String PREF_POSITION_MOOD = "mood_position";
    private static final String PREF_NOTE_MOOD = "mood_note";
    private static final String PREF_DATE_MOOD = "mood_date";
    public static final String PREF_SAVED_MOOD_NUMBER = "saved_mood_number";

    private List<Mood> mMoodList;
    private String mDate;
    private int mPositionFistMood;
    private String mNoteFirstMood;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getPreferences(MODE_PRIVATE);
        initMoodList();

        // If date has changed, we save mood in file
        if (!isSameDate()) {
            if (!mDate.isEmpty()){
                loadMoodOfPreferences();
                saveMoodInFile();
            }
            // We initialise date, position and note because the day has changed
            initialiseMoodPreferences();
            // We save mood in preferences
            saveMoodInPreferences();
        }
        else {
            // We load saved preferences
            loadMoodOfPreferences();
        }
        configureVerticalViewPager();
        configureImageButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If it is not same date, we save mood in file
        if (!isSameDate()){
            saveMoodInFile();
            saveMoodInPreferences();
        }
    }

    // Initialise note and history image button, and add onclick listener
    private void configureImageButton() {
        // Initialise button widgets
        ImageButton noteButton = findViewById(R.id.activity_main_add_note_ibtn);
        ImageButton historyButton = findViewById(R.id.activity_main_history_ibtn);

        // We define tag for button
        noteButton.setTag(1);
        historyButton.setTag(2);
        // We set onClick listener on it
        noteButton.setOnClickListener(this);
        historyButton.setOnClickListener(this);
    }

    // We initialise mood list
    private void initMoodList(){
        mMoodList = new ArrayList<>();

        mMoodList.add(new Mood(R.drawable.smiley_sad, R.color.faded_red, "",""));
        mMoodList.add(new Mood(R.drawable.smiley_disappointed, R.color.warm_grey, "",""));
        mMoodList.add(new Mood(R.drawable.smiley_normal, R.color.cornflower_blue_65, "",""));
        mMoodList.add(new Mood(R.drawable.smiley_happy, R.color.light_sage, "",""));
        mMoodList.add(new Mood(R.drawable.smiley_super_happy, R.color.banana_yellow, "",""));
    }

    // Initialise view pager widget and set adapter to it
    private void configureVerticalViewPager(){
        VerticalViewPager verticalViewPager = findViewById(R.id.activity_main_view_pager_vertical);

        verticalViewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), mMoodList));
        // Define the current item
        verticalViewPager.setCurrentItem(mPositionFistMood);
        // listening change of page and get position value
        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                // If date has changed i save mood in file
                if (!isSameDate()){
                    // We save precedent mood preferences in file mood.txt
                    saveMoodInFile();
                }
                mPositionFistMood = position;
                // saving in preferences mood
                saveMoodInPreferences();
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    // Here, we save position of actual mood in preferences
    private void saveMoodInPreferences(){
        mSharedPreferences.edit().putInt(PREF_POSITION_MOOD, mPositionFistMood).apply();
        mSharedPreferences.edit().putString(PREF_NOTE_MOOD, mNoteFirstMood).apply();
        String actualDate = new SimpleDateFormat(getString(R.string.date_format), Locale.FRENCH).format(Calendar.getInstance().getTime());
        mSharedPreferences.edit().putString(PREF_DATE_MOOD, actualDate).apply();
    }

    // We initialise attributes of mood
    private void initialiseMoodPreferences(){
        mDate = "";
        mPositionFistMood = 3;
        mNoteFirstMood = "";
    }

    /**
     * We try to load position and note of mood
     * If date has change, we save last preferences in file and initialise values
     */
    private void loadMoodOfPreferences(){
        if (mDate.isEmpty()){
            // No date mean no saved data in preferences
            initialiseMoodPreferences();
        } else {
            mPositionFistMood = mSharedPreferences.getInt(PREF_POSITION_MOOD, 3);
            mNoteFirstMood = mSharedPreferences.getString(PREF_NOTE_MOOD, "");
        }
    }

    private boolean isSameDate(){
        // We get actual date
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_format), Locale.FRENCH);
        String actualDate = simpleDateFormat.format(calendar);

        // We load saved date
        mDate = mSharedPreferences.getString(PREF_DATE_MOOD, "");
        return mDate.equalsIgnoreCase(actualDate);
    }

    // save mood object in a file
    private void saveMoodInFile() {
        Mood mood = new Mood(mMoodList.get(mPositionFistMood).getImageRef(), mMoodList.get(mPositionFistMood).getColorRef(), mNoteFirstMood, mDate);

        // We get the number of mood in file saved in sharePreferences
        int savedMoodNumber = mSharedPreferences.getInt(PREF_SAVED_MOOD_NUMBER, 0);
        // We clear PREF_SAVED_MOOD_NUMBER in preferences
        mSharedPreferences.edit().remove(PREF_SAVED_MOOD_NUMBER).apply();
        // If up to 6 mood are saved, we get all moods in list
        if (savedMoodNumber > 6){
            MoodFile.deleteMood(this, 0);
        } else {
            savedMoodNumber++;
        }
        mSharedPreferences.edit().putInt(PREF_SAVED_MOOD_NUMBER, savedMoodNumber).apply();
        // finally we add new mood in file
        MoodFile.writeMood(this, mood);
    }

    @Override
    public void onClick(View v) {
        // If we try to modify note or go to next activity and date has changed, we save mood in file
        if (!isSameDate()){
            saveMoodInFile();
            saveMoodInPreferences();
        }
        switch ((int)v.getTag()){
            case 1:
                // We configure an editable text
                final EditText editText = new EditText(this);
                editText.setTextIsSelectable(true);
                editText.setText(mNoteFirstMood);
                editText.setSelection(mNoteFirstMood.length());

                // Implement AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog alertDialog = builder.setTitle(R.string.alert_dialog_note_title)
                        .setView(editText)
                        .setPositiveButton(R.string.alert_dialog_note_positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNoteFirstMood = editText.getText().toString();
                                // After we modified note, we save mood in preferences
                                saveMoodInPreferences();
                            }
                        }).setNegativeButton(R.string.alert_dialog_note_negative_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.home_button));
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.home_button));
                            }
                        });
                alertDialog.show();
                break;
            case 2:
                // Open the last seven Mood History Activity
                startActivity(new Intent(MainActivity.this, MoodHistoryActivity.class));
                break;
        }
    }
}