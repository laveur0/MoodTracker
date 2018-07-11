package com.noumsi.christian.moodtracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noumsi.christian.moodtracker.R;
import com.noumsi.christian.moodtracker.model.Mood;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by christian-noumsi on 11/07/2018.
 */
public class MoodListAdapter extends ArrayAdapter<Mood> {

    private List<Mood> mMoodList;
    private Context mContext;

    public MoodListAdapter(@NonNull Context context, List<Mood> moods) {
        super(context, R.layout.mood_history_item, moods);
        mContext = context;
        mMoodList = moods;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        convertView = layoutInflater.inflate(R.layout.mood_history_item, parent, false);

        // set all params to our view like width, height and color
        configureParamsOfConvertView(position, convertView, parent);

        // serialize widgets and configure them with mood data
        configureWidgets(position, convertView);

        return convertView;
    }

    private void configureWidgets(int position, View view) {
        TextView dateTxt = view.findViewById(R.id.mood_history_item_text_view);
        ImageView commentImg = view.findViewById(R.id.mood_history_item_comment_img);

        dateTxt.setText(getTimeDuration(position));

        if (mMoodList.get(position).getNote().isEmpty())
            commentImg.setVisibility(View.GONE);
        else
            commentImg.setVisibility(View.VISIBLE);
    }

    private String getTimeDuration(int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd / MM / yyyy", Locale.FRENCH);
        String result = "";
        try {
            Date passDate = simpleDateFormat.parse(mMoodList.get(position).getDate());
            // different between actual date and pass date
            long diff = Calendar.getInstance().getTimeInMillis() - passDate.getTime();
            // we get the number of days who correspond to the difference between two date
            int numbDays = (int) TimeUnit.MILLISECONDS.toDays(diff);

            if (numbDays == 1)
                result = "Hier";
            else if (numbDays == 2)
                result = "Avant-hier";
            else if (numbDays < 7)
                result = "Il y a " + numbDays + " jours";
            else if (numbDays == 7)
                result = "Il y a une semaine";
            else {
                int numbWeek = numbDays / 7;
                int daysAfterWeek = (int)((double)numbDays % 7);
                result = "Il y a " + numbWeek + " semaine(s) et " + daysAfterWeek + " jour(s)";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void configureParamsOfConvertView(int position, View view, ViewGroup parent) {
        // We set background color
        view.setBackgroundResource(mMoodList.get(position).getColorRef());

        // We set size of view
        switch (mMoodList.get(position).getColorRef()){
            case R.color.light_sage:
                view.setLayoutParams(new RelativeLayout.LayoutParams(((8 * parent.getWidth()) / 10), parent.getHeight() / 7));
                break;
            case R.color.faded_red:
                view.setLayoutParams(new RelativeLayout.LayoutParams(((2 * parent.getWidth()) / 10), parent.getHeight() / 7));
                break;
            case R.color.warm_grey:
                view.setLayoutParams(new RelativeLayout.LayoutParams(((4 * parent.getWidth()) / 10), parent.getHeight() / 7));
                break;
            case R.color.cornflower_blue_65:
                view.setLayoutParams(new RelativeLayout.LayoutParams(((6 * parent.getWidth()) / 10), parent.getHeight() / 7));
                break;
            default:
                view.setLayoutParams(new RelativeLayout.LayoutParams(parent.getWidth(), parent.getHeight() / 7));
                break;
        }
    }
}
