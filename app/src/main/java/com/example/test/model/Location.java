package com.example.test.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lat")
    private double lat;
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    @SerializedName("lon")
    private double lon;
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
}
