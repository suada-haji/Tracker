package com.checkpoint.andela.mytracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.checkpoint.andela.mytracker.R;

/**
 * Created by suadahaji.
 */
public class PreferenceActivitySettings extends DialogPreference implements Preference.OnPreferenceChangeListener{

    public static final String _DEFAULT_VALUE = "0:5";
    private TimePicker picker;
    private DurationTime durationTime;

    public PreferenceActivitySettings(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setDialogLayoutResource(R.layout.time_picker);

        setOnPreferenceChangeListener(this);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        int hour = picker.getCurrentHour();
        int minute = picker.getCurrentMinute();
        if (positiveResult) {
            DurationTime duration = new DurationTime(hour, minute);
            String value = duration.toString();

            if (callChangeListener(value)) {
                durationTime = duration;
                persistString(value);
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue) {
            durationTime = getDurationTime(getPersistedString(_DEFAULT_VALUE));
        } else {
            durationTime = getDurationTime((String) defaultValue);
            persistString(durationTime.toString());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected View onCreateDialogView() {
        View view = super.onCreateDialogView();
        picker = (TimePicker) view.findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        return view;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setCurrentHour(durationTime.getHr());
        picker.setCurrentMinute(durationTime.getMin());
    }

    public DurationTime getDurationTime(String duration) {
        String[] arguments = duration.split(":");
        DurationTime time = null;
        if (arguments.length < 2) {
            return new DurationTime(0, 5);
        }
        try {
            return new DurationTime(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DurationTime(0, 5);
    }

    public void getSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setSummary(getDurationTime(newValue.toString()).timeToString());
        getSettings();
        return true;
    }
}
