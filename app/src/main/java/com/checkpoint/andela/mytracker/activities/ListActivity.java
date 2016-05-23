package com.checkpoint.andela.mytracker.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;
import com.checkpoint.andela.mytracker.slidingTab.slider.SlidingTabLayout;

public class ListActivity extends AppCompatActivity {
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Movement", "Locations"};
    int Numboftabs = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles,
                Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);
    }

    @Override
    public void onBackPressed() {
        ActivityLauncher.runIntent(this, Home.class);
        finish();
    }

}
