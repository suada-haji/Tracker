package com.checkpoint.andela.mytracker.slidingTab.locations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.adapters.DateListAdapter;
import com.checkpoint.andela.mytracker.helpers.DBManager;
import com.checkpoint.andela.mytracker.helpers.TrackerDbHelper;
import com.checkpoint.andela.mytracker.model.TrackerModel;

import java.util.ArrayList;

/**
 * Created by suadahaji.
 */
public class Movement extends ListFragment {

    private DateListAdapter dateListAdapter;
    private DBManager dbManager;
    private ListView listView;
    private ArrayList<TrackerModel> trackerModelArrayList;
    private TrackerDbHelper trackerDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movement, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getList();
        initializeComponents();
    }

    public void getList() {
        trackerModelArrayList = new ArrayList<>();
        trackerDbHelper = new TrackerDbHelper(getActivity());
        dbManager = new DBManager(trackerDbHelper);
        trackerModelArrayList = dbManager.listAll();
    }

    public void initializeComponents() {
        dateListAdapter = new DateListAdapter(getActivity(), trackerModelArrayList);
        setListAdapter(dateListAdapter);

    }
}
