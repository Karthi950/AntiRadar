package com.example.karthi.antiradar.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.os.Vibrator;

import com.example.karthi.antiradar.MapsActivity;
import com.example.karthi.antiradar.model.Radar;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Arnaud on 22/06/2016.
 */
public class GPSLocator extends Service implements LocationListener {

    public static LatLng currentLocation = new LatLng(0, 0);

    Intent intent = new Intent();

   /* @Override
    public void onStart(Intent intent, int startId) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 50, this);
        }

    }*/

    public void onLocationChanged(final Location newLocation) {
        //Toast.makeText(getApplicationContext(), loc.toString(), Toast.LENGTH_LONG).show();
        Log.i("PREVIOUS LOC", "CHANGEMENT DE LOC " + currentLocation.toString());
        Log.i("LOC CHANGED", "CHANGEMENT DE LOC " + newLocation.toString());
        System.out.println("CHANGEMENT LOCATION");
        float[] distanceInMeters = new float[1];
        Location.distanceBetween(newLocation.getLatitude(), newLocation.getLongitude(), currentLocation.latitude, currentLocation.longitude, distanceInMeters);
        System.out.println("Distance parcourue " + distanceInMeters[0]);

        int closeRadars = 0;

        System.out.println(MapsActivity.listRadars.size());
        for (Radar radar : MapsActivity.listRadars) {
            //Location location = new Location("Radar");
            float[] distance = new float[1];
            Location.distanceBetween(newLocation.getLatitude(), newLocation.getLongitude(), radar.getPosition().latitude, radar.getPosition().longitude, distance);
            System.out.println("Distance du radar " + distance[0]);
        }

        intent.putExtra("Latitude", newLocation.getLatitude());

        //sendBroadcast(intent);
    }

    public void onProviderDisabled(String provider) {
    }


    public void onProviderEnabled(String provider) {
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return (null);
    }
}