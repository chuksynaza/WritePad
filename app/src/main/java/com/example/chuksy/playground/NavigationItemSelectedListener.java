package com.example.chuksy.playground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by chuksy on 5/27/19.
 */

public class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    public Activity theActivity;

    public NavigationItemSelectedListener(Activity theActivity){

        this.theActivity = theActivity;

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

                break;

            case R.id.navCreateEntry:

                newEntryIntent = new Intent(theActivity, NewEntry.class);

                newEntryIntent.putExtra(MainActivity.KEY_ENTRY_ID, 0);

                theActivity.startActivity(newEntryIntent);


                break;

        }
        DrawerLayout drawer = (DrawerLayout) theActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
