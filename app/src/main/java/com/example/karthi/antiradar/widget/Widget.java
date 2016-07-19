package com.example.karthi.antiradar.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Vibrator;
import android.widget.RemoteViews;

import com.example.karthi.antiradar.R;

/**
 * Created by Arnaud on 22/06/2016.
 */
public class Widget extends AppWidgetProvider {

    private int actualSpeed;
    private int radarCount = 0;
    private Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_layout);


       // Nombre de radars à proximité : " + radarCount

        String speed = actualSpeed + " km/h";
        remoteViews.setImageViewBitmap(R.id.widget_label, buildUpdate(speed, context));

        String count = radarCount + " proche";
        remoteViews.setTextViewText(R.id.widget_label2, count);

        for (int i = 0; i< appWidgetIds.length; i++) {
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (intent.getExtras() != null) {
            actualSpeed = extras.getInt("VITESSE");
            if (extras.getInt("CLOSERADARS") > 0) {
                radarCount = extras.getInt("CLOSERADARS");
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                RingtoneManager.getRingtone(context, notification).play();
            }
        }
        super.onReceive(context, intent);
    }

    public Bitmap buildUpdate(String string, Context context)
    {
        Bitmap myBitmap = Bitmap.createBitmap(540, 140, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface clock = Typeface.createFromAsset(context.getAssets(), "fonts/frozencrystal.ttf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        myCanvas.drawText(string, 120, 60, paint);
        return myBitmap;
    }
}
