package com.karas.tracklaptime.utils;

import android.os.SystemClock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackTimeService {
    private String timeR;
    private long millisecondTime;
    private long startTime;
    private long timeBuff;
    private long updateTime;
    private long millisecondTime2;
    private long startTime2;
    private long timeBuff2;
    private long updateTime2 = 0L;
    private String lastFullTime;
    private int seconds;
    private int minutes;
    private int milliSeconds;
    private int currentLap = 0;
    private int lastLap;
    private int iteration = 0;
    private int seconds2;
    private int minutes2;
    private int milliSeconds2;
    private int numofClick = 0;
    private int lapsToEnd = 0;

    public void incrementIteration() {
        this.iteration++;
    }


    public void reset() {
        this.millisecondTime = 0L;
        this.startTime = 0L;
        this.timeBuff = 0L;
        this.updateTime = 0L;
        this.seconds = 0;
        this.minutes = 0;
        this.milliSeconds = 0;
        this.lastLap = currentLap;
        this.currentLap = 0;

        this.millisecondTime2 = 0L;
        this.startTime2 = 0L;
        this.timeBuff2 = 0L;
        this.updateTime2 = 0L;
        this.seconds2 = 0;
        this.minutes2 = 0;
        this.numofClick = 0;
        this.lapsToEnd = 0;
    }

    public void initialize() {
        this.millisecondTime = SystemClock.uptimeMillis() - startTime;

        this.updateTime = timeBuff + millisecondTime;
        this.seconds = (int) (updateTime / 1000);
        this.minutes = seconds / 60;
        this.seconds = seconds % 60;
        this.milliSeconds = (int) (updateTime % 1000);

    }

    public void incrementLaps() {
        incrementIteration();
        this.currentLap++;
    }

    public void decrementLaps() {
        incrementIteration();
        this.currentLap--;
    }

    public void incrementNumOfClicks() {
        numofClick++;
    }

    public void setSeconds2Mod60() {
        this.seconds2 = this.seconds2 % 60;
    }

    public void addTimeToPauseTimeBuff() {
        this.timeBuff += this.millisecondTime;
    }

}
