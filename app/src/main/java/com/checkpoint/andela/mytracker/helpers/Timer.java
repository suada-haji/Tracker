package com.checkpoint.andela.mytracker.helpers;

import android.app.Activity;

/**
 * Created by suadahaji.
 */
public class Timer {

    public  int timeInSeconds;
    private Activity activity;
    public  Boolean timer;
    public int increment;
    private String timeUsed;
    private TimerListener timerListener;

    public Timer(Activity activity) {

        this.activity = activity;
    }

    public Timer() {

    }

    public void setTimerListener(TimerListener timerListener) {

        this.timerListener = timerListener;
    }

    public void turnOn() {

        this.timer = true;
        updateTimer();
    }

    public void turnOff() {

        reset();
        this.timer = false;
    }

    public void reset() {

        increment = 0;
        timeInSeconds = 0;
    }

    public void updateTimer() {

        reset();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted() && timer) {
                        Thread.sleep(1000);
                        increment++;
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                timeInSeconds = increment;
                                timeUsed = formatTime(increment);
                                timerListener.onTick(timeUsed);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    public String formatTime(int seconds) {

        int hour = seconds/Constants.SECONDS_TO_HOUR;
        int rem = seconds%Constants.SECONDS_TO_HOUR;
        int minute = rem/Constants.SECONDS_TO_MINUTES;
        int second = rem%60;
        String hourStr = (hour<10 ? "0" : "")+hour;
        String minuteStr = (minute<10 ? "0" : "")+minute;
        String secondStr = (second<10 ? "0" : "")+second;

        return hourStr+":"+minuteStr+":"+secondStr;
    }

    public int formatDelayText(String delayText) {

        return Integer.parseInt(delayText.split(" ")[0]);
    }

}
