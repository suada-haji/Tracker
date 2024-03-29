package com.checkpoint.andela.mytracker.views.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.adapters.ViewPagerAdapter;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;
import com.checkpoint.andela.mytracker.views.slidingtab.SlidingTabLayout;

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Movement", "Locations"};
    int Numboftabs = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.list_activity_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActivityLauncher.runIntent(ListActivity.this, Home.class);
                finish();
            }
        });

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles,
                Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.list_settings:
               ActivityLauncher.runIntent(this, PreferenceSettings.class);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityLauncher.runIntent(ListActivity.this, Home.class);
        finish();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
