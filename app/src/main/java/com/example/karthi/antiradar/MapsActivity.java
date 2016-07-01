package com.example.karthi.antiradar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.karthi.antiradar.model.Radar;

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
    private static ClusterManager<MyItem> mClusterManager;

    private static List<Radar> listRadars = new ArrayList<>();



    public ListView listView;
    public Context context;

    private Marker radarFixe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(100)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }*/
        LoadRadarsAsyncTask task = new LoadRadarsAsyncTask(context , listView);
        setUpClusterer();
        task.execute();
    }

  /*  public static void addRadarsToMap(List<Radar> listRadars) {

        ArrayList<Marker> radarFixe = new ArrayList<Marker>();
        MapsActivity.listRadars = listRadars;


        for (Radar radar : listRadars) {
            LatLng radarLatLong = new LatLng(radar.getLatitude(), radar.getLongitude());
            Marker marker = mMap.addMarker(
                    new MarkerOptions()
                            .position(radarLatLong)
                            .title("Info Vitesse : " + radar.getVitesse()+ " km/h")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.cam))
                            .snippet("Radar Fixe")
            );

            radarFixe.add(marker);
        }


        /*for (Marker marker : radarFixe) {
            marker.setVisible(false);
            //marker.remove(); <-- works too!
        }*/


/*
        LatLng latlng = new LatLng(44.03836, 4.88396);


        // Move the camera instantly to location with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }
*/
    private void setUpClusterer() {

        // Position the map.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mMap, mClusterManager));

       // addItems();

    }



    public static void addRadarsToMap(List<Radar> listRadars)  {

          MapsActivity.listRadars = listRadars;


        for (Radar radar : listRadars) {
            //LatLng radarLatLong = new LatLng(radar.getLatitude(), radar.getLongitude());
            MyItem offsetItem = new MyItem(null,radar.getLatitude(), radar.getLongitude(),"Info Vitesse : " +radar.getVitesse(),"Radar fixe");
            mClusterManager.addItem(offsetItem);

        }



    }


    private void addItems()  {


        double lat = 51.5145160;
        double lng = -0.1270060;


        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(BitmapDescriptorFactory.fromResource(R.drawable.cammin),lat, lng,"test","test1");
            mClusterManager.addItem(offsetItem);
        }



    }





}
