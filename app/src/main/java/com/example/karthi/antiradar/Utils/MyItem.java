package com.example.karthi.antiradar.Utils;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Karthi on 23/06/2016.
 */
public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    BitmapDescriptor icon;
    String title;
    String snippet;

    public MyItem(BitmapDescriptor ic,double lat , double lng,String tit ,String sni) {
        mPosition = new LatLng(lat, lng);
        icon = ic;
        title = tit;
        snippet = sni;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }
}
