package com.around.places.placesaround;


import java.util.List;

public class DataHolder {

    private List<Place> nearbyPlaceList;
    private Place currentPlace;

    public List<Place> getPlacesList() {
        return nearbyPlaceList;
    }
    public void setPlacesList(List<Place> nearbyPlaceList) {
        this.nearbyPlaceList = nearbyPlaceList;
    }

    public void setCurrentPlace(double lat, double lon, String address) {
        this.currentPlace = new Place(lat, lon, address);
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}
