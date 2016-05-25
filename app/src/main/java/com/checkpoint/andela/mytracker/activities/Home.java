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
import android.widget.TextView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;
import com.checkpoint.andela.mytracker.helpers.ActivityTypeListener;
import com.checkpoint.andela.mytracker.helpers.Constants;
import com.checkpoint.andela.mytracker.helpers.CustomTextViewFont;
import com.checkpoint.andela.mytracker.helpers.DBManager;
import com.checkpoint.andela.mytracker.helpers.DurationTime;
import com.checkpoint.andela.mytracker.helpers.Setting;
import com.checkpoint.andela.mytracker.helpers.TrackView;
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

public class Home extends TrackView implements  NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private long elapsedDuration;
    private String current_activity;
    private String initial_activity;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return;
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Class activitySwitch = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_list:
                activitySwitch = ListActivity.class;
                break;
            case R.id.nav_settings:
                activitySwitch = PreferenceSettings.class;
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        ActivityLauncher.runIntent(this, activitySwitch);
        finish();
        return true;
    }


}
