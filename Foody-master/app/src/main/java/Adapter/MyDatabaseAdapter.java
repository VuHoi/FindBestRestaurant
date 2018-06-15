package Adapter;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MyDatabaseAdapter extends SQLiteOpenHelper {

    private SQLiteDatabase myDataBase;
    private Context context;
    private String databaseName;

    public MyDatabaseAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        this.context = context;
        this.databaseName = name;
    }


    public static SQLiteDatabase initDatabase(Context context, String databaseName) {
        try {
            String outFileName = context.getApplicationInfo().dataDir + "/databases/" + databaseName;
            File f = new File(outFileName);
            if (!f.exists()) {
                InputStream e = context.getAssets().open(databaseName);
                File folder = new File(context.getApplicationInfo().dataDir + "/databases/");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                FileOutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];

                int length;
                while ((length = e.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                e.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
    }

    // implement method
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /*--------------------------------------------------------------------------------------------*/


    //Open database
    public void openDatabase() throws SQLException {
        String myPath = context.getApplicationInfo().dataDir + "/databases/" + databaseName;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    //close database
    public synchronized void closeDataBase() throws SQLException {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    //get database
    public SQLiteDatabase getMyDatabase() {
        return myDataBase;
    }

    //delete database
    public void db_delete() {
        File f = new File(context.getApplicationInfo().dataDir + "/databases/" + databaseName);

        if (f.exists()) {
            f.delete();
            System.out.println("Database file deleted.");
        }
    }


}
