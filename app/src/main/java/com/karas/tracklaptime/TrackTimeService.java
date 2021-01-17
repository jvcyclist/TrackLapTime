package com.karas.tracklaptime;

import android.os.SystemClock;

public class TrackTimeService {
    String timeR;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime,MillisecondTime2,StartTime2,TimeBuff2,UpdateTime2 = 0L ;
    String lastFullTime;
    int Seconds, Minutes, MilliSeconds,Lap=0,lastLap ;
    int Seconds2,Minutes2,MilliSeconds2;
    int numofClick=0;

    public void reset() {
        this.MillisecondTime = 0L ;
        this.StartTime = 0L ;
        this.TimeBuff = 0L ;
        this.UpdateTime = 0L ;
        this.Seconds = 0 ;
        this.Minutes = 0 ;
        this.MilliSeconds = 0 ;
        this.lastLap=Lap;
        this.Lap=0;

        this.MillisecondTime2 = 0L ;
        this.StartTime2 = 0L ;
        this.TimeBuff2 = 0L ;
        this.UpdateTime2 = 0L ;
        this.UpdateTime2 = 0L ;
        this.Seconds2 = 0 ;
        this.Minutes2 = 0 ;
        this.numofClick=0;
    }

    public void initialize() {
        this.MillisecondTime = SystemClock.uptimeMillis() - StartTime;

        this.UpdateTime = TimeBuff + MillisecondTime;
        this.Seconds = (int) (UpdateTime / 1000);
        this.Minutes = Seconds / 60;
        this.Seconds = Seconds % 60;
        this.MilliSeconds = (int) (UpdateTime % 1000);

    }

    public void incrementLaps() {
        this.Lap++;
    }

    public void incrementNumofClicks() {
        numofClick++;
    }

    public void setSeconds2Mod60(){
        this.Seconds2 = this.Seconds2 % 60;
    }

    public void addTimeToPauseTimeBuff() {
        this.TimeBuff += this.MillisecondTime;
    }





    public String getTimeR() {
        return timeR;
    }

    public void setTimeR(String timeR) {
        this.timeR = timeR;
    }

    public long getMillisecondTime() {
        return MillisecondTime;
    }

    public void setMillisecondTime(long millisecondTime) {
        MillisecondTime = millisecondTime;
    }

    public long getStartTime() {
        return StartTime;
    }

    public void setStartTime(long startTime) {
        StartTime = startTime;
    }

    public long getTimeBuff() {
        return TimeBuff;
    }

    public void setTimeBuff(long timeBuff) {
        TimeBuff = timeBuff;
    }

    public long getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(long updateTime) {
        UpdateTime = updateTime;
    }

    public long getMillisecondTime2() {
        return MillisecondTime2;
    }

    public void setMillisecondTime2(long millisecondTime2) {
        MillisecondTime2 = millisecondTime2;
    }

    public long getStartTime2() {
        return StartTime2;
    }

    public void setStartTime2(long startTime2) {
        StartTime2 = startTime2;
    }

    public long getTimeBuff2() {
        return TimeBuff2;
    }

    public void setTimeBuff2(long timeBuff2) {
        TimeBuff2 = timeBuff2;
    }

    public long getUpdateTime2() {
        return UpdateTime2;
    }

    public void setUpdateTime2(long updateTime2) {
        UpdateTime2 = updateTime2;
    }

    public String getLastFullTime() {
        return lastFullTime;
    }

    public void setLastFullTime(String lastFullTime) {
        this.lastFullTime = lastFullTime;
    }

    public int getSeconds() {
        return Seconds;
    }

    public void setSeconds(int seconds) {
        Seconds = seconds;
    }

    public int getMinutes() {
        return Minutes;
    }

    public void setMinutes(int minutes) {
        Minutes = minutes;
    }

    public int getMilliSeconds() {
        return MilliSeconds;
    }

    public void setMilliSeconds(int milliSeconds) {
        MilliSeconds = milliSeconds;
    }

    public int getLap() {
        return Lap;
    }

    public void setLap(int lap) {
        Lap = lap;
    }

    public int getLastLap() {
        return lastLap;
    }

    public void setLastLap(int lastLap) {
        this.lastLap = lastLap;
    }

    public int getSeconds2() {
        return Seconds2;
    }

    public void setSeconds2(int seconds2) {
        Seconds2 = seconds2;
    }

    public int getMinutes2() {
        return Minutes2;
    }

    public void setMinutes2(int minutes2) {
        Minutes2 = minutes2;
    }

    public int getMilliSeconds2() {
        return MilliSeconds2;
    }

    public void setMilliSeconds2(int milliSeconds2) {
        MilliSeconds2 = milliSeconds2;
    }

    public int getNumofClick() {
        return numofClick;
    }

    public void setNumofClick(int numofClick) {
        this.numofClick = numofClick;
    }
}
