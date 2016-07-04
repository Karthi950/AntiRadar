package com.example.karthi.antiradar.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Arnaud on 22/06/2016.
 */
public class GPSLocator extends Service implements LocationListener {

    Intent intent;

   /* @Override
    public void onStart(Intent intent, int startId) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 50, this);
        }

    }*/

    public void onLocationChanged(final Location loc) {
        Toast.makeText(getApplicationContext(), loc.toString(), Toast.LENGTH_LONG).show();
        System.out.println("CHANGEMENT DE LOC "+loc.toString());
        loc.getLatitude();
        loc.getLongitude();
        intent.putExtra("Latitude", loc.getLatitude());
        intent.putExtra("Longitude", loc.getLongitude());
        intent.putExtra("Provider", loc.getProvider());
        //sendBroadcast(intent);
    }

    public void onProviderDisabled(String provider) {
        //Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
    }


    public void onProviderEnabled(String provider) {
        //Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return (null);
    }
}