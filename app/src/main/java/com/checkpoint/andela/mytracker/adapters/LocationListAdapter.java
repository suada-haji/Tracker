package com.checkpoint.andela.mytracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.model.Places;;
import java.util.ArrayList;

/**
 * Created by suadahaji.
 */
public class LocationListAdapter extends ArrayAdapter<Places> {

    public LocationListAdapter(Context context, ArrayList<Places> places) {
        super(context, 0, places);
    }

    static class LayoutHandler {

        TextView tracker_location, tracker_duration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Places model = getItem(position);
        View row = convertView;
        LayoutHandler layoutHandler;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_date_layout, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.tracker_location = (TextView) row.findViewById(R.id.row_date_location);
            layoutHandler.tracker_duration = (TextView) row.findViewById(R.id.row_date_duration);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();
        }

        layoutHandler.tracker_location.setText(model.getLocation());
        layoutHandler.tracker_duration.setText(model.getTimeSpentToString());

        return row;
    }


}
