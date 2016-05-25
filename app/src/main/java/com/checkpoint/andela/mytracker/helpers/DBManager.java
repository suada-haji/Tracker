package com.checkpoint.andela.mytracker.helpers;

import android.content.ContentValues;
import android.database.Cursor;

import com.checkpoint.andela.mytracker.model.Places;
import com.checkpoint.andela.mytracker.model.TrackerModel;


import java.util.ArrayList;

/**
 * Created by suadahaji.
 */
public class DBManager {
    private TrackerDbHelper trackerDbHelper;
    private Long duration;
    private String[] columns = {
            Constants.TABLE_COLUMN_ID,
            Constants.TABLE_COLUMN_LOCATION,
            Constants.TABLE_COLUMN_COORDINATES,
            Constants.TABLE_COLUMN_DURATION,
            Constants.TABLE_COLUMN_ACTIVITY,
            Constants.TABLE_COLUMN_DATE
    };

    public  DBManager(TrackerDbHelper trackerDbHelper) {
        this.trackerDbHelper = trackerDbHelper;
    }

    public void insertDataIntoDatabase(TrackerModel trackerModel) {
        if (!hasLocationInDB(trackerModel)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.TABLE_COLUMN_LOCATION, trackerModel.getLocation());
            contentValues.put(Constants.TABLE_COLUMN_DATE, trackerModel.getTracker_date());
            contentValues.put(Constants.TABLE_COLUMN_COORDINATES, trackerModel.getCoordinates());
            contentValues.put(Constants.TABLE_COLUMN_DURATION, trackerModel.getDuration());
            trackerDbHelper.getDB().insert(Constants.TABLE_NAME, null, contentValues);
        } else {
            updateDataInDatabase(trackerModel.getLocation(), trackerModel.getDuration(),trackerModel.getTracker_date());
        }
    }

    public void deleteRecordfromDB(TrackerModel trackerModel) {
        String dbQuery = "DELETE FROM " + Constants.TABLE_NAME + " WHERE " +
                Constants.TABLE_COLUMN_LOCATION + " = '" + trackerModel.getLocation() + "' AND " +
                Constants.TABLE_COLUMN_COORDINATES + " = '" + trackerModel.getCoordinates() + "' AND " +
                Constants.TABLE_COLUMN_DATE + " = '" + trackerModel.getTracker_date() + "' AND " +
                Constants.TABLE_COLUMN_DURATION + " = '" + trackerModel.getDuration() + "'";
        trackerDbHelper.getDB().execSQL(dbQuery);
    }

    public void deleteLocationFromDB(Places trackerModel) {
        String dbQuery = "DELETE FROM " + Constants.TABLE_NAME + " WHERE " +
                Constants.TABLE_COLUMN_LOCATION + " = '" + trackerModel.getLocation() + "'";
        trackerDbHelper.getDB().execSQL(dbQuery);
    }

    private boolean hasLocationInDB(TrackerModel trackerModel) {
        String dbQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " +
                Constants.TABLE_COLUMN_LOCATION + " = '" + trackerModel.getLocation() + "' AND " +
                Constants.TABLE_COLUMN_DATE + " = '" + trackerModel.getTracker_date() + "'";
        Cursor cursor = trackerDbHelper.getDB().rawQuery(dbQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            duration = cursor.getLong(cursor.getColumnIndex(Constants.TABLE_COLUMN_DURATION));
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private void updateDataInDatabase(String location, Long tracker_duration, String date) {
        long totalDuration = tracker_duration + duration;
        String dbQuery = "UPDATE " + Constants.TABLE_NAME + " SET " + Constants.TABLE_COLUMN_DURATION + " = " + totalDuration +
                " WHERE " +Constants.TABLE_COLUMN_LOCATION + " = '" + location + "' AND " +
                Constants.TABLE_COLUMN_DATE + " = '" + date + "'";
        trackerDbHelper.getDB().execSQL(dbQuery);
    }

    private TrackerModel getFromCursor(Cursor cursor) {
        TrackerModel trackerModel = new TrackerModel();

        trackerModel.setTracker_id((int) cursor.getLong(0));
        trackerModel.setLocation(cursor.getString(Constants.TABLE_COLUMN_LOCATION_INDEX));
        trackerModel.setCoordinates(cursor.getString(Constants.TABLE_COORDINATES_INDEX));
        trackerModel.setDuration(Long.parseLong(cursor.getString(Constants.TABLE_COLUMN_DURATION_INDEX)));
        trackerModel.setTracker_date(cursor.getString(Constants.TABLE_COLUMN_DATE_INDEX));
        return trackerModel;
    }

    private TrackerModel getFromDB(String data, String[] dataArguments, String sort) {
        Cursor cursor = trackerDbHelper.getDB().query(Constants.TABLE_NAME, columns, data, dataArguments, null, null, sort);

        if (!cursor.moveToNext()) {
            return null;
        }
        TrackerModel trackerModel = getFromCursor(cursor);
        cursor.close();
        return trackerModel;
    }

    public void saveToDB(TrackerModel trackerModel) {
         insertDataIntoDatabase(trackerModel);
    }

    public ArrayList<TrackerModel> queryDatabase (String data, String[] dataArguments, String group, String sort) {


        Cursor cursor = trackerDbHelper.getDB().query(Constants.TABLE_NAME, columns, data, dataArguments, group, null, sort);

        ArrayList<TrackerModel> activities = new ArrayList<>();
        while (cursor.moveToNext()) {
            activities.add(getFromCursor(cursor));
        }
        cursor.close();

        return activities;
    }

    public TrackerModel listByID(int id) {
        String dataSelection = Constants.TABLE_COLUMN_ID + " = ?";
        String[] arguments = { String.valueOf(id)};
        String orderBy = Constants.TABLE_COLUMN_ID;
        return getFromDB(dataSelection, arguments, orderBy);
    }

    public ArrayList<TrackerModel> listAll() {
        String order = Constants.TABLE_COLUMN_DATE;
        return queryDatabase(null, null, null, order);
    }
}
