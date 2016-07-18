package com.example.karthi.antiradar.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.widget.RemoteViews;

import com.example.karthi.antiradar.R;

/**
 * Created by Arnaud on 22/06/2016.
 */
public class Widget extends AppWidgetProvider {

    private float actualSpeed;
    private int radarCount = 0;
    private Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.widget_label, "Vitesse : "+actualSpeed+" km/h\nNombre de radars à proximité : "+radarCount);

        for (int i = 0; i< appWidgetIds.length; i++) {
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (intent.getExtras() != null) {
            actualSpeed = extras.getFloat("VITESSE");
            if (extras.getInt("CLOSERADARS") > 0) {
                radarCount = extras.getInt("CLOSERADARS");
                RingtoneManager.getRingtone(context, notification).play();
            }
        }
        super.onReceive(context, intent);
    }
}
