package com.checkpoint.andela.mytracker.activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;
import com.checkpoint.andela.mytracker.helpers.Constants;
import com.checkpoint.andela.mytracker.helpers.SharedPreferenceSettings;
import com.checkpoint.andela.mytracker.helpers.Timer;


public class Settings extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private TextView saveText;
    private Button buttonSave;
    private Timer watch;
    private Dialog dialog;
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor editor;
    private SharedPreferenceSettings sharedPreferenceSettings;
    private int time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLauncher.runIntent(Settings.this, Home.class);
                finish();
            }
        });

        watch = new Timer();

        preferenceSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferenceSettings.edit();
        saveText = (TextView) findViewById(R.id.save_time);
        sharedPreferenceSettings = new SharedPreferenceSettings(this);
        saveText.setText(sharedPreferenceSettings.getTime());
        time = watch.formatDelayText(sharedPreferenceSettings.getTime());
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        saveText.setText(newVal + Constants.MINUTES);
        editor.putString(Constants.DEFAULT_DELAY_TIME, saveText.getText().toString());
        editor.commit();
        sharedPreferenceSettings.saveTime(saveText.getText().toString());
    }

    public void revealNumberPickerDialog(View view) {
        dialog = new Dialog(this);
        dialog.setTitle(Constants.DIALOG_TITLE);
        dialog.setContentView(R.layout.time_picker);
        buttonSave = (Button) dialog.findViewById(R.id.time_save_button);
        final NumberPicker timePicker = (NumberPicker) dialog.findViewById(R.id.time_picker);
        timePicker.setMaxValue(60);
        timePicker.setMinValue(1);
        timePicker.setValue(time);
        dialog.show();
        timePicker.setOnValueChangedListener(this);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityLauncher.runIntent(this, Home.class);
        finish();
    }
}
