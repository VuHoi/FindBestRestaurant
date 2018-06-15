package com.example.vukhachoi.demo_foody;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapter.Adapter_Res;
import Adapter.MyDatabaseAdapter;
import Model.Restaurant;

public class manage_restaurant extends AppCompatActivity {
    Toolbar toolbar1,toolbar2;
    ListView listView;
    Adapter_Res adapter_res;
    ArrayList<Restaurant> arrayList;
    private BottomSheetBehavior mBottomSheetBehavior;
    View bottomSheet ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_restaurant);


        AddControl();
    }
    private void AddControl() {
        bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(80);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        toolbar1 = findViewById(R.id.tool_back2);
        toolbar1.setNavigationIcon(R.drawable.navi);
        toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });
        toolbar2=findViewById(R.id.tool_fav2);
        toolbar2.inflateMenu(R.menu.manage);

        toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(Restaurant_Details.this,"fav",Toast.LENGTH_LONG).show();

                return true;
            }
        });

        listView=findViewById(R.id.listviewmanage1);
        arrayList=new ArrayList<>();

        SQLiteDatabase database = MyDatabaseAdapter.initDatabase(manage_restaurant.this, "db_foody.sqlite");

        Cursor cursor = database.rawQuery("select * from Restaurant", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String name=cursor.getString(1);
            String restype=cursor.getString(7);
            String address=cursor.getString(4);
            byte[] hinhanh = cursor.getBlob(3);
            arrayList.add(new Restaurant(name,address,restype,hinhanh));
            cursor.moveToNext();
        }
        cursor.close();



        adapter_res=new Adapter_Res(this,R.layout.item_restaurant,arrayList);
        listView.setAdapter(adapter_res);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}



