package com.checkpoint.andela.mytracker.slidingTab.locations;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.checkpoint.andela.mytracker.adapters.DateListAdapter;
import com.checkpoint.andela.mytracker.helpers.DBManager;
import com.checkpoint.andela.mytracker.helpers.TrackerDbHelper;
import com.checkpoint.andela.mytracker.model.TrackerModel;

import java.util.ArrayList;

/**
 * Created by suadahaji.
 */
public class Movement extends ListFragment implements SearchView.OnQueryTextListener {

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
        getActivity().supportInvalidateOptionsMenu();
        setHasOptionsMenu(true);
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
        final ArrayList<TrackerModel> trackerFilter = filterSearch(trackerModelArrayList, query);
        dateListAdapter =  new DateListAdapter(getActivity(), trackerFilter);
        setListAdapter(dateListAdapter);
        dateListAdapter.notifyDataSetChanged();
        return true;
    }

    private ArrayList<TrackerModel> filterSearch(ArrayList<TrackerModel> dateModelArrayList, String search){
        search = search.toLowerCase();
        final ArrayList<TrackerModel> filteredSearch = new ArrayList<>();
        for (TrackerModel trackModel: dateModelArrayList) {
            final String note_title = trackModel.getLocation().toLowerCase();
            if (note_title.contains(search)) {
                filteredSearch.add(trackModel);
            }
        }
        return filteredSearch;

    }

}
