package com.checkpoint.andela.mytracker.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.checkpoint.andela.mytracker.R;

public class ListByDate extends AppCompatActivity {

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.list_by_date_toolbar);
        setSupportActionBar(toolbar);
    }

    public void initializeComponents() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    }

}
