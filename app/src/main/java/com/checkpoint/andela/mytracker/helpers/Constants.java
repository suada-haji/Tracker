package com.checkpoint.andela.mytracker.helpers;

/**
 * Created by suadahaji.
 */
public class Constants {

    public static final String PACKAGE_NAME = "com.checkpoint.andela.mytracker";

    public static final String STRING_ACTION = PACKAGE_NAME + ".STRING_ACTION";

    public static final String STRING_EXTRA = PACKAGE_NAME + ".STRING_EXTRA";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final String TIME_KEY = "TIME_KEY";
    public static final String DEFAULT_DELAY_TIME = "5 minutes";
    public static final String MINUTES = " min";
    public static final String DIALOG_TITLE = "Tracking Time";
    public static final String DURATION_KEY = "delay_key";

    public static final String NOTIFIER = "Notifier";
    public static final int NOTIFICATION_ID = 1;
    public static final String NOTIFICATION_TITLE = "Tracking in Progress";

    public static final int MILLISECONDS = 1000;
    public static final int MINUTES_TO_MILLISECONDS = 6000;
    public static final int SECONDS_TO_MINUTES = 60;
    public static final int SECONDS_TO_HOUR = 3600;
    public static final long INTERVALS = 0;

    public static final String DATABASE_NAME ="tracker.db";
    public static final String TABLE_NAME = "tracker_table";
    public static final String TABLE_COLUMN_ID = "_id";
    public static final String TABLE_COLUMN_LOCATION = "address";
    public static final int TABLE_COLUMN_LOCATION_INDEX = 1;
    public static final String TABLE_COLUMN_COORDINATES = "TABLE_COLUMN_COORDINATES";
    public static final int TABLE_COORDINATES_INDEX = 2;
    public static final String TABLE_COLUMN_ACTIVITY = "TABLE_COLUMN_ACTIVITY";
    public static final String TABLE_COLUMN_DURATION = "duration";
    public static final int TABLE_COLUMN_DURATION_INDEX = 3;
    public static final int TABLE_ACTIVITY_INDEX = 4;
    public static final String TABLE_COLUMN_DATE = "dateCreated";
    public static final int TABLE_COLUMN_DATE_INDEX = 5;
    public static final String TABLE_COLUMN_LONGITUDE = "longitude";
    public static final String TABLE_COLUMN_LATITUDE = "latitude";

    public final static String ELAPSED_TIME = "elapsed_time";
    public final static String START_ACTIVITY = "start_activity";
    public final static String _ACTIVITY = "user_activity";
    public final static String USER_MOVEMENT = "user_location";


    public static final int DATABASE_VERSION = 1;


    public static final String MOST_PROBABLE_ACTIVITY = PACKAGE_NAME + ".most probable Activity";
    public static final String ACTIVITY = PACKAGE_NAME + ".TrackerModel";
    public static final String STILL = "Standing Still";
    public static final String IN_VEHICLE = "In a Vehicle";
    public static final String WALKING = "Walking";
    public static final String ON_FOOT = "On foot";
    public static final String RUNNING = "Running";
    public static final String TILTING = "Tilting";
    public static final String UKNOWN = "Unknown TrackerModel";
    public static final String ON_BICYCLE = "On a bycycle";
    public static final String UNIDENTIFIABLE_ACTIVITIES = "Unidentifiable activities";

}
