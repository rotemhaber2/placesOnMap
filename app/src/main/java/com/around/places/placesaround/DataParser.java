package com.around.places.placesaround;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataParser {

    /** parse one place that returned in the list from the server
     *
     * @param googlePlaceJson
     * @return
     */
    private Place getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        Place place = new Place();

        Log.d("DataParser","jsonobject ="+googlePlaceJson.toString());


        try {
            if (!googlePlaceJson.isNull("name")) {
                place.setPlaceName(googlePlaceJson.getString("name"));
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                place.setVicinity(googlePlaceJson.getString("vicinity"));
            }
            if (googlePlaceJson.getJSONObject("geometry") != null && googlePlaceJson.getJSONObject("geometry").getJSONObject("location") != null) {
                place.setLatitude(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                place.setLongitude(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
            }

            if (!googlePlaceJson.isNull("icon")) {
                place.setIcon(googlePlaceJson.getString("icon"));
            }


        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

    /**
     * get the places list as a json from the server and
     * return that as a list
     * @param jsonArray
     * @return
     */
    private List<Place>getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<Place> placelist = new ArrayList<>();
        Place placeMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }


    /**
     * get json from the server and return as a list
     * @param jsonData
     * @return
     */
    public List<Place> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}
