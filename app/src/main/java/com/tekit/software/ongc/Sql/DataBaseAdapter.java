package com.tekit.software.ongc.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.tekit.software.ongc.Model.LocationData;

import java.util.ArrayList;
import java.util.List;

public class DataBaseAdapter {

    private static final String TAG = DataBaseAdapter.class.getName();

    Context context;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public DataBaseAdapter(Context context) {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
    }

    // open DataBase
    public void openDataBase() {
        try {
            sqLiteDatabase = dataBaseHelper.getWritableDatabase();
            //sqLiteDatabase.beginTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //close DataBase
    public void closeDataBase() {

        try {
            dataBaseHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public long setLocationData(List<LocationData> locationDataList) {
        openDataBase();
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        long rowId =0;
        ContentValues values = new ContentValues();
        for (LocationData locationData : locationDataList) {
            values.put(DataBaseConstant.WELL_LAT_COLUMN, locationData.getLat());
            values.put(DataBaseConstant.WELL_LOC_COLUMN, locationData.getLongitude());
            values.put(DataBaseConstant.WELL_ID, locationData.getWellId());
            values.put(DataBaseConstant.WELL_NAME,locationData.getWellName());
            values.put(DataBaseConstant.UWI,locationData.getUWI());
            values.put(DataBaseConstant.SHORT_NAME,locationData.getShortName());
            values.put(DataBaseConstant.RELEASE_NAME,locationData.getReleaseName());
            rowId = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME_WELL_LOC, null, values);

            Log.e(TAG, "" + values + "  " + rowId);


        }

        closeDataBase();

        return rowId;

    }



    public List<LocationData> getLocationData(){
        List<LocationData> locationDataList = new ArrayList<>();
        LocationData locationData;
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(DataBaseConstant.SELECT_TABLE_WELL_LOC, null);
        Log.v(TAG, DataBaseConstant.SELECT_TABLE_WELL_LOC);
        if (cursor.moveToFirst()) {
            do {
                locationData = new LocationData();
                locationData.setWellId(cursor.getString(cursor.getColumnIndex(DataBaseConstant.WELL_ID)));
                locationData.setWellName(cursor.getString(cursor.getColumnIndex(DataBaseConstant.WELL_NAME)));
                locationData.setLongitude(cursor.getString(cursor.getColumnIndex(DataBaseConstant.WELL_LOC_COLUMN)));
                locationData.setLat(cursor.getString(cursor.getColumnIndex(DataBaseConstant.WELL_LAT_COLUMN)));
                locationData.setUWI(cursor.getString(cursor.getColumnIndex(DataBaseConstant.UWI)));
                locationData.setReleaseName(cursor.getString(cursor.getColumnIndex(DataBaseConstant.RELEASE_NAME)));
                locationData.setShortName(cursor.getString(cursor.getColumnIndex(DataBaseConstant.SHORT_NAME)));
                locationDataList.add(locationData);
            } while (cursor.moveToNext());
        }

        return locationDataList;
    }


}
