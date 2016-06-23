package com.example.karthi.antiradar.asynctasks;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.example.karthi.antiradar.MapsActivity;
import com.example.karthi.antiradar.model.Radar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karthi on 19/06/2016.
 */
public class LoadRadarsAsyncTask extends AsyncTask<Void, Void, List<Radar>> {

    private final String SITE_RADARS_URL = "http://speedcamlocator.livehost.fr/radarFR";

    private Context context;
    private ListView listView;

    public LoadRadarsAsyncTask(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected List<Radar> doInBackground(Void... params) {
        List<Radar> radarList = new ArrayList<>();
        try {
            JSONArray jsonString = getJsonFromServer(SITE_RADARS_URL);
            for (int i = 0; i < jsonString.length(); i++) {
                JSONObject radarJSON = jsonString.getJSONObject(i);
                radarList.add(new Radar(Float.parseFloat(radarJSON.getString("Latitude")),
                        Float.parseFloat(radarJSON.getString("Longitude")),
                        radarJSON.getString("PaysVitesse"),
                        radarJSON.getString("Pays"),
                        Integer.parseInt(radarJSON.getString("Vitesse"))));
            }
        }
        catch (IOException | JSONException error) {
            Log.d("jsonError ", error.getMessage());
        }
        return (radarList);
    }


    @Override
    protected void onPostExecute(List<Radar> result) {
        //MapsActivity.addRadarsToMap(result);

    }

    public static JSONArray getJsonFromServer(String url) throws IOException {

        BufferedReader inputStream = null;

        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(dc.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String aux = "";
        while ((aux = inputStream.readLine()) != null) {
            builder.append(aux);
        }
        String text = builder.toString();
        try {
            JSONArray json = new JSONArray(text);
            return json;
        }
        catch (JSONException error) {
            System.out.println(error);
            return (null);
        }
    }
}