package com.checkpoint.andela.mytracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by suadahaji.
 */
public class TrackerModel implements Parcelable {

    private Long _id;
    private String tracker_date;
    private String tracker_location;
    private String tracker_activity;
    private int tracker_duration;

    public TrackerModel() {
        this.tracker_date = setTracker_date();
    }
    public Long get_id() {
        return _id;
    }

    public String getTracker_date() {
        return this.tracker_date;
    }

    public String setTracker_date() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        long time = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(time);
        return dateFormat.format(timestamp);
    }

    public String getTracker_location() {
        return tracker_location;
    }

    public void setTracker_location(String tracker_location) {
        this.tracker_location = tracker_location;
    }

    public String getTracker_activity() {
        return tracker_activity;
    }

    public void setTracker_activity(String tracker_activity) {
        this.tracker_activity = tracker_activity;
    }

    public int getTracker_duration() {
        return tracker_duration;
    }

    public void setTracker_duration(int tracker_duration) {
        this.tracker_duration = tracker_duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeString(this.tracker_date);
        dest.writeString(this.tracker_location);
        dest.writeString(this.tracker_activity);
        dest.writeInt(this.tracker_duration);
    }

    public TrackerModel(Parcel in) {
        this._id = in.readLong();
        this.tracker_date = in.readString();
        this.tracker_location = in.readString();
        this.tracker_activity = in.readString();
        this.tracker_duration = in.readInt();
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
}
