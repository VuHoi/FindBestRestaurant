package com.example.vukhachoi.demo_foody;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import junit.framework.Test;

import java.util.ArrayList;

import Adapter.AdapterSaveDatabase;
import Adapter.MyDatabaseAdapter;
import Model.Restaurant;

public class SaveDatabaseActivity extends AppCompatActivity {
    Toolbar toolbar2;
    private  int REQUESTCODE=123;

    final String DATABASE_NAME = "db_foody.sqlite";
    SQLiteDatabase database;

    ListView lvRestaurant;
    ArrayList<Restaurant> arrayRestaurant;
    AdapterSaveDatabase adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_database);

        addControl();

        addListView();

        readData();

    }

    private void addControl() {
        toolbar2=findViewById(R.id.tool_fav1_savelist);
        toolbar2.inflateMenu(R.menu.search);

        //kích vào trái tim->qua mh tìm kiếm
        toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(SaveDatabaseActivity.this,location_chooser.class);
                startActivityForResult(intent,REQUESTCODE);
                return true;
            }
        });
    }


    private void addListView() {
        lvRestaurant = (ListView) findViewById(R.id.listview_savelist);
        arrayRestaurant = new ArrayList<>();
        adapter = new AdapterSaveDatabase(this, R.layout.row_save_restaurant, arrayRestaurant);
        lvRestaurant.setAdapter(adapter);



        lvRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SaveDatabaseActivity.this, arrayRestaurant.get(position).getTitle()+"\n"+arrayRestaurant.get(position).getAddress(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SaveDatabaseActivity.this, Restaurant_Details.class);
                intent.putExtra("ChooseLocation", arrayRestaurant.get(position));
                startActivity(intent);
            }
        });


        lvRestaurant.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder dialogXoa = new AlertDialog.Builder(SaveDatabaseActivity.this);
                dialogXoa.setMessage("Xóa nhà hàng " + arrayRestaurant.get(position).getTitle() + " ?");

                dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(arrayRestaurant.get(position).getId());
                        Toast.makeText(SaveDatabaseActivity.this, "Đã xóa: " + arrayRestaurant.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });

                dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                dialogXoa.show();


                return true;
            }
        });


    }


    private void delete(int id){
        SQLiteDatabase database = MyDatabaseAdapter.initDatabase(SaveDatabaseActivity.this, "db_foody.sqlite");
        database.delete("Restaurant", "ID = ?", new String[]{id + ""});

        //reload after delete on Dialog
        readData();
    }



    private void readData(){
        database = MyDatabaseAdapter.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM Restaurant", null);

        arrayRestaurant.clear();
        while(cursor.moveToNext()){
            arrayRestaurant.add(new Restaurant(cursor.getInt(0),  //Cột 0: Id, 1: Title, 2: Image, 3: Address
                    cursor.getString(1),
                    cursor.getString(3),
                    cursor.getBlob(2),

                    Double.parseDouble(cursor.getString(4)),
                    Double.parseDouble(cursor.getString(5))
            ));
        }
        adapter.notifyDataSetChanged(); //cập nhật adapter
    }



}
