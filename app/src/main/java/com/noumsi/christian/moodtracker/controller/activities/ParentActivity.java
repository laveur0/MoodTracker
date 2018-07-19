package com.noumsi.christian.moodtracker.controller.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.model.Mood;
import com.noumsi.christian.moodtracker.model.MoodFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by christian-noumsi on 19/07/2018.
 */
public abstract class ParentActivity extends AppCompatActivity {

    public static final String FILE_NAME_PREF = "mood_pref";
    public static final String PREF_SAVED_MOOD_NUMBER = "saved_mood_number";
    private static final String PREF_POSITION_MOOD = "mood_position";
    private static final String PREF_NOTE_MOOD = "mood_note";
    private static final String PREF_DATE_MOOD = "mood_date";
    protected List<Mood> mMoodList;
    protected String mDate;
    protected int mPositionFistMood;
    protected String mNoteFirstMood;
    protected SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initMoodList();
        mSharedPreferences = getSharedPreferences(FILE_NAME_PREF, MODE_PRIVATE);

        // If date has changed, we save mood in file
        if (!isSameDate()) {
            if (!mDate.isEmpty()) {
                loadMoodOfPreferences();
                saveMoodInFile();
            }
            // We initialise date, position and note because the day has changed
            initialiseMoodPreferences();
            // We save mood in preferences
            saveMoodInPreferences();
        } else {
            // We load saved preferences
            loadMoodOfPreferences();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If it is not same date, we save mood in file
        if (!isSameDate()) {
            saveMoodInFile();
            saveMoodInPreferences();
        }
    }

    // We initialise mood list
    private void initMoodList() {
        mMoodList = new ArrayList<>();

        mMoodList.add(new Mood(R.drawable.smiley_sad, R.color.faded_red, "", ""));
        mMoodList.add(new Mood(R.drawable.smiley_disappointed, R.color.warm_grey, "", ""));
        mMoodList.add(new Mood(R.drawable.smiley_normal, R.color.cornflower_blue_65, "", ""));
        mMoodList.add(new Mood(R.drawable.smiley_happy, R.color.light_sage, "", ""));
        mMoodList.add(new Mood(R.drawable.smiley_super_happy, R.color.banana_yellow, "", ""));
    }

    // We initialise attributes of mood
    private void initialiseMoodPreferences() {
        mDate = "";
        mPositionFistMood = 3;
        mNoteFirstMood = "";
    }

    protected boolean isSameDate() {
        // We get actual date
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_format), Locale.FRENCH);
        String actualDate = simpleDateFormat.format(calendar);

        // We load saved date
        mDate = mSharedPreferences.getString(PREF_DATE_MOOD, "");
        return mDate.equalsIgnoreCase(actualDate);
    }

    /**
     * We try to load position and note of mood
     * If date has change, we save last preferences in file and initialise values
     */
    protected void loadMoodOfPreferences() {
        if (mDate.isEmpty()) {
            // No date mean no saved data in preferences
            initialiseMoodPreferences();
        } else {
            mPositionFistMood = mSharedPreferences.getInt(PREF_POSITION_MOOD, 3);
            mNoteFirstMood = mSharedPreferences.getString(PREF_NOTE_MOOD, "");
        }
    }

    // Here, we save position of actual mood in preferences
    protected void saveMoodInPreferences() {
        mSharedPreferences.edit().putInt(PREF_POSITION_MOOD, mPositionFistMood).apply();
        mSharedPreferences.edit().putString(PREF_NOTE_MOOD, mNoteFirstMood).apply();
        String actualDate = new SimpleDateFormat(getString(R.string.date_format), Locale.FRENCH).format(Calendar.getInstance().getTime());
        mSharedPreferences.edit().putString(PREF_DATE_MOOD, actualDate).apply();
    }

    // save mood object in a file
    protected void saveMoodInFile() {
        Mood mood = new Mood(mMoodList.get(mPositionFistMood).getImageRef(), mMoodList.get(mPositionFistMood).getColorRef(), mNoteFirstMood, mDate);

        // We get the number of mood in file saved in sharePreferences
        int savedMoodNumber = mSharedPreferences.getInt(PREF_SAVED_MOOD_NUMBER, 0);
        // We clear PREF_SAVED_MOOD_NUMBER in preferences
        mSharedPreferences.edit().remove(PREF_SAVED_MOOD_NUMBER).apply();

        // If up to 6 mood are saved, we get all moods in list
        if (savedMoodNumber > 6) {
            MoodFile.deleteMood(this, 0);
        } else {
            savedMoodNumber++;
        }
        // we save new mood saved number
        mSharedPreferences.edit().putInt(PREF_SAVED_MOOD_NUMBER, savedMoodNumber).apply();
        // finally we add new mood in file
        MoodFile.writeMood(this, mood);
    }
}
