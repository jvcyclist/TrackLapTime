package com.karas.tracklaptime.utils;

import android.os.SystemClock;

public class TrackTimeService {
    private String timeR;
    private long millisecondTime, startTime, timeBuff, updateTime, millisecondTime2, startTime2, timeBuff2, updateTime2 = 0L ;
    private String lastFullTime;
    private int seconds, minutes, milliSeconds, currentLap =0,lastLap, iteration=0 ;
    private int seconds2, minutes2, milliSeconds2;
    private int numofClick=0;
    private int lapsToEnd=0;

    public int getLapsToEnd() {
        return lapsToEnd;
    }

    public void setLapsToEnd(int lapsToEnd) {
        this.lapsToEnd = lapsToEnd;
    }

    public void incrementIteration(){
        this.iteration++;
    }


    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public void reset() {
        this.millisecondTime = 0L ;
        this.startTime = 0L ;
        this.timeBuff = 0L ;
        this.updateTime = 0L ;
        this.seconds = 0 ;
        this.minutes = 0 ;
        this.milliSeconds = 0 ;
        this.lastLap= currentLap;
        this.currentLap =0;

        this.millisecondTime2 = 0L ;
        this.startTime2 = 0L ;
        this.timeBuff2 = 0L ;
        this.updateTime2 = 0L ;
        this.updateTime2 = 0L ;
        this.seconds2 = 0 ;
        this.minutes2 = 0 ;
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

    public void incrementNumofClicks() {
        numofClick++;
    }

    public void setSeconds2Mod60() {
        this.seconds2 = this.seconds2 % 60;
    }

    public void addTimeToPauseTimeBuff() {
    this.timeBuff += this.millisecondTime;
    }

    public String getTimeR() {
        return timeR;
    }

    public void setTimeR(String timeR) {
        this.timeR = timeR;
    }

    public long getMillisecondTime() {
        return millisecondTime;
    }

    public void setMillisecondTime(long millisecondTime) {
        this.millisecondTime = millisecondTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTimeBuff() {
        return timeBuff;
    }

    public void setTimeBuff(long timeBuff) {
        this.timeBuff = timeBuff;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getMillisecondTime2() {
        return millisecondTime2;
    }

    public void setMillisecondTime2(long millisecondTime2) {
        this.millisecondTime2 = millisecondTime2;
    }

    public long getStartTime2() {
        return startTime2;
    }

    public void setStartTime2(long startTime2) {
        this.startTime2 = startTime2;
    }

    public long getTimeBuff2() {
        return timeBuff2;
    }

    public void setTimeBuff2(long timeBuff2) {
        this.timeBuff2 = timeBuff2;
    }

    public long getUpdateTime2() {
        return updateTime2;
    }

    public void setUpdateTime2(long updateTime2) {
        this.updateTime2 = updateTime2;
    }

    public String getLastFullTime() {
        return lastFullTime;
    }

    public void setLastFullTime(String lastFullTime) {
        this.lastFullTime = lastFullTime;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getMilliSeconds() {
        return milliSeconds;
    }

    public void setMilliSeconds(int milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    public int getCurrentLap() {
        return currentLap;
    }

    public void setCurrentLap(int currentLap) {
        this.currentLap = currentLap;
    }

    public int getLastLap() {
        return lastLap;
    }

    public void setLastLap(int lastLap) {
        this.lastLap = lastLap;
    }

    public int getSeconds2() {
        return seconds2;
    }

    public void setSeconds2(int seconds2) {
        this.seconds2 = seconds2;
    }

    public int getMinutes2() {
        return minutes2;
    }

    public void setMinutes2(int minutes2) {
        this.minutes2 = minutes2;
    }

    public int getMilliSeconds2() {

        return milliSeconds2;
    }

    public void setMilliSeconds2(int milliSeconds2) {
        this.milliSeconds2 = milliSeconds2;
    }

    public int getNumofClick() {
        return numofClick;
    }

    public void setNumofClick(int numofClick) {
        this.numofClick = numofClick;
    }
}
