package com.around.places.placesaround;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import android.util.Log;
import java.util.concurrent.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    LocationManager locationManager;
    LocationListener locationListener;
    int PROXIMITY_RADIUS = 1000;
    String placesKey = "my_key";
    String placesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(TAG, "init map: on map ready");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            private int FASTEST_INTERVAL = 100000;
            private Location currentLocation = null;
            private long locationUpdatedAt = Long.MIN_VALUE;
            @Override
            public void onLocationChanged(Location location) {
                // avoid update the location frequently in order to save calls to API
                boolean updateLocation = false;
                if(currentLocation == null){
                    currentLocation = location;
                    locationUpdatedAt = System.currentTimeMillis();
                    updateLocation = true;
                } else {
                    long secondsElapsed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - locationUpdatedAt);
                    if (secondsElapsed >= TimeUnit.MILLISECONDS.toSeconds(FASTEST_INTERVAL)){
                        // check location accuracy here
                        currentLocation = location;
                        locationUpdatedAt = System.currentTimeMillis();
                        updateLocation = true;
                    }
                }
                if(updateLocation){
                    displayLocationsOnMap(location);
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        // check if there are permissions, if not request from the user
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            displayLocationsOnMap(lastKnownLocation);
        }
    }

    /**
     * display both the current location and the places around on the map
     * @param location
     */

    public void displayLocationsOnMap(Location location){

        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        float zoomLevel = 16.0f;
        if(location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            String locationType = "restaurant";
            String url = getUrl(lat, lon, locationType);
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            getNearbyPlacesData.execute(dataTransfer); // get places location via API
            /* set current location on the map in different color */
            LatLng latLang = new LatLng(lat, lon);
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
                if (addressList != null && addressList.size() > 0) {
                    String address = addressList.get(0).getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(latLang).title(address));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoomLevel));

            } catch (IOException e) {
                Log.e(TAG, "exception", e);
            }
        }
    }

    /**
     * build the url for the API call the get places
     * @param lat
     * @param lon
     * @param locationType
     * @return
     */
    private String getUrl(double lat, double lon, String locationType){
        StringBuilder googlePlacesUrl = new StringBuilder(placesUrl);
        googlePlacesUrl.append("location="+lat+","+lon);
        googlePlacesUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&type="+locationType);
        googlePlacesUrl.append("&key="+placesKey);
        return googlePlacesUrl.toString();
    }
}
