package com.noumsi.christian.moodtracker.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.adapters.PageAdapter;

import me.kaelaela.verticalviewpager.VerticalViewPager;

public class MainActivity extends ParentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureVerticalViewPager();
        configureImageButton();
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

    // Initialise view pager widget and set adapter to it
    private void configureVerticalViewPager() {
        VerticalViewPager verticalViewPager = findViewById(R.id.activity_main_view_pager_vertical);

        verticalViewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), mMoodList));
        // Define the current item
        verticalViewPager.setCurrentItem(mPositionFistMood);
        // listening change of page and get position value
        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // If date has changed i save mood in file
                if (!isSameDate())
                    saveMoodInFile();
                mPositionFistMood = position;
                // saving in preferences mood
                saveMoodInPreferences();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_add_note_ibtn:
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
                                // If date has changed i save mood in file
                                if (!isSameDate())
                                    saveMoodInFile();
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
            case R.id.activity_main_history_ibtn:
                // Open the last seven Mood History Activity
                startActivity(new Intent(MainActivity.this, MoodHistoryActivity.class));
                break;
        }
    }
}