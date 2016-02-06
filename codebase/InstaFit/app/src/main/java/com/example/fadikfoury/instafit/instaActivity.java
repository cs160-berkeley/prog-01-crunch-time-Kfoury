package com.example.fadikfoury.instafit;

/**
 * Created by fadikfoury on 2/3/16.
 */
public class instaActivity {
    private String name;
    private int iconID;
    private boolean isReps;
    private int repsRate;
    private int durationRate;


    // initialzer
    public instaActivity(String name, int iconID, boolean isReps, int repsRate, int durationRate)
    {

        super();
        this.name = name;
        this.isReps = isReps;
        this.iconID = iconID;
        this.repsRate = repsRate;
        this.durationRate = durationRate;



    }

    public String getName() {
        return name;
    }

    public int getIconID() {
        return iconID;
    }

    public boolean isReps() {
        return isReps;
    }

    public int getRepsRate() {
        return repsRate;
    }

    public int getDurationRate() {
        return durationRate;
    }





}
