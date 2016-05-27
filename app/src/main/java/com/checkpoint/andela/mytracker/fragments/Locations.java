package com.checkpoint.andela.mytracker.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class Locations extends ListFragment implements SearchView.OnQueryTextListener {

    private LocationListAdapter locationListAdapter;
    private ArrayList<TrackerModel> trackerModelArrayList;
    private DBManager dbManager;
    private ArrayList<Places> placesArrayList;
    private TrackerDbHelper trackerDbHelper;
    private Places places;
    private int placePosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.locations, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().supportInvalidateOptionsMenu();
        setHasOptionsMenu(true);
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
        getListView().setDivider(getResources().getDrawable(R.drawable.list_divider));
        getListView().setDividerHeight(1);
        locationListAdapter = new LocationListAdapter(getActivity(), placesArrayList);
        setListAdapter(locationListAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        placePosition = position;
        deleteItem();
    }



    private void deleteItem() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Delete");

        alert.setMessage("Do you want to delete this location?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeData(placesArrayList, locationListAdapter, placePosition);
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void removeData(ArrayList<Places> placesArrayList1, LocationListAdapter listAdapter, int position) {
        Places placeModel = placesArrayList1.get(position);
        dbManager.deleteLocationFromDB(placeModel);
        placesArrayList1.remove(position);
        listAdapter.notifyDataSetChanged();
        reload();
    }
    private void reload(){
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.list_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.list_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        onQueryTextChange("");
    }
    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query)
    {
        final ArrayList<Places> trackerFilter = filterSearch(placesArrayList, query);
        locationListAdapter =  new LocationListAdapter(getActivity(), trackerFilter);
        setListAdapter(locationListAdapter);
        locationListAdapter.notifyDataSetChanged();
        return true;
    }

    private ArrayList<Places> filterSearch(ArrayList<Places> locationModelArrayList, String search){
        search = search.toLowerCase();
        final ArrayList<Places> filteredSearch = new ArrayList<>();
        for (Places noteModel: locationModelArrayList) {
            final String note_title = noteModel.getLocation().toLowerCase();
            if (note_title.contains(search)) {
                filteredSearch.add(noteModel);
            }
        }
        return filteredSearch;

    }
}
