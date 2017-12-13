package com.around.places.placesaround;

import android.graphics.drawable.Drawable;

public class Place {

    private String placeName = "NA";
    private String vicinity= "NA";
    private double latitude;
    private double longitude;
    private String icon;
    private String address;


    public Place(){ }

    public Place(double lat, double lon, String address){
        this.latitude = lat;
        this.longitude = lon;
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setIcon(String icon) { }

    public void setAddress(String address) { this.address = address; }

    public String getPlaceName(){
        return this.placeName;
    }

    public String getVicinity(){
        return this.vicinity;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public String getIcon() { return icon; }

    public String getAddress() {  return address; }
}
