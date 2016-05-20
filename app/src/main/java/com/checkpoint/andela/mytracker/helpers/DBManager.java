package com.checkpoint.andela.mytracker.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.Selection;
import android.util.Log;

import com.checkpoint.andela.mytracker.model.TrackerModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suadahaji.
 */
public class DBManager {
    private TrackerDbHelper trackerDbHelper;
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("EEE, MMMM d, y");
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

    public long insertDataIntoDatabase(TrackerModel trackerModel) {
        trackerDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TABLE_COLUMN_LOCATION, trackerModel.getLocation());
        contentValues.put(Constants.TABLE_COLUMN_DATE, trackerModel.getTracker_date().toString(dateFormat));
        contentValues.put(Constants.TABLE_COLUMN_COORDINATES, trackerModel.getCoordinates());
        contentValues.put(Constants.TABLE_COLUMN_ACTIVITY, trackerModel.getActivityType().toString());
        contentValues.put(Constants.TABLE_COLUMN_DURATION, trackerModel.getDuration());
        Log.e("Suada", "Location: " + trackerModel.getLocation() + " \n Date: " + trackerModel.getTracker_date().toString(dateFormat) + "\n  Latitude: " + trackerModel.getCoordinates() + "\n TrackerModel " + trackerModel.getActivityType().toString() + " Duration:  " + trackerModel.getDuration());


        return trackerDbHelper.getDB().insert(Constants.TABLE_NAME, null, contentValues);
        }

    private TrackerModel getFromCursor(Cursor cursor) {
        TrackerModel trackerModel = new TrackerModel();

        trackerModel.setTracker_id((int) cursor.getLong(0));
        trackerModel.setLocation(cursor.getString(Constants.TABLE_COLUMN_LOCATION_INDEX));
        trackerModel.setCoordinates(cursor.getString(Constants.TABLE_COORDINATES_INDEX));
        trackerModel.setDuration(Long.parseLong(cursor.getString(Constants.TABLE_COLUMN_DURATION_INDEX)));
        trackerModel.setActivityType(TrackerModel.TypeOfActivity.valueOf(cursor.getString(Constants.TABLE_ACTIVITY_INDEX)));
        trackerModel.setTracker_date(DateTime.parse(cursor.getString(Constants.TABLE_COLUMN_DATE_INDEX), dateFormat));
        return trackerModel;
    }

    public int updateDataInDatabase(TrackerModel trackerModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TABLE_COLUMN_LOCATION, trackerModel.getLocation());
        contentValues.put(Constants.TABLE_COLUMN_DATE, trackerModel.getTracker_date().toString(dateFormat));
        contentValues.put(Constants.TABLE_COLUMN_COORDINATES, trackerModel.getCoordinates());
        contentValues.put(Constants.TABLE_COLUMN_ACTIVITY, trackerModel.getActivityType().toString());
        contentValues.put(Constants.TABLE_COLUMN_DURATION, trackerModel.getDuration());

        String condition = Constants.TABLE_COLUMN_ID + " = ?";
        String[] arguments = { String.valueOf(trackerModel.getTracker_id())};

        return trackerDbHelper.getDB().update(Constants.TABLE_NAME, contentValues, condition, arguments);
    }

    public List<TrackerModel> queryDatabase (String data, String[] dataArguments, String group, String sort) {


        Cursor cursor = trackerDbHelper.getDB().query(Constants.TABLE_NAME, columns, data, dataArguments, group, null, sort);

        List<TrackerModel> activities = new ArrayList<>();
        while (cursor.moveToNext()) {
            activities.add(getFromCursor(cursor));
        }
        cursor.close();

        return activities;
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

    public long saveToDB(TrackerModel trackerModel) {
        return insertDataIntoDatabase(trackerModel);
    }

    public List<TrackerModel> getById(DateTime dateTime, Selection selection) {
        String dataSelection = Constants.TABLE_COLUMN_DATE + " = ?";
        String[] dataArguments = {dateTime.toString(dateFormat)};
        String order = Constants.TABLE_COLUMN_ID;
        return queryDatabase(dataSelection, dataArguments, null, order);
    }

    public List<TrackerModel> getByLocation(String location, Selection selection) {
        String dataSelection = Constants.TABLE_COLUMN_LOCATION + " = ?";
        String[] dataArguments = {location};
        String order = Constants.TABLE_COLUMN_ID;
        return queryDatabase(dataSelection, dataArguments, null, order);
    }

    public List<TrackerModel> getByCoordinates(String coordinates, Selection selection) {
        String dataSelection = Constants.TABLE_COLUMN_COORDINATES + " = ?";
        String[] dataArguments = {coordinates};
        String order = Constants.TABLE_COLUMN_ID;
        return queryDatabase(dataSelection, dataArguments, null, order);
    }

    public List<TrackerModel> getByActivityType(TrackerModel.TypeOfActivity activity, Selection selection) {
        String dataSelection = Constants.TABLE_COLUMN_ACTIVITY + " = ?";
        String[] dataArguments = {activity.toString()};
        String order = Constants.TABLE_COLUMN_ID;
        return queryDatabase(dataSelection, dataArguments, null, order);
    }

    public List<TrackerModel> getAll() {
        return queryDatabase(null, null, null, null);
    }



}
