package com.checkpoint.andela.mytracker.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;

public class PreferenceSettings extends PreferenceActivity {
    private AppCompatDelegate compatDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getCompatDelegate().installViewFactory();
        getCompatDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        ActionBar bar = getCurrentActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        addPreferencesFromResource(R.xml.preference);
    }

    public ActionBar getCurrentActionBar() {
        return getCompatDelegate().getSupportActionBar();
    }

    public void setCurrentActionBar(Toolbar toolbar) {
        getCompatDelegate().setSupportActionBar(toolbar);
    }
    public void setCompatDelegate(AppCompatDelegate compatDelegate) {
        this.compatDelegate = compatDelegate;
    }

    private AppCompatDelegate getCompatDelegate() {
        if (compatDelegate == null) {
            compatDelegate = AppCompatDelegate.create(this, null);
        }
        return compatDelegate;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == android.R.id.home && !super.onMenuItemSelected(featureId, item)){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
       return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getCompatDelegate().onPostCreate(savedInstanceState);
        LinearLayout parent = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbr = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, parent, false);
        parent.addView(toolbr, 0);
        toolbr.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLauncher.runIntent(PreferenceSettings.this, Home.class);
                finish();
            }
        });
    }

    @Override
    public MenuInflater getMenuInflater() {
        return getCompatDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getCompatDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getCompatDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getCompatDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getCompatDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getCompatDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getCompatDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getCompatDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getCompatDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCompatDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getCompatDelegate().invalidateOptionsMenu();
    }


    @Override
    public void onBackPressed() {
        ActivityLauncher.runIntent(this, Home.class);
        finish();
    }
}
