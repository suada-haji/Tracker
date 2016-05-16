package com.checkpoint.andela.mytracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by suadahaji.
 */
public class SharedPreferenceSettings {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferenceSettings(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void saveTime(String time) {
        editor.putString(Constants.TIME_KEY, time);
        editor.commit();
    }

    public String getTime() {
        return sharedPreferences.getString(Constants.TIME_KEY, Constants.DEFAULT_DELAY_TIME);
    }
}
