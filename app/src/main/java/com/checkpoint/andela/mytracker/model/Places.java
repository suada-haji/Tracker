package com.checkpoint.andela.mytracker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suadahaji.
 */
public class Places {
    private String location;
    private double longitude;
    private double latitude;
    private long duration;
    private String dateTime;
    private ArrayList<TrackerModel> models;
    public Places() {
        models = new ArrayList<>();
    }
    public Places(String Location, double longitude, double latitude, long duration, String dateTime) {
        this();
        location = Location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.duration = duration;
        this.dateTime = dateTime;
    }

    public void addTrack(TrackerModel model) {
        models.add(model);
    }

    public ArrayList<TrackerModel> getTracks() {
        return models;
    }

    public ArrayList<TrackerModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<TrackerModel> models) {
        this.models = models;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public CharSequence getTimeSpentToString() {
        long sec = duration / 1000;
        long min = sec / 60;
        long hr = min / 60;
        if (hr < 1){
            return (min <= 1) ? min + " min" : min+ " mins";
        }

        return ((hr <= 1) ? hr + " hr" : hr + " hrs") + ((min <= 1) ? min + " min" : min+ " mins");
    }

    public static Map<String, ArrayList<TrackerModel>> groupByLocation(ArrayList<TrackerModel> list) {
        Map<String, ArrayList<TrackerModel>> trackerGroup = new HashMap<>();
        for (TrackerModel trackerModel: list) {
            String key = trackerModel.getLocation();
            if (!trackerGroup.containsKey(key)) {
                ArrayList<TrackerModel> places = new ArrayList<>();
                places.add(trackerModel);
                trackerGroup.put(key, places);
            }
            else {
                trackerGroup.get(key).add(trackerModel);
            }
        }
        return trackerGroup;
    }

    public ArrayList<Places> getPlaces(ArrayList<TrackerModel> trackerModels)  {
        ArrayList<Places> listOfPlaces = new ArrayList<>();
        Places places;
        Map<String, ArrayList<TrackerModel>> map;
        map = groupByLocation(trackerModels);
        for (String key: map.keySet()) {
            places = new Places();
            places.setLocation(key);
            places.setDuration(totalDuration(map.get(key)));
            places.setModels(map.get(key));
            listOfPlaces.add(places);
        }
        return listOfPlaces;
    }

    public long totalDuration(ArrayList<TrackerModel> trackerModels) {
        long value = 0;
        for (TrackerModel model: trackerModels) {
            value += model.getDuration();
        }
        return value;
    }
}
