package com.example.karthi.antiradar.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Arnaud on 22/06/2016.
 */
public class Widget extends AppWidgetProvider {

    private int radarsCount = 0;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        System.out.println("VIBRATION");
        //v.vibrate(500);
        /*super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId, new Date().toString());
        }*/
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String text) {
        /*RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.button_widget, text);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);*/
    }
}
