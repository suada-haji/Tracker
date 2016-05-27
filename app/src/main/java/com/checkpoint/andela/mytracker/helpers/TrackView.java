package com.checkpoint.andela.mytracker.helpers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.activities.ListActivity;
import com.checkpoint.andela.mytracker.model.TrackerModel;
import com.checkpoint.andela.mytracker.services.LocationGoogleAPIService;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by suadahaji.
 */
public class TrackView  extends AppCompatActivity implements ResultCallback<Status> {

    private CustomTextViewFont hour;
    private CustomTextViewFont minute;
    private CustomTextViewFont second;
    private TextView activityText;
    private TextView locationText;

    private boolean isRecording;
    private FloatingActionButton fab;
    private LocationGoogleAPIService locationGoogleAPIService;
    private ActivitiesService activitiesIntentService;
    private LocationTypeListener listener;
    private Watch watch;
    private TimerCountDown timerCountDown;
    private String current_activity;
    private String initial_activity;
    private DetectedActivity detectedActivity;
    private Setting settings;
    private TrackerModel model;
    private String location;
    private long elapsedDuration;
    private String activity;
    private Date date;
    private SimpleDateFormat simpleDateFormat;

    public void initializeValues() {
        activitiesIntentService = new ActivitiesService();
        model = new TrackerModel();
        fab = (FloatingActionButton) findViewById(R.id.home_fab);
        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        hour = (CustomTextViewFont) findViewById(R.id.hour_timer_text);
        minute = (CustomTextViewFont) findViewById(R.id.minute_timer_text);
        second = (CustomTextViewFont) findViewById(R.id.second_timer_text);
        activity = "";
        activityText = (TextView) findViewById(R.id.activity_text);
        setActivityTextChange();
        simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        date = new Date();
        current_activity = activityText.getText().toString().trim();
        locationText = (TextView) findViewById(R.id.location_text);
        setLocationTextChange();
        isRecording = false;
        watch = new Watch();
        locationGoogleAPIService = new LocationGoogleAPIService(this);
        getTrackerLocation();
        settings = new Setting(this);
    }

    public void setLocationTextChange() {
        locationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (locationText.getText().toString().isEmpty()) {
                    locationText.setText("Unknown Location");
                }
            }
        });
    }

    public void setActivityTextChange() {
        activityText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                location = locationText.getText().toString();
                initial_activity= activityText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                activity = activityText.getText().toString();
            }
        });
    }
    public void record(View view) {
        if (!isRecording) {
            startRecording(view);
        } else {
            stopRecording(view);
        }
    }

    public void startRecording(View view) {
        if (!settings.checkGPSAvailabilty()) {
            settings.requestGPSSettings();
            return;
        }
        if (!settings.checkOnline()) {
            settings.requestConnectivity();
            return;
        }
        if (!locationGoogleAPIService.isGoogleApiClientConnected()) {
            settings.requestGooglePlayServices();
            return;
        }

        activityText.setText(R.string.connecting);
        getCurrentUserActivity();
        changeIcon();
        isRecording = true;
        watch.startWatch();
        timerCountDown = new TimerCountDown(watch.getStartDuration(), 10);
        timerCountDown.start();
    }

    public void stopRecording(View view) {
        changeIcon();
        isRecording = false;
        timerCountDown.cancel();
        elapsedDuration = watch.getElapsedDuration();
        if (ifReadyToSave()) {
            insertRecord(getTrackerModel());
        }
        locationGoogleAPIService.disconnect();
        ActivityLauncher.runIntent(this, ListActivity.class);
    }


    private void changeIcon() {
        if (!isRecording){
            fab.setImageResource(R.drawable.ic_media_pause);
            isRecording = true;
        }
        else {
            fab.setImageResource(R.drawable.ic_media_play);
        }
    }

    public void getTrackerLocation() {
        listener = new LocationTypeListener() {
            @Override
            public void onLocationChange(String name) {
                location = name;
                if (isRecording) {
                    locationText.setText(location);
                }
            }
        };
        locationGoogleAPIService.setLocationTypeListener(listener);
    }


    public void getCurrentUserActivity() {
        Intent intent = new Intent(this, com.checkpoint.andela.mytracker.services.ActivitiesIntentService.class);
        startService(intent);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(locationGoogleAPIService.getGoogleAPI(), 0, pendingIntent);

    }

    private boolean ifReadyToSave() {
        return elapsedDuration >= getDelayDuration() && current_activity.equals("Standing Still") && !location.equals("") && !location.equals("Unknown Location") ;
    }

    public long getDelayDuration() {
        Setting setting = new Setting(this);
        String value = setting.getSettings();
        DurationTime durationTime = setting.getDurationTime(value);
        return durationTime.convertTime();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationGoogleAPIService.connect();
        activitiesIntentService = new ActivitiesService();
        activityText.setText(current_activity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationGoogleAPIService.isGoogleApiClientConnected()) {
            locationGoogleAPIService.disconnect();
        }
    }

    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(activitiesIntentService);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(activitiesIntentService,
                new IntentFilter(Constants.STRING_ACTION));
    }

    @Override
    public void onResult(Status status) {

    }

    public class TimerCountDown extends CountDownTimer {

        public TimerCountDown(long millis, long interval) {
            super(millis, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String seconds = watch.secondsToString();
            String minutes = watch.minuteToString();
            String hours = watch.hourToString();
            hour.setText(hours);
            minute.setText(minutes);
            second.setText(seconds);
            elapsedDuration = watch.getElapsedDuration();
        }

        @Override
        public void onFinish() {

        }
    }

    public class ActivitiesService extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            detectedActivity = intent.getParcelableExtra(Constants.STRING_EXTRA);
            current_activity = getDetectedActivity(detectedActivity.getType());
            if (initial_activity == null) {
                initial_activity = current_activity;
            }
            if (hasActivityChanged(initial_activity, current_activity)) {
                activityText.setText(current_activity);
                resetTimer();
            } else {
                return;
            }

            if(isRecording) {
                activityText.setText(current_activity);
            }
        }


        public boolean hasActivityChanged(String initialActivity, String newActivity) {
            return !initialActivity.equalsIgnoreCase(newActivity);

        }
    }

    public String getDetectedActivity(int detectedActivityType) {
        Resources resources = this.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }


    public void insertRecord(TrackerModel model) {
        TrackerDbHelper trackerDbHelper = new TrackerDbHelper(this);
        DBManager dbManager = new DBManager(trackerDbHelper);
        dbManager.saveToDB(model);
    }

    public TrackerModel getTrackerModel() {
        model.setLocation(location);
        model.setCoordinates(locationGoogleAPIService.getCoordinates());
        model.setDuration(elapsedDuration);
        model.setTracker_date(simpleDateFormat.format(date));
        return model;
    }

    public void resetTimer() {
        elapsedDuration = watch.getElapsedDuration();
        if (ifReadyToSave()) {
            insertRecord(getTrackerModel());
        }
        watch.stopWatch();
        try {
            Thread.sleep(1000);
            watch.startWatch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
