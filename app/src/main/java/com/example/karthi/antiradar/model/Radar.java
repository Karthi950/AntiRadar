package com.example.karthi.antiradar.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Karthi on 19/06/2016.
 */
public class Radar implements ClusterItem {

    private BitmapDescriptor icon;
    private LatLng position;
    private String title;
    private String snippet;

    public Radar(BitmapDescriptor icon, float latitude, float longitude, String title, String snippet) {
        this.icon = icon;
        this.position = new LatLng(latitude, longitude);
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
