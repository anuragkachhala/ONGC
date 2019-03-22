package com.tekit.software.ongc.Sql;

public class DataBaseConstant {

    public static final String TABLE_NAME_WELL_LOC = "WELL_LOCATION_MASTER";

    // port data table
    public static final String WELL_LAT_COLUMN = "LONGITUDE";
    public static final String WELL_LOC_COLUMN = "LATITUDE";
    public static final String WELL_ID = "WELL_ID";
    public static final String WELL_NAME="WELL_NAME";
    public static final String UWI="UWI";
    public static final String SHORT_NAME="SHORT_NAME";
    public static final String RELEASE_NAME="RELEASE_NAME";
    public static final String CREATE_TABLE_WELL_LOC = "CREATE TABLE " + TABLE_NAME_WELL_LOC + "(" +
            WELL_ID + " TEXT,  " +
            WELL_LAT_COLUMN + " TEXT, " +
            WELL_LOC_COLUMN + " TEXT, "+
            WELL_NAME+" TEXT, "+
            UWI+" TEXT, "+
            SHORT_NAME+" TEXT, "+
            RELEASE_NAME+" TEXT "
            + ")";
    // Database Version
    public static final int DATABASE_VERSION = 2;
    // Database Name
    public static final String DATABASE_NAME = "ONGCAMD.db";
    public static String SELECT_TABLE_WELL_LOC = "SELECT  * FROM " + TABLE_NAME_WELL_LOC + " ORDER BY " + WELL_ID + " DESC ";


}
