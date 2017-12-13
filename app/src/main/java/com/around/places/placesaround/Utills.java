package com.around.places.placesaround;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by user on 13/12/2017.
 */

public final class Utills {

    public static String getAddress(double lat, double lon, Context context) {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
            if (addressList != null && addressList.size() > 0) {
                String address = addressList.get(0).getAddressLine(0);
                return address;
            }
        } catch (IOException e) {
            Log.e("GET_ADDRESS", "exception", e);
        }
        return "NA";
    }

}
