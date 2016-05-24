package com.checkpoint.andela.mytracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.model.TrackerModel;

import java.util.ArrayList;

/**
 * Created by suadahaji.
 */
public class DateListAdapter extends ArrayAdapter<TrackerModel> {

    public DateListAdapter(Context context, ArrayList<TrackerModel> models) {
        super(context, 0, models);
    }

    static class LayoutHandler {

        TextView tracker_activity,tracker_location, tracker_duration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrackerModel model = getItem(position);
        View row = convertView;
        LayoutHandler layoutHandler;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.cardview_row_date, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.tracker_activity = (TextView) row.findViewById(R.id.row_date_date);
            layoutHandler.tracker_location = (TextView) row.findViewById(R.id.row_date_location);
            layoutHandler.tracker_duration = (TextView) row.findViewById(R.id.row_date_duration);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();
        }

        layoutHandler.tracker_location.setText(model.getLocation());
        layoutHandler.tracker_duration.setText(model.convertDurationToString());
        layoutHandler.tracker_activity.setText(model.getTracker_date());
        return row;
    }
}
