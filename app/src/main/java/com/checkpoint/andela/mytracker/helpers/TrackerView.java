package com.checkpoint.andela.mytracker.helpers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.model.TrackerModel;
import com.checkpoint.andela.mytracker.services.ActivitiesIntentService;
import com.checkpoint.andela.mytracker.services.LocationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by suadahaji.
 */
public class TrackerView extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status>, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TrackerView";
    private LocationManager locationManager;
    private ActivityReceiver mBroadcastActivityReceiver;
    private ToggleButton toggleButton;
    private TextView activityText;
    private TextView locationText;
    private CustomTextViewFont timeUsedText;
    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private boolean notify;
    private Notifier notifier;
    private Timer watch;
    private TimerListener timerListener;
    private boolean timeElapsed;
    private CountDownTimer countDownTimer;
    private String currentActivity;
    private String currentLocation;
    private int currentDuration;
    private SharedPreferenceSettings preferenceSettings;
    private GoogleApiClient googleApiClient;
    private String activity;
    private TrackerModel trackerModel;
    private SQLiteDatabase database;
    private TrackerDbHelper trackerDbHelper;
    private LocationRequest locationRequest;
    private LocationReceiver locationReceiver;
    private String mAddressOutput;
    protected boolean mAddressRequested;
    private Location location;


    public boolean checkGPSAvailabilty() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void makeToastText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void initializeValues() {
        mBroadcastActivityReceiver = new ActivityReceiver();

        toggleButton = (ToggleButton) findViewById(R.id.tracker_button);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean start = ((ToggleButton) v).isChecked();

                if (!checkOnline() && (!checkGPSAvailabilty()) && start) {
                    makeToastText("GPS and Internet not turned on");
                    ((ToggleButton) v).setChecked(false);
                } else if (!checkOnline() && (checkGPSAvailabilty()) && start) {
                    makeToastText("No Internet Connection");
                    ((ToggleButton) v).setChecked(false);
                } else if (checkOnline() && (!checkGPSAvailabilty()) && start) {
                    makeToastText("Please turn on GPS");
                } else if (start) {
                    startRecording();
                    Log.e("Start Recording", "Database created or opened... ");
                } else {
                    stopRecording();
                }
            }
        });

        locationText = (TextView) findViewById(R.id.location_text);
        setLocationTextChange();

        activityText = (TextView) findViewById(R.id.activity_text);
        setActivityTextChange();

        timeUsedText = (CustomTextViewFont) findViewById(R.id.timer_text);

        context = getApplicationContext();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastActivityReceiver, new IntentFilter(Constants.STRING_ACTION));

        locationReceiver = new LocationReceiver(new Handler());
        mAddressRequested = false;
        activity = "";
        mAddressOutput = "";

        currentActivity = activityText.getText().toString().trim();
        currentLocation = locationText.getText().toString().trim();
        currentDuration = 0;

        timeElapsed = false;
        notify = true;

        preferenceSettings = new SharedPreferenceSettings(this);

        trackerDbHelper = new TrackerDbHelper(this);
        database = trackerDbHelper.getWritableDatabase();
        trackerModel = new TrackerModel();
        notifier = new Notifier(context, TrackerView.this);
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        watch = new Timer(this);

        timerListener = new TimerListener() {
            @Override
            public void onTick(String timeSpent) {
                if (watch.timer) {
                    timeUsedText.setText(timeSpent);
                } else {
                    timeUsedText.setText("00:00:00");
                }
            }
        };
        watch.setTimerListener(timerListener);


    }

    private boolean checkOnline() {
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

/**
 * This registers and unregisters the broadcast receiver when the activity resumes and pauses respectively.*/
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastActivityReceiver, new IntentFilter(Constants.STRING_ACTION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastActivityReceiver);
        super.onPause();
    }

    public void startRecording() {
        activityText.setText(R.string.connecting);
        notify = true;
        watch.turnOn();

        if (!timeElapsed) {
            countDownTime(watch.formatDelayText(preferenceSettings.getTime()));
        }

        detectActivity();
    }

    public void stopRecording() {
        activityText.setText(R.string.not_tracking);
        watch.turnOff();
        notifier.cancelNotification(this, 1);
        timeElapsed = false;
        notify = false;
        //reload();
        if (timeElapsed) {
            //ActivityLauncher.runIntent(this, ListByDate.class);
        } else {
           // reload();
        }
    }

    protected synchronized void getGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
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
                currentLocation = locationText.getText().toString();
                currentActivity = activityText.getText().toString();
                currentDuration = watch.timeInSeconds;
            }

            @Override
            public void afterTextChanged(Editable s) {
                activity = activityText.getText().toString();
                if (activityText.equals(R.string.connecting)) {
                    watch.reset();
                }
                if (isReady()) {
                    recordTrack();
                    watch.reset();
                }
            }
        });
    }

    public void recordTrack() {
        trackerModel.setTracker_activity(currentActivity);
        trackerModel.setTracker_location(currentLocation);
        trackerModel.setTracker_duration(currentDuration);
        insertRecord(trackerModel);
    }
    private boolean insertRecord(TrackerModel model) {
        cupboard().withDatabase(database).put(model);
        return true;

    }

    public boolean isReady() {
        return (!activityText.getText().toString().equals(currentActivity))
                && (!currentActivity.equals(R.string.connecting)) && (!currentActivity.equals(R.string.tracking_stopped))
                && (!currentActivity.equals(R.string.not_tracking)) && (timeElapsed);
    }


    public void countDownTime(int timer) {
        if (!activity.equals(R.string.connecting) && notify) {
            countDownTimer = new CountDownTimer(timer * Constants.MINUTES_TO_MILLISECONDS, Constants.BEEP_IN_MILLISECONDS) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if ((!activity.equals("Connecting....") && (!activity.equals("Tracking Stopped"))) && notify) {
                        timeElapsed = true;
                        notifier.sendNotification("MyTracker");
                    }

                }
            }.start();
        }
    }

    public void detectActivity() {
        if (checkOnline()) {
            Intent intent = new Intent(this, ActivitiesIntentService.class);
            startService(intent);

            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, 0, pendingIntent);
        } else {
            makeToastText("No Internet Connection");
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


public class ActivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        DetectedActivity detectedActivity = intent.getParcelableExtra(Constants.STRING_EXTRA);
        String activity =getDetectedActivity(detectedActivity.getType());
        activityText.setText(activity);
    }
}

    public class LocationReceiver extends ResultReceiver {
        public LocationReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            locationText.setText(mAddressOutput);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
        googleApiClient.disconnect();
         }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (checkOnline()) {
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(500);

            try {
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

                // LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } catch (SecurityException exception) {
                System.out.println("Security Exception");
            }

            startIntentService();
        }
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, LocationIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, locationReceiver );

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        locationText.setText(mAddressOutput);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onResult(Status status) {
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
