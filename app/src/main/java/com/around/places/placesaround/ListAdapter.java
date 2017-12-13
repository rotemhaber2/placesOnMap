package com.around.places.placesaround;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ListAdapter extends ArrayAdapter<Place> {

    public ListAdapter(Context context, int resource, List<Place> places) {
        super(context, resource, places);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.place_list_row, null);
        }

        Place p =  getItem(position);

        if (p != null) {
            TextView text1 = v.findViewById(R.id.name);
            TextView text2 = v.findViewById(R.id.address);
            ImageView icon = v.findViewById(R.id.imageView1);
            if (text1 != null) {
                text1.setText(p.getPlaceName());
            }
            if (text2 != null) {
                text2.setText(p.getAddress());
            }
            if (icon != null) {
              //  icon.setImageDrawable(p.getIcon());
                icon.setImageResource(R.drawable.restaurant);
            }
        }

        return v;
    }

}