package com.checkpoint.andela.mytracker.helpers;


import android.os.Handler;

/**
 * Created by suadahaji.
 */
public class Watch {

    private long hours;
    private long minutes;
    private long seconds;
    private Handler handler;
    private long startDuration;
    private long elapsedDuration;

    private String secStr, minStr, hrStr;
    private boolean isStopped;


    public Watch() {
        handler = new Handler();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            elapsedDuration = System.currentTimeMillis() - startDuration;
            updateWatch(getElapsedDuration());
            handler.postDelayed(this, Constants.MILLISECONDS);
        }
    };

    public void startWatch() {
        startDuration = System.currentTimeMillis();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 0);
    }

    public void stopWatch() {
        startDuration = System.currentTimeMillis();
        isStopped = true;
    }

    private void updateWatch(float duration) {
        seconds = (long) duration/ 1000;
        minutes = seconds / 60;
        hours = minutes / 60;
    }

    public String secondsToString() {
        seconds = seconds % 60;
        secStr = String.valueOf(seconds);

        if(seconds == 0){
            secStr = "00";
        }

        if(seconds <10 && seconds > 0){
            secStr = "0"+ secStr;
        }

        return secStr;
    }

    public String minuteToString() {
        minutes = minutes % 60;
        minStr =String.valueOf(minutes);

        if(minutes == 0){
            minStr = "00";
        }

        if(minutes <10 && minutes > 0){
            minStr = "0" + minStr;
        }

        return minStr;
    }

    public String hourToString() {
        hrStr = String.valueOf(hours);

        if(hours == 0){
            hrStr = "00";
        }

        if(hours < 10 && hours > 0){
            hrStr = "0" + hrStr;
        }

        return hrStr;
    }

    public long getStartDuration() {
        return startDuration;
    }

    public void setStartDuration(long startDuration) {
        this.startDuration = startDuration;
    }

    public long getElapsedDuration() {
        return elapsedDuration;
    }

    public void setElapsedDuration(long elapsedDuration) {
        this.elapsedDuration = elapsedDuration;
    }

    public String getSecStr() {
        return secStr;
    }

    public void setSecStr(String secStr) {
        this.secStr = secStr;
    }

    public String getMinStr() {
        return minStr;
    }

    public void setMinStr(String minStr) {
        this.minStr = minStr;
    }

    public String getHrStr() {
        return hrStr;
    }

    public void setHrStr(String hrStr) {
        this.hrStr = hrStr;
    }
}
