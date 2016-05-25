package com.checkpoint.andela.mytracker.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.helpers.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by suadahaji.
 */
public class LocationIntentService extends IntentService {
    private static final String ADDRESS = "GetAddress";
    private Location location;
    String locationAddress = "";
    protected ResultReceiver resultReceiver;

    public LocationIntentService() {
        super(ADDRESS);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMsg = "";

        location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        } catch (IOException e) {
            errorMsg = "Service not Available";
            Log.e(ADDRESS, errorMsg);
        } catch (IllegalArgumentException e) {
            errorMsg = "Invalid latitude or longitude used";
            Log.e(ADDRESS, errorMsg);
        }

        // Handle case where no address was found.
        if (addressList == null || addressList.size()  == 0) {
            if (errorMsg.isEmpty()) {
                errorMsg = getString(R.string.no_address_found);
                Log.e(ADDRESS, errorMsg);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMsg, null);
        } else {
            Address address = addressList.get(0);
            locationAddress = address.getThoroughfare() + ", " + address.getAdminArea();
            Log.e(ADDRESS, address.toString());
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    locationAddress, address);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_ADDRESS, address);
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }
}
