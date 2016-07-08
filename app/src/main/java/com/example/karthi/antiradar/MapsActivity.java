package com.example.karthi.antiradar;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.karthi.antiradar.Utils.MyItem;
import com.example.karthi.antiradar.Utils.OwnRendring;
import com.example.karthi.antiradar.asynctasks.LoadRadarsAsyncTask;
import com.example.karthi.antiradar.model.OwnRendering;
import com.example.karthi.antiradar.model.Radar;

import com.example.karthi.antiradar.services.GPSLocator;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private LocationManager locationManager;
    private Location location;
    private GPSLocator gpsLocator = new GPSLocator();
    private static ClusterManager<Radar> mClusterManager;
    public static List<Radar> listRadars = new ArrayList<>();
    public ListView listView;
    public Context context;

    /* Préférences */

    private static SharedPreferences preferences;

    private long timeUpdate = 10;
    private float distanceUpdate = 1;
    public static boolean displayFixedRadars = true;
    public static boolean displayRedLightRadars = true;
    public static int distanceAlert = 500;
    private static float zoom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMap();
                refreshLocation();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(new OwnRendering(getApplicationContext(), mMap, mClusterManager));
        refreshLocation();
        LoadRadarsAsyncTask task = new LoadRadarsAsyncTask(context, listView);
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //preferenceGeneralZoom();
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
            Log.d("MENU Setting", "yo je suis dans le menu setting");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    public void preferenceGeneralZoom() {
        boolean test2 = preferences.getBoolean("radar_fixe_switch", false);
        Log.d("test2", Boolean.toString(test2));
        String prefZoom = preferences.getString("pref_list_zoom_start", "0");
        String zoom2 = prefZoom;
        refreshLocation();
        refreshMap();
        Log.d("test3", zoom2);
    }*/

    public void preferenceWidget() {

    }

    private void refreshLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate, distanceUpdate, gpsLocator);

            if (location != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(zoom)
                        .build();
                GPSLocator.currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    private static void refreshMap() {
        displayFixedRadars = preferences.getBoolean("radar_fixe_switch", true);
        displayRedLightRadars = preferences.getBoolean("radar_feu_switch", true);
        zoom = Float.parseFloat(preferences.getString("pref_list_zoom_start", "0"));
        mClusterManager.clearItems();
        mClusterManager.addItems(getDisplayedRadars());
        mClusterManager.cluster();
    }

    public static void addRadarsToMap(List<Radar> listRadars) {
        MapsActivity.listRadars = listRadars;
        refreshMap();
    }

    private static List<Radar> getDisplayedRadars() {
        List<Radar> radarList = new ArrayList<>();
        for (Radar radar : MapsActivity.listRadars) {
            if ((radar.getTitle().equals("Radar Fixe") && MapsActivity.displayFixedRadars == true)
                    || (radar.getTitle().equals("Radar feu rouge") && MapsActivity.displayRedLightRadars == true)) {
                MapsActivity.mClusterManager.addItem(radar);
            }
        }
        return (radarList);
    }
}