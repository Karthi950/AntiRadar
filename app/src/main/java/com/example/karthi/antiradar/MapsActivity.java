package com.example.karthi.antiradar;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.location.Location;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karthi.antiradar.Settings.SettingsActivity;
import com.example.karthi.antiradar.asynctasks.LoadRadarsAsyncTask;
import com.example.karthi.antiradar.model.OwnRendering;
import com.example.karthi.antiradar.model.Radar;

import com.example.karthi.antiradar.widget.Widget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private static GoogleMap mMap;
    private LocationManager locationManager;
    private Location location;
    private static ClusterManager<Radar> mClusterManager;

    public static List<Radar> listRadars = new ArrayList<>();
    public ListView listView;
    public Context context;


    /* Préférences */

    private static SharedPreferences preferences;

    private long timeUpdate = 5000;
    public static float distanceUpdate = 0.2F;
    public static boolean displayFixedRadars = true;
    public static boolean displayRedLightRadars = true;
    public static float distanceAlert = 500;
    private static float zoom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView radarIcon = (ImageView) findViewById(R.id.radarIcon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            radarIcon.setImageDrawable(getDrawable(R.drawable.cammin));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.ic_launcher));
        }
        setSupportActionBar(toolbar);
        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLocation();
            }
        });
    }

    public void onResume(){
        super.onResume();
        if (mClusterManager == null) {
            return;
        }
        refreshMap();
        refreshLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(new OwnRendering(getApplicationContext(), mMap, mClusterManager));
        mMap.getUiSettings().setMapToolbarEnabled(false);

        refreshLocation();

        LoadRadarsAsyncTask task = new LoadRadarsAsyncTask(context, listView);
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            distanceAlert = Float.parseFloat(preferences.getString("pref_alert_distance", "500"));

            if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate, distanceUpdate, this);
            }

            else if (locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate, distanceUpdate, this);
            }

            if (location != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(zoom)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                updateVitesse(location.getSpeed());
            }
        }
    }

    private static void refreshMap() {
        displayFixedRadars = preferences.getBoolean("radar_fixe_switch", true);
        displayRedLightRadars = preferences.getBoolean("radar_feu_switch", true);
        zoom = Float.parseFloat(preferences.getString("pref_list_zoom_start", "0"));
        distanceAlert = Float.parseFloat(preferences.getString("pref_alert_distance", "500"));
        mClusterManager.clearItems();
        getDisplayedRadars();
        mClusterManager.cluster();
    }

    public static void addRadarsToMap(List<Radar> listRadars) {
        MapsActivity.listRadars = listRadars;
        refreshMap();
        mClusterManager.setAlgorithm(new PreCachingAlgorithmDecorator<Radar>(new GridBasedAlgorithm<Radar>()));
    }

    private static void getDisplayedRadars() {
        for (Radar radar : MapsActivity.listRadars) {
            if ((radar.getTitle().equals("Radar Fixe") && MapsActivity.displayFixedRadars == true)
                    || (radar.getTitle().equals("Radar feu rouge") && MapsActivity.displayRedLightRadars == true)) {
                MapsActivity.mClusterManager.addItem(radar);
            }
        }
    }

    private void updateVitesse(float vitesse) {
        TextView vitesseLabel = (TextView) findViewById(R.id.textView);
        vitesseLabel.setText("Vitesse : "+ vitesse +" km/h");
    }

    public void onLocationChanged(final Location newLocation) {

        int vitesse = Math.round(newLocation.getSpeed()*3.6f); //  m/s en km/h
        int closeRadars = 0;
        for (Radar radar : MapsActivity.listRadars) {
            float[] distance = new float[1];
            Location.distanceBetween(newLocation.getLatitude(), newLocation.getLongitude(), radar.getPosition().latitude, radar.getPosition().longitude, distance);
            if (distance[0] <= MapsActivity.distanceAlert) {
                closeRadars++;
            }
        }

        TextView vitesseLabel = (TextView) findViewById(R.id.textView);
        vitesseLabel.setText("Vitesse : "+ vitesse +" km/h");

        TextView radarNumberTxtView = (TextView) findViewById(R.id.radarNumber);
        if(radarNumberTxtView != null){
            radarNumberTxtView.setText(String.valueOf(closeRadars));
        }

        Intent intent = new Intent(getApplicationContext(), Widget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra("VITESSE", vitesse);
        intent.putExtra("CLOSERADARS", closeRadars);
        int[] ids = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class));
        if(ids != null && ids.length > 0) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            getApplicationContext().sendBroadcast(intent);
        }
    }

    public void onProviderDisabled(String provider) {
    }


    public void onProviderEnabled(String provider) {
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
