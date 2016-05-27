package com.checkpoint.andela.mytracker.helpers;

/**
 * Created by suadahaji.
 */
public class DurationTime {
    private int hr;
    private int min;
    private int sec;

    public DurationTime(int hr, int minute) {
        this.hr = hr;
        this.min = minute;

    }

    public DurationTime(int hour, int minute, int second) {
        this.hr = hour;
        this.min = minute;
        this.sec = second;
    }

    public DurationTime() {}

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public long convertTime() {
        return (((hr * Constants.SECONDS_TO_HOUR) + (min * Constants.SECONDS_TO_MINUTES)) * Constants.MILLISECONDS);
    }

    private String hrToString() {
       return (hr <= 0) ? "" : (hr + " hours: ");
    }

    private String minToString() {

        if (min <= 0) {
            return "";
        }

        return (min <= 1) ? (min + " minute") : (min + " minutes");
    }

    public String timeToString() {

        if (hr <= 0 && min <= 0) {
            return "No Set Time";
        }

        return hrToString() + minToString();
    }

    @Override
    public String toString() {

        return (hr <= 0 && min <= 0) ? "No Value set" : hr + ":" + min;
    }
}
