package com.example.vukhachoi.demo_foody;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapter.AdapterSaveDatabase;
import Adapter.AdapterSpinner;
import Adapter.MyDatabaseAdapter;
import Model.Restaurant;

public class location_chooser extends AppCompatActivity {

    EditText editText;
    Spinner spinnerSearch;
    ArrayList<Restaurant> arraySearch;
    AdapterSpinner adapter;

    Button btnShow;
    Button btnChoose;
    int check = -1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_chooser);
        btnShow=findViewById(R.id.btnShow);
        btnChoose=findViewById(R.id.btnChooseRestaurant);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.my_color));
//       getSupportActionBar().hide();


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gửi list qua qua màn hình chứa list
                addSpinner();
            }
        });

        //show map
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check != -1) {
                    Intent intent = new Intent(location_chooser.this, Restaurant_Details.class);
                    intent.putExtra("ChooseLocation", arraySearch.get(check)); //check = position
                    startActivity(intent);
                }
            }
        });



    }

    private void addSpinner() {
        spinnerSearch = (Spinner) findViewById(R.id.spinnerNameResult);
        arraySearch = new ArrayList<>();
        arraySearch = ArraySearch();

        adapter = new AdapterSpinner(this, R.layout.row_spinner, arraySearch);
        spinnerSearch.setAdapter(adapter);



        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                check = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }



    public ArrayList<Restaurant> ArraySearch() {
        SQLiteDatabase database = MyDatabaseAdapter.initDatabase(location_chooser.this, "db_foody.sqlite");

        editText=findViewById(R.id.edtcity);

        arraySearch = new ArrayList<>();

        //trường hợp lần 1 tìm đc list, lần 2 tìm ko đc, thì check vẫn giữ value của lần trước, nên reset check lại
        check = -1;

        try {
            if (!editText.getText().toString().equals("")) {

                //Search in database
                String table = "Restaurant"; // The table to query
                String[] columns = new String[]{"id", "title", "img", "address", "Lati", "Longti"}; // The columns to return
                String where = "address" + " LIKE ?";  // WHERE statement. Make sure you have the correct spacing "_LIKE_?"
                String searchString = "%" + editText.getText().toString() + "%"; // Arguments to WHERE.
                String[] whereArgs = new String[]{searchString};

                Cursor cursor = null;
                cursor = database.query(true, table, columns, where, whereArgs, null, null, null, null);


                if(cursor != null && cursor.getCount() > 0){

                    //show seach list
                    Toast.makeText(this, "Show list successful!", Toast.LENGTH_SHORT).show();

                    cursor.moveToFirst();
                    arraySearch.clear();
                    while (!cursor.isAfterLast()) {
                        arraySearch.add(new Restaurant(cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(3),
                                cursor.getBlob(2),

                                Double.parseDouble(cursor.getString(4)),
                                Double.parseDouble(cursor.getString(5))
                        ));
                        cursor.moveToNext();
                    }
                    cursor.close();
                    return arraySearch;

//                    Intent intent = new Intent();
//                    intent.putExtra("arraylistSearch", arraySearch);
//                    setResult(RESULT_OK, intent);

                }else{
                    setResult(RESULT_CANCELED);
                    Toast.makeText(this, "Can not find this address!", Toast.LENGTH_SHORT).show();
                }

            }else {
                setResult(RESULT_CANCELED);
                Toast.makeText(this, "Please enter your address!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return arraySearch;
    }
}
