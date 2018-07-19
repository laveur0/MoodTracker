package com.noumsi.christian.moodtracker.controller.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.model.Mood;
import com.noumsi.christian.moodtracker.model.MoodFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MoodHistoryActivity extends ParentActivity implements View.OnClickListener {

    List<Mood> mMoods;
    private LinearLayout mLinearLayout;
    private TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        // We get all mood in file "mood.txt"
        getAllMoodHistory();
        // we associate xml widgets to my attributes
        serializeWidgets();
    }

    private void serializeWidgets() {
        for (int i = 0; i < 7; i++) {
            // For each turn we serialize widget corresponding and apply params of mood
            switch (i) {
                case 0:
                    mLinearLayout = findViewById(R.id.activity_mood_history_7);
                    mTextView = findViewById(R.id.activity_mood_history_text_view_7);
                    mImageView = findViewById(R.id.activity_mood_history_comment_img_7);
                    configureTextViewAndImageView(i);
                    break;
                case 1:
                    mLinearLayout = findViewById(R.id.activity_mood_history_6);
                    mTextView = findViewById(R.id.activity_mood_history_text_view_6);
                    mImageView = findViewById(R.id.activity_mood_history_comment_img_6);
                    configureTextViewAndImageView(i);
                    break;
                case 2:
                    mLinearLayout = findViewById(R.id.activity_mood_history_5);
                    mTextView = findViewById(R.id.activity_mood_history_text_view_5);
                    mImageView = findViewById(R.id.activity_mood_history_comment_img_5);
                    configureTextViewAndImageView(i);
                    break;
                case 3:
                    mLinearLayout = findViewById(R.id.activity_mood_history_4);
                    mTextView = findViewById(R.id.activity_mood_history_text_view_4);
                    mImageView = findViewById(R.id.activity_mood_history_comment_img_4);
                    configureTextViewAndImageView(i);
                    break;
                case 4:
                    mLinearLayout = findViewById(R.id.activity_mood_history_3);
                    mTextView = findViewById(R.id.activity_mood_history_text_view_3);
                    mImageView = findViewById(R.id.activity_mood_history_comment_img_3);
                    configureTextViewAndImageView(i);
                    break;
                case 5:
                    mLinearLayout = findViewById(R.id.activity_mood_history_2);
                    mTextView = findViewById(R.id.activity_mood_history_text_view_2);
                    mImageView = findViewById(R.id.activity_mood_history_comment_img_2);
                    configureTextViewAndImageView(i);
                    break;
                case 6:
                    mLinearLayout = findViewById(R.id.activity_mood_history_1);
                    mTextView = findViewById(R.id.activity_mood_history_text_view_1);
                    mImageView = findViewById(R.id.activity_mood_history_comment_img_1);
                    configureTextViewAndImageView(i);
                    break;
            }
        }
    }

    // We define the background color of view, set date and show or hide imageView
    private void configureTextViewAndImageView(int position) {
        float imgWeight = 0.5f;
        // if list of mood is empty, we do not do anything
        if (!mMoods.isEmpty() && mMoods.size() > position) {
            // We define color of layout
            mTextView.setBackgroundResource(mMoods.get(position).getColorRef());
            mImageView.setBackgroundResource(mMoods.get(position).getColorRef());

            // set on click listener on image view
            mImageView.setOnClickListener(this);

            // we set visibility of image view
            if (!mMoods.get(position).getNote().isEmpty()) {
                mImageView.setVisibility(View.VISIBLE);
                imgWeight = 0f;
            } else
                mImageView.setVisibility(View.GONE);

            // We define width of mood view
            switch (mMoods.get(position).getColorRef()) {
                case R.color.faded_red:
                    configureWidthMoodView(0.5f + imgWeight, 0.5f - imgWeight);
                    break;
                case R.color.warm_grey:
                    configureWidthMoodView(1.5f + imgWeight, 0.5f - imgWeight);
                    break;
                case R.color.cornflower_blue_65:
                    configureWidthMoodView(2.5f + imgWeight, 0.5f - imgWeight);
                    break;
                case R.color.light_sage:
                    configureWidthMoodView(3.5f + imgWeight, 0.5f - imgWeight);
                    break;
                case R.color.banana_yellow:
                    configureWidthMoodView(4.5f + imgWeight, 0.5f - imgWeight);
                    break;
            }
            // we set duration in text view
            mTextView.setText(getTimeDuration(position));
        } else
            mLinearLayout.setVisibility(View.GONE);
    }

    /**
     * We set weight of text and image view
     * @param textWeight weight of text view
     * @param imgWeight  weight of image view
     */
    private void configureWidthMoodView(float textWeight, float imgWeight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTextView.getLayoutParams();
        params.weight = textWeight;
        mTextView.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
        params.weight = imgWeight;
        mImageView.setLayoutParams(params);
    }

    /**
     * Calculate number of days/weeks between two date
     * @param position position of mood in list
     * @return result
     */
    private String getTimeDuration(int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_format), Locale.FRENCH);
        String result = "";
        try {
            Date pastDate = simpleDateFormat.parse(mMoods.get(position).getDate());
            // different between actual date and pass date
            long diff = Calendar.getInstance().getTimeInMillis() - pastDate.getTime();
            // we get the number of days who correspond to the difference between two date
            int numbDays = (int) TimeUnit.MILLISECONDS.toDays(diff);
            // We get plural resources
            if (numbDays == 1)
                result = getResources().getString(R.string.one_days);
            else if (numbDays == 2)
                result = getResources().getString(R.string.two_days);
            else if (numbDays < 7)
                result = getResources().getString(R.string.other_days, numbDays);
            else {
                int numbWeek = numbDays / 7, daysAfterWeek = (int) ((double) numbDays % 7);
                if (numbWeek == 1) {
                    if (daysAfterWeek == 0)
                        result = getResources().getString(R.string.one_week);
                    else if (daysAfterWeek == 1)
                        result = getResources().getString(R.string.one_week_one_days);
                    else
                        result = getResources().getString(R.string.one_week_other_days, daysAfterWeek);
                } else {
                    if (daysAfterWeek == 0)
                        result = getResources().getString(R.string.other_week, numbWeek);
                    else if (daysAfterWeek == 1)
                        result = getResources().getString(R.string.other_week_one_days, numbWeek);
                    else
                        result = getResources().getString(R.string.other_week_other_days, numbWeek, daysAfterWeek);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    // get the seven last saved mood
    private void getAllMoodHistory() {
        mMoods = new ArrayList<>();
        try {
            MoodFile.readMoodList(this, mMoods);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        configureToast(Integer.parseInt(v.getTag().toString()));
    }

    /**
     * Method to create a toast for display saved note of a view
     *
     * @param position position of mood concerned
     */
    private void configureToast(int position) {
        // We personalise a textView
        TextView textView = new TextView(MoodHistoryActivity.this);
        textView.setBackgroundResource(R.color.toast_bg);
        textView.setText(mMoods.get(position).getNote());
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