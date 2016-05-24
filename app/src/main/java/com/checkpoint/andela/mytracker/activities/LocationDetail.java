package com.checkpoint.andela.mytracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.model.TrackerModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationDetail extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String coordinates;
    private String location;
    private String dateTracked;
    private TrackerModel model;
    private TextView locationTxt;
    private TextView durationTxt;
    private TextView dateTxt;
    private TextView coordinatesTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.locationDetail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationDetail.this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit", true);
                startActivity(intent);
                finish();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        model = getIntent().getParcelableExtra("currentItem");
        coordinates = model.getCoordinates();
        location = model.getLocation();
        dateTracked = model.getTracker_date();
        locationTxt = (TextView) findViewById(R.id.detail_location_info);
        durationTxt = (TextView) findViewById(R.id.detail_duration_info);
        dateTxt = (TextView) findViewById(R.id.detail_date_info);
        coordinatesTxt = (TextView) findViewById(R.id.detail_coordinates_info);
        locationTxt.setText(location);
        durationTxt.setText(model.convertDurationToString());
        dateTxt.setText(dateTracked);
        coordinatesTxt.setText(coordinates);
    }

   @ Override
    public void onMapReady(GoogleMap googleMap) {
       mMap = googleMap;
       String[] str = coordinates.split(":");
       Double[] doubles = new Double[str.length];
       for (int i = 0; i < str.length; i++)
       {
           doubles[i] = Double.parseDouble(str[i]);

       }
       LatLng coordinate = new LatLng(doubles[0], doubles[1]);
       mMap.addMarker(new MarkerOptions().position(coordinate).title(location)).setVisible(true);
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15.0f));
   }
}
