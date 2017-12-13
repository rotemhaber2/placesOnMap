package com.around.places.placesaround;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView placesList = findViewById(R.id.placesList);
        List<Place> placesArray = new ArrayList<Place>();

        List<Place> nearbyPlaceList = DataHolder.getInstance().getPlacesList();
        for (Place place : nearbyPlaceList) {
            place.setAddress(Utills.getAddress(place.getLatitude(), place.getLongitude(),this));
            placesArray.add(place);
        }

        ArrayAdapter<Place> adapter = new ListAdapter(this, R.layout.place_list_row,nearbyPlaceList);

        placesList.setAdapter(adapter);
    }
}
