package Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by Billy on 9/14/2017.
 */

public class Adapter_Res extends ArrayAdapter<Restaurant> {
    Activity context;
    int resource;
    List<Restaurant> objects;

    public Adapter_Res(Activity context, int resource, List<Restaurant> objects) {
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

        TextView name=row.findViewById(R.id.txtnameRestaurent);
        TextView txtstype=row.findViewById(R.id.txtstype);

        TextView txtlocation=row.findViewById(R.id.txtlocation);

        ImageView country_photo=row.findViewById(R.id.country_photo);


        final Restaurant hoa = this.objects.get(position);

        name.setText(hoa.getTitle().toString());
        txtstype.setText(hoa.getRestype());
        txtlocation.setText(hoa.getAddress());


        byte[] hinhanh = hoa.getImage();
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);
        country_photo.setImageBitmap(bitmap1);
        return row;
    }
}
