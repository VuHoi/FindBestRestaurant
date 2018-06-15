package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vukhachoi.demo_foody.R;
import com.example.vukhachoi.demo_foody.SaveDatabaseActivity;

import java.util.List;

import Model.Restaurant;

/**
 * Created by MTIT on 12/24/2017.
 */

public class AdapterSaveDatabase extends BaseAdapter {

    private SaveDatabaseActivity context; //dịch chuyển intent tại Main
    private int layout;
    private List<Restaurant> restaurantList;

    public AdapterSaveDatabase(SaveDatabaseActivity context, int layout, List<Restaurant> restaurantList) {
        this.context = context;
        this.layout = layout;
        this.restaurantList = restaurantList;
    }


    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }




    private class ViewHolder {
        ImageView image;
        TextView txtName;
        TextView txtAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            //có view rồi thì ánh xạ
            holder.image = (ImageView) convertView.findViewById(R.id.country_photo_save);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtnameRestaurent_save);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txtlocation_save);


            convertView.setTag(holder);

        }else{ //đã tồn tại
            holder = (ViewHolder) convertView.getTag();
        }

        final Restaurant restaurant = restaurantList.get(position);
        holder.txtName.setText(restaurant.getTitle());
        holder.txtAddress.setText(restaurant.getAddress());

        byte[] picture = restaurant.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        holder.image.setImageBitmap(bitmap);


        return convertView;
    }


}
