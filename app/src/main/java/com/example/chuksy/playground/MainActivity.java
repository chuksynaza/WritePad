package com.example.chuksy.playground;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    public static String KEY_ENTRY_ID = "entryID";

    protected SQLiteDatabase database;

    protected ArrayList<Entry> entries;

    protected TextView defaultNoEntriesMessage;

    protected EntryAdapter entryAdapter;

    protected String searchQuery = "";

    public static String KEY_NAV_NAME = "navHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Open the database, so that we can read and write
        Database databaseConnection = new Database(this);

        database = databaseConnection.open();


        FloatingActionButton newEntry = findViewById(R.id.newEntry);

        defaultNoEntriesMessage = findViewById(R.id.defaultNoEntriesMessage);

        ListView entriesList = findViewById(R.id.entriesList);

        newEntry.setOnClickListener(new NewEntryClick(MainActivity.this));

        fetchEntries(false);

        entryAdapter = new EntryAdapter(getApplicationContext(), R.layout.entry_row_item, entries);

        entriesList.setAdapter(entryAdapter);

        entriesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        entriesList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener(){

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater = getMenuInflater();

                inflater.inflate(R.menu.selected_entries_menu, menu);

                return true;

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                int actionItemID = item.getItemId();

                //Get Selected Positions

                int[] selectedPositions = entryAdapter.getSelectedPositions();

                switch(actionItemID){

                    case R.id.delete_selected:

                        //Delete Action was clicked

                        //Get Selected Entry IDs for deletion

                        long[] selectedEntryIds = getEntryIDsFromPositions(selectedPositions);

                        //Delete

                        EntryTable.deleteMany(database, selectedEntryIds);

                        //Update the data set

                        fetchEntries(true);

                        break;

                    case R.id.share_selected:

                        //Share Action was clicked

                        //Get Entries for share

                        Entry[] selectedEntries = getEntriesFromPositions(selectedPositions);

                        //Share

                        new ShareEntries(selectedEntries, getApplicationContext());

                        break;

                }
                
                mode.finish();

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

                entryAdapter.clearSelection();

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                Entry selectedEntry = entries.get(position);

                Log.d("PLAY", "Entry Item: " + selectedEntry.getID() +  " with Title: " + selectedEntry.getTitle() + (checked ? " checked" : " unchecked"));

                if(checked) {

                    entryAdapter.addSelection(position);

                } else {

                    entryAdapter.removeSelection(position);

                }

                mode.setTitle(entryAdapter.countSelection() + " selected");

            }
        });

        entriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Entry clickedEntry = entries.get(i);

                Log.d("PLAY", "Entry Item: " + clickedEntry.getID() + " clicked");

                Intent newEntryIntent = new Intent(getApplicationContext(), NewEntry.class);

                newEntryIntent.putExtra(MainActivity.KEY_ENTRY_ID, clickedEntry.getID());

                startActivity(newEntryIntent);

            }

        });

        //Navigation

        new NavigationDrawer(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    long[] getEntryIDsFromPositions(int[] positions){

        //Retrieve the IDs from the positions, this can be used after getting the selected positions by using the method getSelectedPositions in the Adapter

        int positionSize = positions.length;

        long[] entryIDs = new long[positionSize];

        for(int i = 0; i < positionSize; i++){

            entryIDs[i] = entries.get(positions[i]).getID();

        }

        return entryIDs;

    }

    Entry[] getEntriesFromPositions(int[] positions){

        //Retrieve the Entries from the positions, this can be used after getting the selected positions by using the method getSelectedPositions in the Adapter

        int positionSize = positions.length;

        Entry[] theEntries = new Entry[positionSize];

        for(int i = 0; i < positionSize; i++){

            theEntries[i] = entries.get(positions[i]);

        }

        return theEntries;

    }

    void fetchEntries(boolean dataSetChanged){

        if(searchQuery.trim().equals("")){

            entries = EntryTable.getAll(database);

            Log.d("PLAY", "No. of entries retrieved: " + entries.size());

            defaultNoEntriesMessage.setText(R.string.defaultNoEntriesMessage);

        } else {

            Log.d("PLAY", "Searching for: " + searchQuery);

            entries = EntryTable.getAll(database, searchQuery);

            Log.d("PLAY", "No. of entries retrieved: " + entries.size());

            defaultNoEntriesMessage.setText(R.string.searchNoEntriesMessage);

        }

        if(dataSetChanged){

            entryAdapter.clear();

            entryAdapter.addAll(entries);

            entryAdapter.notifyDataSetChanged();

        }

        if(entries.size() == 0){

            defaultNoEntriesMessage.setVisibility(View.VISIBLE);

        } else {

            defaultNoEntriesMessage.setVisibility(View.GONE);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();


        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchQueryTextListener());

        return true;
    }


    class SearchQueryTextListener implements SearchView.OnQueryTextListener{

        @Override
        public boolean onQueryTextSubmit(String s) {

            //Every hit on "Enter"

            searchQuery = s;

            fetchEntries(true);

            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {

            //Any change in the query string

            searchQuery = s;

            fetchEntries(true);

            return true;
        }


    }


}

