package Adapter;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vukhachoi.demo_foody.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Restaurant;

/**
 * Created by Hoi on 11/15/2017.
 */

public class AdapterLocation extends ArrayAdapter<Restaurant> {
    Activity context;
    int resource;
    List<Restaurant> objects;

    public AdapterLocation(Activity context, int resource, List<Restaurant> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        TextView textView=row.findViewById(R.id.txtnameRestaurent);
        TextView textView1=row.findViewById(R.id.txtlocation);

        ImageView imageView=row.findViewById(R.id.country_photo);


        final Restaurant restaurant = this.objects.get(position);
        textView.setText(restaurant.getTitle().toString());
        textView1.setText(restaurant.getAddress().toString());

//        Picasso.with(getContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference="+hoa.getPhoto_ref()+"&key=AIzaSyAafUK3_rCTM6esCaZKIj7DNTu8ZkQ6QCw").into(imageView);
        Picasso.with(context)
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+restaurant.getImageString()+"&key=AIzaSyABLune_lERG5qC-CmY4wlY0nM5RuCJ4vs")
                .error(R.drawable.add)
                .into(imageView);
        return row;

    }
}
