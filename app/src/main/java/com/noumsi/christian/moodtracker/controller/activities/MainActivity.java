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
import com.noumsi.christian.moodtracker.view.VerticalViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        // We load saved preferences
        loadMoodPreferences();

        configureVerticalViewPager();
        configureImageButton();
    }

    // Initialise note and history button, and add onclick listener
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
                mPositionFistMood = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    // Here, we save position of actual mood in preferences
    private void saveTemporallyMoodPreferences(){
        mSharedPreferences.edit().putInt(PREF_POSITION_MOOD, mPositionFistMood).apply();
        mSharedPreferences.edit().putString(PREF_NOTE_MOOD, mNoteFirstMood).apply();
        String actualDate = new SimpleDateFormat("dd / MM / yyyy", Locale.FRENCH).format(Calendar.getInstance().getTime());
        mSharedPreferences.edit().putString(PREF_DATE_MOOD, actualDate).apply();
    }

    // Here, we load position of saved mood
    private void loadMoodPreferences(){
        // We get date and if it is different of actual date, we initialise preferences
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd / MM / yyyy", Locale.FRENCH);
        String actualDate = simpleDateFormat.format(calendar);
        mDate = mSharedPreferences.getString(PREF_DATE_MOOD, "");

        if (mDate.isEmpty()){
            // No date mean no saved data
            mPositionFistMood = 3;
            mNoteFirstMood = "";
        } else {
            mPositionFistMood = mSharedPreferences.getInt(PREF_POSITION_MOOD, 0);
            mNoteFirstMood = mSharedPreferences.getString(PREF_NOTE_MOOD, "");
            if (!mDate.equalsIgnoreCase(actualDate)){
                // We save precedent mood preferences finally in a mood.txt file
                saveMoodInFile(mPositionFistMood, mNoteFirstMood, mDate);
                // We clear preferences
                mSharedPreferences.edit().clear().apply();
                // We initialise position and note because the day has changed
                mPositionFistMood = 3;
                mNoteFirstMood = "";
            }
        }
    }

    // Register mood object in a file
    private void saveMoodInFile(int position, String note, String date) {
        Mood mood = new Mood(mMoodList.get(position).getImageRef(), mMoodList.get(position).getColorRef(), note, date);
        // We are going to open file mood.txt in writing for save cars
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("mood.txt")))){
            objectOutputStream.writeObject(mood);
        } catch (IOException e) {
            System.out.println("Problème d'écriture de l'objet mMood dans le fichier mood.txt!");
        }
    }

    @Override
    protected void onStop() {
        // We save the position of actual mood
        saveTemporallyMoodPreferences();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch ((int)v.getTag()){
            case 1:
                // We configure an editable text
                final EditText editText = new EditText(this);
                editText.setTextIsSelectable(true);
                editText.setText(mNoteFirstMood);
                editText.setSelection(mNoteFirstMood.length());

                // Implement AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog alertDialog = builder.setTitle("Commentaire")
                        .setView(editText)
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNoteFirstMood = editText.getText().toString();
                            }
                        }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
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
        }
    }
}