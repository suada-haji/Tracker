package com.checkpoint.andela.mytracker.slidingTab.movement;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.adapters.LocationListAdapter;
import com.checkpoint.andela.mytracker.helpers.DBManager;
import com.checkpoint.andela.mytracker.helpers.TrackerDbHelper;
import com.checkpoint.andela.mytracker.model.Places;
import com.checkpoint.andela.mytracker.model.TrackerModel;

import java.util.ArrayList;

/**
 * Created by suadahaji.
 */
public class Locations extends ListFragment {

    private LocationListAdapter dateListAdapter;
    private ArrayList<TrackerModel> trackerModelArrayList;
    private DBManager dbManager;
    private ListView listView;
    private ArrayList<Places> placesArrayList;
    private TrackerDbHelper trackerDbHelper;
    private Places places;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.locations, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLocations();
        initializeComponents();
    }

    public void getLocations() {
        places = new Places();
        trackerModelArrayList = new ArrayList<>();
        trackerDbHelper = new TrackerDbHelper(getActivity());
        dbManager = new DBManager(trackerDbHelper);
        trackerModelArrayList = dbManager.listAll();
        placesArrayList = places.getPlaces(trackerModelArrayList);
    }

    public void initializeComponents() {
        dateListAdapter = new LocationListAdapter(getActivity(), placesArrayList);
        setListAdapter(dateListAdapter);
    }
}
