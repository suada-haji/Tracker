package com.checkpoint.andela.mytracker.services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.checkpoint.andela.mytracker.helpers.LocationTypeListener;
import com.checkpoint.andela.mytracker.model.LocationModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by suadahaji.
 */
public class LocationGoogleAPIService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Activity activity;
    private double tracker_longitude;
    private double tracker_latitude;
    private LocationTypeListener locationTypeListener;
    private LocationModel locationModel;
    private String tracker_location;

    public LocationGoogleAPIService(Activity activity) {
        this.activity = activity;
        getGoogleApiClient();
        tracker_latitude = 0;
        tracker_longitude = 0;
        locationModel = new LocationModel(activity);
        tracker_location = "";
    }

    private void getGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
    }

    public void setLocationTypeListener(LocationTypeListener locationTypeListener) {
        this.locationTypeListener = locationTypeListener;
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onResult(Status status) {

    }

    private int checkPermission(Object accessLocation) {
        return 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        tracker_latitude = location.getLatitude();
        tracker_longitude = location.getLongitude();
        tracker_location = locationModel.getLocationName(tracker_latitude, tracker_longitude);
        if (!tracker_location.equals(null)) {
            locationTypeListener.onLocationChange(tracker_location);
        }
    }
    public GoogleApiClient getGoogleAPI () {
        return googleApiClient;
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }


    public double getTracker_longitude() {
        return tracker_longitude;
    }

    public void setTracker_longitude(double tracker_longitude) {
        this.tracker_longitude = tracker_longitude;
    }

    public double getTracker_latitude() {
        return tracker_latitude;
    }

    public void setTracker_latitude(double tracker_latitude) {
        this.tracker_latitude = tracker_latitude;
    }
    public boolean isGoogleApiClientConnected() {
        return googleApiClient.isConnected();
    }

    public String getCoordinates() {
        return tracker_latitude + ":" + tracker_longitude;
    }

}
