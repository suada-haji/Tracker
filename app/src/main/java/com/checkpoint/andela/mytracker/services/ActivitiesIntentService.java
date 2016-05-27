package com.checkpoint.andela.mytracker.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.helpers.Constants;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;


/**
 * Created by suadahaji.
 */
public class ActivitiesIntentService extends IntentService {

    private static final String TAG = "ActivitiesIntentService";

    public ActivitiesIntentService() {
        super(TAG);
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
