package com.checkpoint.andela.mytracker.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.checkpoint.andela.mytracker.R;
import com.checkpoint.andela.mytracker.activities.LocationDetail;
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
    private ArrayList<TrackerModel> trackerModelArrayList;
    private TrackerDbHelper trackerDbHelper;
    private int trackerPosition;
    private ArrayList<TrackerModel> databaseData;

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
        databaseData = dbManager.listAll();
        getListView().setDivider(getResources().getDrawable(R.drawable.list_divider));
        getListView().setDividerHeight(1);
        dateListAdapter = new DateListAdapter(getActivity(), trackerModelArrayList);
        setListAdapter(dateListAdapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                trackerPosition = position;
                deleteTrackDialogue();
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TrackerModel model = dateListAdapter.getItem(position);
        Intent intent = new Intent(getContext(), LocationDetail.class);
        intent.putExtra("currentItem", model);
        intent.putParcelableArrayListExtra("listData", databaseData);
        startActivityForResult(intent, 10);
    }


    private void deleteTrackDialogue() {
            AlertDialog.Builder deleteNote = new AlertDialog.Builder(getActivity());
            deleteNote.setMessage("Are you sure you want to delete this information?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeData(trackerModelArrayList, dateListAdapter, trackerPosition);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        deleteNote.show();
    }

    public void removeData(ArrayList<TrackerModel> trackerModels, DateListAdapter listAdapter, int position) {
        TrackerModel trackerModel = trackerModels.get(position);
        dbManager.deleteRecordfromDB(trackerModel);
        trackerModels.remove(position);
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
        final ArrayList<TrackerModel> trackerFilter = filterSearch(trackerModelArrayList, query);
        dateListAdapter = new DateListAdapter(getActivity(), trackerFilter);
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
