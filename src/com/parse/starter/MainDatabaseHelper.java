package com.parse.starter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class MainDatabaseHelper extends SQLiteOpenHelper{
    private static MainDatabaseHelper sInstance = null;

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

        public static final String COLUMN_NAME_CONTENT = "alert";
    }


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ParseReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CONTENT + TEXT_TYPE  +
                    " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static MainDatabaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new MainDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    public MainDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public boolean findById(String id){

        MainDatabaseHelper mDbHelper = this;
        String sortOrder = MainDatabaseHelper.FeedEntry._ID + " DESC";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                MainDatabaseHelper.FeedEntry._ID,
                MainDatabaseHelper.FeedEntry.COLUMN_NAME_ENTRY_ID,
                MainDatabaseHelper.FeedEntry.COLUMN_NAME_TITLE,
        };
        String selection = MainDatabaseHelper.FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { id };
        if (projection == null){
            Log.v("DBH","projection is null!");
            return false;
        }
        else if (selection == null){
            Log.v("DBH","selection is null!");
            return false;
        }
        else if (selectionArgs == null){
            Log.v("DBH","selectionArgs is null!");
            return false;
        }  else {

        Cursor c = db.query(
                MainDatabaseHelper.FeedEntry.TABLE_NAME,  // The table to query
                projection,            // The columns to return
                selection,             // The columns for the WHERE clause
                selectionArgs,         // The values for the WHERE clause
                null,                  // don't group the rows
                null,                  // don't filter by row groups
                sortOrder              // The sort order
        );
        return c.moveToFirst();
        }
    }

}
