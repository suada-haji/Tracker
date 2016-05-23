package com.checkpoint.andela.mytracker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.adapters.LocationListAdapter;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;
import com.checkpoint.andela.mytracker.helpers.DBManager;
import com.checkpoint.andela.mytracker.helpers.TrackerDbHelper;
import com.checkpoint.andela.mytracker.model.Places;
import com.checkpoint.andela.mytracker.model.TrackerModel;

import java.util.ArrayList;

public class ListByLocation extends AppCompatActivity {

    private LocationListAdapter dateListAdapter;
    private ArrayList<TrackerModel> trackerModelArrayList;
    private DBManager dbManager;
    private ListView listView;
    private ArrayList<Places> placesArrayList;
    private TrackerDbHelper trackerDbHelper;
    private Places places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.list_by_date_toolbar);
        setSupportActionBar(toolbar);
        getLocations();
        initializeComponents();
    }

    public void getLocations() {
        places = new Places();
        trackerModelArrayList = new ArrayList<>();
        trackerDbHelper = new TrackerDbHelper(this);
        dbManager = new DBManager(trackerDbHelper);
        trackerModelArrayList = dbManager.listAll();
        placesArrayList = places.getPlaces(trackerModelArrayList);
    }

    public void initializeComponents() {
        dateListAdapter = new LocationListAdapter(this, placesArrayList);
        listView = (ListView) findViewById(R.id.listview_by_date_note);
        listView.setAdapter(dateListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            ActivityLauncher.runIntent(this, Home.class);
            finish();
        }
        if (id == R.id.nav_settings) {
            ActivityLauncher.runIntent(this, PreferenceSettings.class);
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        ActivityLauncher.runIntent(this, Home.class);
        finish();
    }
}
