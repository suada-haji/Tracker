package com.checkpoint.andela.mytracker.model;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;

/**
 * Created by suadahaji.
 */
public class LocationModel {
    private Activity activity;
    private String location;

    public LocationModel(Activity activity) {
        this.activity = activity;
        location = "";
    }

    public List<Address> getAddress(double latitude, double longitude) throws Exception {

        Geocoder geocoder = new Geocoder(activity);
        return geocoder.getFromLocation(latitude, longitude, 1);
    }

    public String getLocationName (double latitude, double longitude) {
        List<Address> addresses = null;

        try {
            addresses = getAddress(latitude, longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            location = address.getThoroughfare() + ", " + address.getAdminArea();
        }

        return location;
    }
}
