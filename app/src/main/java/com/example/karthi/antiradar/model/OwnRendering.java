package com.example.karthi.antiradar.model;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Karthi on 27/06/2016.
 */


public class OwnRendering extends DefaultClusterRenderer<Radar> {

    public OwnRendering(Context context, GoogleMap map,
                        ClusterManager<Radar> clusterManager) {
        super(context, map, clusterManager);
    }

    protected void onBeforeClusterItemRendered(Radar item, MarkerOptions markerOptions) {

        markerOptions.icon(item.getIcon());
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
