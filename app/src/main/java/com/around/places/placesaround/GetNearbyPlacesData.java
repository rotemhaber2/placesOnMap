package com.around.places.placesaround;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;

    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadURL = new DownloadUrl();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){
        float zoomLevel = 16.0f;
        List<Place> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);

        DataHolder.getInstance().setPlacesList(nearbyPlaceList); // save the places list in singleton

        Log.d("nearbyplacesdata","called parse method");
        mMap.clear();
        Place currentPlace = DataHolder.getInstance().getCurrentPlace();
        LatLng latLang = new LatLng(currentPlace.getLatitude(), currentPlace.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLang).title(currentPlace.getAddress()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoomLevel));
        showNearbyPlaces(nearbyPlaceList);
    }


    /**
     * get the list of location from the API and display
     * them on the map
     * @param nearbyPlaceList
     */
    private void showNearbyPlaces(List<Place> nearbyPlaceList)
    {
        for(int i = 0; i < nearbyPlaceList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            Place googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.getPlaceName();
            String vicinity = googlePlace.getVicinity();
            double lat = googlePlace.getLatitude();
            double lng = googlePlace.getLongitude();

            LatLng latLng = new LatLng( lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(markerOptions);
        }
    }
}
