package com.checkpoint.andela.mytracker.activities;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.helpers.ActivityCallback;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;
import com.checkpoint.andela.mytracker.helpers.ActivityTypeListener;
import com.checkpoint.andela.mytracker.helpers.Constants;
import com.checkpoint.andela.mytracker.helpers.CustomTextViewFont;
import com.checkpoint.andela.mytracker.helpers.DBManager;
import com.checkpoint.andela.mytracker.helpers.DurationTime;
import com.checkpoint.andela.mytracker.helpers.Setting;
import com.checkpoint.andela.mytracker.helpers.TrackerDbHelper;
import com.checkpoint.andela.mytracker.helpers.Watch;
import com.checkpoint.andela.mytracker.model.TrackerModel;
import com.checkpoint.andela.mytracker.services.LocationGoogleAPIService;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, ResultCallback<Status> {

    private static final String TAG = "TrackerView";
    private CustomTextViewFont hour;
    private CustomTextViewFont minute;
    private CustomTextViewFont second;
    private TextView activityText;
    private TextView locationText;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private RelativeLayout layout;
    private boolean isRecording;
    private FloatingActionButton fab;

    private LocationGoogleAPIService locationGoogleAPIService;
    private ActivitiesService activitiesIntentService;
    private ActivityTypeListener listener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null) {
            elapsedDuration = savedInstanceState.getLong(Constants.ELAPSED_TIME);
            initial_activity = savedInstanceState.getString(Constants.START_ACTIVITY);
            current_activity = savedInstanceState.getString(Constants._ACTIVITY);
        }
        initializeComponents();
        initializeValues();
    }

    public void initializeComponents() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setItemIconTintList(null);

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

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
        locationGoogleAPIService = new LocationGoogleAPIService(Home.this);
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
        timerCountDown = new TimerCountDown(watch.getStartDuration(), 100);
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
        listener = new ActivityTypeListener() {
            @Override
            public void onActivityTypeChange(String name) {
                location = name;
                if (isRecording) {
                    locationText.setText(location);
                }
            }
        };
        locationGoogleAPIService.setActivityTypeListener(listener);
    }


    public void getCurrentUserActivity() {
        Intent intent = new Intent(this, com.checkpoint.andela.mytracker.services.ActivitiesIntentService.class);
        startService(intent);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(locationGoogleAPIService.getGoogleAPI(), 0, pendingIntent);
    }

    private boolean ifReadyToSave() {
        return elapsedDuration >= getDelayDuration();
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
            if (isSignificant(detectedActivity)) {
                if (initial_activity == null) {
                    initial_activity = current_activity;
                }
                if (hasActivityChanged(initial_activity, current_activity)) {
                    if (ifReadyToSave()) {
                        insertRecord(getTrackerModel());
                    }
                    resetTimer();

                } else {
                    return;
                }
            }
            if(isRecording) {
                activityText.setText(current_activity);
            }
        }


        public boolean hasActivityChanged(String initialActivity, String newActivity) {
            return !initialActivity.equalsIgnoreCase(newActivity);

        }

        private boolean isSignificant(DetectedActivity detectedActivity) {
            int type = detectedActivity.getType();
            switch (type) {
                case DetectedActivity.STILL:
                    return true;
                case DetectedActivity.IN_VEHICLE:
                    return true;
                case DetectedActivity.WALKING:
                    return true;

                case DetectedActivity.ON_BICYCLE:
                    return true;

                case DetectedActivity.ON_FOOT:
                    return false;

                case DetectedActivity.UNKNOWN:
                    return false;

                case DetectedActivity.RUNNING:
                    return true;

                default:
                    return false;
            }

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(Constants.ELAPSED_TIME, elapsedDuration);
        outState.putString(Constants.START_ACTIVITY, initial_activity);
        outState.putString(Constants._ACTIVITY, current_activity);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        elapsedDuration = savedInstanceState.getLong(Constants.ELAPSED_TIME);
        initial_activity = savedInstanceState.getString(Constants.START_ACTIVITY);
        current_activity = savedInstanceState.getString(Constants._ACTIVITY);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {

        } if (id == R.id.nav_list) {
            ActivityLauncher.runIntent(this, ListActivity.class);
            finish();

        } if (id == R.id.nav_settings) {
            ActivityLauncher.runIntent(this, PreferenceSettings.class);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(back_pressed + 2000 > System.currentTimeMillis()) {
            moveTaskToBack(true);
            finish();
        } else {
            getToastMessage("Press once again to exit");
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getToastMessage(String message) {

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    public void resetTimer() {
        watch.stopWatch();
        try {
            Thread.sleep(1000);
            watch.startWatch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
