package com.noumsi.christian.moodtracker.model;

import java.io.Serializable;

/**
 * Created by christian-noumsi on 10/07/2018.
 */
public class Mood implements Serializable{
    private int mImageRef;
    private int mColorRef;
    private String mNote;
    private String mDate;

    public Mood(int imageRef, int colorRef, String note, String date) {
        mImageRef = imageRef;
        mColorRef = colorRef;
        mNote = note;
        mDate = date;
    }

    public int getImageRef() {
        return mImageRef;
    }

    public int getColorRef() {
        return mColorRef;
    }

    public String getNote() {
        return mNote;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
