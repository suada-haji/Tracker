package com.checkpoint.andela.mytracker.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.checkpoint.andela.mytracker.R;

/**
 * Created by suadahaji.
 */
public class Setting {
    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public Setting(Context context) {
        this.context = context;
    }

    public DurationTime getDurationTime(String durationTime) {
        String[] arguments = durationTime.split(":");
        DurationTime duration = null;
        if (arguments.length < 2) {
            return new DurationTime(0, 5);
        }
        try {
            return new DurationTime(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DurationTime(0, 5);
    }

    public String getSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.DURATION_KEY, "0:5");
    }

    public boolean checkOnline() {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public boolean checkGPSAvailabilty() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Request permission to turn on GPS
     */
    public void requestGPSSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("GPS is turned off")
                .setPositiveButton("Go to GPS settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);

                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Request permission to turn on Connectivity
     */
    public void requestConnectivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Internet is not turned on")
                .setPositiveButton("Check Internet Connection", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Request permission to enable or update Google play services
     */
    public void requestGooglePlayServices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.common_google_play_services_enable_title)
                .setPositiveButton("My Tracker will not work unless you enable Google Play services.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
