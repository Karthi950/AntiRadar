package com.example.karthi.antiradar;


import android.content.Context;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.ListView;

import com.example.karthi.antiradar.Utils.MyItem;
import com.example.karthi.antiradar.Utils.OwnRendring;
import com.example.karthi.antiradar.asynctasks.LoadRadarsAsyncTask;
import com.example.karthi.antiradar.model.Radar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.maps.android.clustering.ClusterManager;


import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private static ClusterManager<MyItem> mClusterManager;

    private static List<Radar> listRadars = new ArrayList<>();

    public ListView listView;
    public Context context;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Paris, France.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

    /*public static void addRadarsToMap(List<Radar> listRadars) {
        MapsActivity.listRadars = listRadars;


        for (Radar radar : listRadars) {
            LatLng radarLatLong = new LatLng(radar.getLatitude(), radar.getLongitude());
            mMap.addMarker(
                    new MarkerOptions()
                            .position(radarLatLong)
                            .title("Info Vitesse : " + radar.getVitesse()+ " km/h")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.cam))
                            .snippet("Radar Fixe")
            );
        }

        LatLng latlng = new LatLng(44.03836, 4.88396);


        // Move the camera instantly to location with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }*/

    private void setUpClusterer() {
        // Declare a variable for the cluster manager.


        // Position the map.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
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
            MyItem offsetItem = new MyItem(BitmapDescriptorFactory.fromResource(R.drawable.cammin),radar.getLatitude(), radar.getLongitude(),"Info Vitesse : " +radar.getVitesse(),"Radar fixe");
            mClusterManager.addItem(offsetItem);

        }



    }


    private void addItems()  {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(BitmapDescriptorFactory.fromResource(R.drawable.cammin),lat, lng,"test","test1");
            mClusterManager.addItem(offsetItem);
        }



    }





}
