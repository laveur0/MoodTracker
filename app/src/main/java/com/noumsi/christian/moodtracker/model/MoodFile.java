package com.noumsi.christian.moodtracker.model;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christian-noumsi on 12/07/2018.
 */
public class MoodFile {

    private static String sFileName = "mood.txt";

    /**
     * Add a mood to the file of history mood
     * @param context context of application
     * @param mood object to add
     */
    public static void writeMood(Context context, Mood mood){
        List<Mood> moods = new ArrayList<>();
        try {
            if (!context.getFileStreamPath(sFileName).exists())
                moods.add(mood); // if file does not exit, we add new mood directly in list of moods
            else { // else we get all mood file in list and we append new mood in list
                readMoodList(context, moods);
                moods.add(mood);
            }
            // We write the actualise list in file
            writeMoodList(context, moods);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write in file all moods list
     * @param moods moods list to add in file
     */
    private static void writeMoodList(Context context, List<Mood> moods){
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput(sFileName, Context.MODE_PRIVATE)))) {
            for (Mood mood : moods) {
                objectOutputStream.writeObject(mood);
            }
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get all moods in file like Object and saved it in mood list
     * @param moods list for saved mood data in file
     * @throws IOException input output exception to catch
     * @throws ClassNotFoundException class not found exception to catch
     */
    public static void readMoodList(Context context, List<Mood> moods) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(context.openFileInput(sFileName))) {
            while (true) {
                try {
                    Mood mood = (Mood)ois.readObject();
                    moods.add(mood);
                } catch (EOFException ex) {
                    break;
                }
            }
        }
    }

    /**
     * Delete mood in the file
     * We getting all mood in list, remove mood at position and rewriting file with list
     * @param position position of mood to be deleted
     */
    public static void deleteMood(Context context, int position) {
        List<Mood> moods = new ArrayList<>();
        try {
            readMoodList(context, moods);
            moods.remove(position);
            writeMoodList(context, moods);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
