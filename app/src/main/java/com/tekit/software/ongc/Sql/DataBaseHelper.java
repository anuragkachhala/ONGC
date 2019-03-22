package com.tekit.software.ongc.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String TAG =DataBaseHelper.class.getName();
    public DataBaseHelper(Context context) {
        super(context, DataBaseConstant.DATABASE_NAME, null, DataBaseConstant.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            sqLiteDatabase.execSQL(DataBaseConstant.CREATE_TABLE_WELL_LOC);
            Log.v(TAG,DataBaseConstant.CREATE_TABLE_WELL_LOC);
        }catch (Exception e){
            {
                e.printStackTrace();
                Log.v(TAG,DataBaseConstant.CREATE_TABLE_WELL_LOC);
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        onCreate(sqLiteDatabase);

    }
}
