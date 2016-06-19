package com.example.karthi.antiradar.model;

/**
 * Created by Karthi on 19/06/2016.
 */
public class Radar {

    private float latitude;
    private float longitude;
    private String paysVitesse;
    private String pays;
    private int vitesse;

    public Radar(float latitude, float longitude, String paysVitesse, String pays, int vitesse) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.paysVitesse = paysVitesse;
        this.pays = pays;
        this.vitesse = vitesse;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getPaysVitesse() {
        return paysVitesse;
    }

    public void setPaysVitesse(String paysVitesse) {
        this.paysVitesse = paysVitesse;
    }

    public int getVitesse() {
        return vitesse;
    }

    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }
}