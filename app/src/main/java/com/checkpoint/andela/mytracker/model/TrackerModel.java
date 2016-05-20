package com.checkpoint.andela.mytracker.model;

import com.checkpoint.andela.mytracker.helpers.Constants;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suadahaji.
 */
public class TrackerModel {
    public enum TypeOfActivity{
        STILL,
        IN_VEHICLE,
        ON_FOOT,
        ON_BICYCLE,
        WALKING,
        RUNNING,
        TILTING,
        UNKNOWN
    }

    private int tracker_id;

    private TypeOfActivity activityType;

    private long duration;

    private DateTime tracker_date;

    private String location;

    private String coordinates;

    public static List<TrackerModel> activities;

    public TrackerModel() {

    }

    public TrackerModel(String location, String coordinates, DateTime tracker_date, long duration) {
        this.location = location;
        this.coordinates = coordinates;
        this.tracker_date = tracker_date;
        this.duration = duration;
        activities = new ArrayList<>();
    }

    public int getTracker_id() {
        return tracker_id;
    }

    public void setTracker_id(int tracker_id) {
        this.tracker_id = tracker_id;
    }

    public TypeOfActivity getActivityType() {
        return activityType;
    }

    public void setActivityType(TypeOfActivity activityType) {
        this.activityType = activityType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public DateTime getTracker_date() {
        return tracker_date;
    }

    public void setTracker_date(DateTime tracker_date) {
        this.tracker_date = tracker_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String movementTypeToString() {
        switch (getActivityType()){
            case STILL:
                return Constants.STILL;
            case IN_VEHICLE:
                return Constants.IN_VEHICLE;
            case WALKING:
                return Constants.WALKING;
            case ON_FOOT:
                return Constants.ON_FOOT;
            case RUNNING:
                return Constants.RUNNING;
            case TILTING:
                return Constants.TILTING;
            case UNKNOWN:
                return Constants.UKNOWN;
            case ON_BICYCLE:
                return Constants.ON_BICYCLE;

            default:
                return "";

        }
    }
    public CharSequence convertDurationToString() {
        long sec = duration / 1000;
        long min = sec / 60;
        long hr = min / 60;
        if (hr < 1){
            return (min <= 1) ? min + " min" : min+ " mins";
        }

        return ((hr <= 1) ? hr + " hr" : hr + " hrs") + ((min <= 1) ? min + " min" : min+ " mins");
    }

    public  TypeOfActivity stringToActivity(String activity) {
        switch (activity){
            case Constants.STILL:
                return TypeOfActivity.STILL;
            case Constants.IN_VEHICLE:
                return TypeOfActivity.IN_VEHICLE;
            case Constants.WALKING:
                return TypeOfActivity.WALKING;
            case Constants.ON_FOOT:
                return TypeOfActivity.ON_FOOT;
            case Constants.RUNNING:
                return TypeOfActivity.RUNNING;
            case Constants.TILTING:
                return TypeOfActivity.TILTING;
            case Constants.UKNOWN:
                return TypeOfActivity.UNKNOWN;
            default:
                return null;
        }
    }

}
