package com.checkpoint.andela.mytracker.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.checkpoint.andela.mytracker.helpers.Constants;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;


/**
 * Created by suadahaji.
 */
public class ActivitiesIntentService extends IntentService {

    public static final String CURRENT_ACTIVITY = "ActivitiesIntentService";

    /** the constructor is required. It calls the super IntentService(String)
     * constructor with the name of a worker thread*/
    public ActivitiesIntentService() {
        super(CURRENT_ACTIVITY);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        if (result != null) {
            DetectedActivity activity = result.getMostProbableActivity();
            Intent i = new Intent(Constants.STRING_ACTION);
            i.putExtra(Constants.STRING_EXTRA, activity);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
    }
}
