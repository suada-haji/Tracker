package com.checkpoint.andela.mytracker.helpers;

import android.content.ContentValues;

import com.checkpoint.andela.mytracker.model.TrackerModel;
import com.checkpoint.andela.mytracker.slidingTab.locations.Movement;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by suadahaji.
 */
public class TestHelper {
    public static ContentValues createMovementValues() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        TrackerModel trackerModel = new TrackerModel();
        trackerModel.setTracker_date(simpleDateFormat.format(date));
        trackerModel.setLocation("Kindaruma Rd, Nairobi ");
        trackerModel.setDuration(230313);
        trackerModel.setCoordinates("-1.2974303:36.7889626");
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TABLE_COLUMN_LOCATION, trackerModel.getLocation());
        contentValues.put(Constants.TABLE_COLUMN_DATE, trackerModel.getTracker_date());
        contentValues.put(Constants.TABLE_COLUMN_COORDINATES, trackerModel.getCoordinates());
        contentValues.put(Constants.TABLE_COLUMN_DURATION, trackerModel.getDuration());
        return contentValues;
    }

}