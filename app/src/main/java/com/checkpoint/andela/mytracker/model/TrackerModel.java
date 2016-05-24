package com.checkpoint.andela.mytracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.checkpoint.andela.mytracker.helpers.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suadahaji.
 */
public class TrackerModel implements Parcelable{


    private int tracker_id;

    private long duration;

    private String tracker_date;

    private String location;

    private String coordinates;

    public static List<TrackerModel> activities;

    public TrackerModel() {

    }

    public TrackerModel(String location, String coordinates, String tracker_date, long duration) {
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTracker_date() {
        return tracker_date;
    }

    public void setTracker_date(String tracker_date) {
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

    public CharSequence convertDurationToString() {
        long rem = duration / 1000;
        long min = rem / 60;
        long sec = (rem % 1000) % 60;
        long hr = min / 60;
        if (hr < 1 ){
            return ((min <= 1) ? min + " min " : min+ " mins ") + ((sec <= 1) ? sec + "sec" : sec + "secs");
        }

        return ((hr <= 1) ? hr + " hr " : hr + " hrs ") + ((min <= 1) ? min + " min " : min+ " mins ") + ((sec <= 1) ? sec + "sec" : sec + "secs");
    }
    private TrackerModel(Parcel in) {
        location = in.readString();
        tracker_date = in.readString();
        coordinates = in.readString();
        duration = in.readLong();
    }

    public static final Creator<TrackerModel> CREATOR = new Creator<TrackerModel>() {
        @Override
        public TrackerModel createFromParcel(Parcel source) {
            return new TrackerModel(source);
        }

        @Override
        public TrackerModel[] newArray(int size) {
            return new TrackerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(tracker_date);
        dest.writeString(coordinates);
        dest.writeLong(duration);

    }
}
