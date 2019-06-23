package com.example.chuksy.playground;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

/**
 * Created by chuksy on 5/27/19.
 */

public class LocationProvider implements View.OnClickListener {

    private Activity theActivity;

    private Location theCurrentLocation;

    private String city, state, country;

    private LocationManager locationManager;

    private LocationListener locationListener;

    public static int LOCATION_PERMISSION_REQUEST_CODE = 11;

    public static interface LocationListenerInterface {

        void retrieveLocation(String[] address, Location location);

    }

    public LocationProvider(Activity activity){

        theActivity = activity;

    }

    boolean getLocation(){

        locationManager = (LocationManager) theActivity.getSystemService(theActivity.LOCATION_SERVICE);

        // Define a listener that responds to location updates

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);

                theCurrentLocation = location;

                updateAddress();


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };



        String locationProvider = LocationManager.GPS_PROVIDER;
        // Or, use GPS location data:
        // String locationProvider = LocationManager.GPS_PROVIDER;



        try {

            Log.d("PLAY", "Trying to get the location");

            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

            theCurrentLocation = lastKnownLocation;

            locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

            updateAddress();

            return true;



        } catch(SecurityException e){

            Log.d("PLAY", "There was a security exception trying to get the location");

            Log.d("PLAY", e.getMessage());

            Toast.makeText(theActivity, "No permissions for location", Toast.LENGTH_LONG).show();

            return false;

        }

    }

    void updateAddress(){

        Geocoder geocoder = new Geocoder(theActivity, Locale.getDefault());

        try{

            List<Address> addresses = geocoder.getFromLocation(theCurrentLocation.getLatitude(), theCurrentLocation.getLongitude(), 1);

            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);

            city = cityName;

            state = stateName;

            country = countryName;

            LocationListenerInterface theRetrievableActivity = (LocationListenerInterface) theActivity;

            theRetrievableActivity.retrieveLocation(getAddress(), theCurrentLocation);

        } catch(IOException e){


        }

    }

    public String[] getAddress(){

        return new String[] {city, state, country};

    }

    public void stopListeningForUpdates(){

        locationManager.removeUpdates(locationListener);

    }

    @Override
    public void onClick(View v) {

        Log.d("PLAY", "Location Clicked");

        if (ContextCompat.checkSelfPermission(theActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ) {

            getLocation();



        } else {

            ActivityCompat.requestPermissions(theActivity, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    this.LOCATION_PERMISSION_REQUEST_CODE);

            //Toast.makeText(theActivity, "No permissions for location", Toast.LENGTH_LONG).show();
//
        }




    }

}
