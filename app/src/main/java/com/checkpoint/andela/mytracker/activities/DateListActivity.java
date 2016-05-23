package com.checkpoint.andela.mytracker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.adapters.DateListAdapter;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;
import com.checkpoint.andela.mytracker.helpers.DBManager;
import com.checkpoint.andela.mytracker.helpers.TrackerDbHelper;
import com.checkpoint.andela.mytracker.model.TrackerModel;

import java.util.ArrayList;

public class DateListActivity extends AppCompatActivity {
    private DateListAdapter dateListAdapter;
    private DBManager dbManager;
    private ListView listView;
    private ArrayList<TrackerModel> trackerModelArrayList;
    private TrackerDbHelper trackerDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getList();
        initializeComponents();

    }

    public void getList() {
        trackerModelArrayList = new ArrayList<>();
        trackerDbHelper = new TrackerDbHelper(this);
        dbManager = new DBManager(trackerDbHelper);
        trackerModelArrayList = dbManager.listAll();
    }

    public void initializeComponents() {
        dateListAdapter = new DateListAdapter(this, trackerModelArrayList);
        listView = (ListView) findViewById(R.id.listview_by_date);
        listView.setAdapter(dateListAdapter);
    }

    @Override
    public void onBackPressed() {
        ActivityLauncher.runIntent(this, Home.class);
        finish();
    }
}
