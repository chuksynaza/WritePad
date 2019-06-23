package com.example.chuksy.playground;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by chuksy on 5/27/19.
 */

public class NavigationDrawer implements NavigationView.OnNavigationItemSelectedListener {


    private Activity theActivity;

    public NavigationDrawer(Activity theActivity){

        this.theActivity = theActivity;

        Toolbar toolbar = (Toolbar) this.theActivity.findViewById(R.id.toolbar);

        ((AppCompatActivity)this.theActivity).setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) this.theActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this.theActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) this.theActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d("PLAY", "Navigation Item Clicked with ID: " + id);

        Intent newEntryIntent;

        switch (id){

            case R.id.navHome:


                newEntryIntent = new Intent(theActivity, MainActivity.class);

                theActivity.startActivity(newEntryIntent);

                break;

            case R.id.navStats:

                newEntryIntent = new Intent(theActivity, StatsTracking.class);

                theActivity.startActivity(newEntryIntent);

                break;

            case R.id.navCreateEntry:

                newEntryIntent = new Intent(theActivity, NewEntry.class);

                newEntryIntent.putExtra(MainActivity.KEY_ENTRY_ID, (long) 0);

                theActivity.startActivity(newEntryIntent);


                break;

        }
        DrawerLayout drawer = (DrawerLayout) theActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
