package com.pranjaldesai.popularmovies2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pranjal on 9/20/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    // Constructor
    MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + MovieContract.MovieDBEntry.TABLE_NAME + " (" +
                MovieContract.MovieDBEntry._ID                + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieDBEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieDBEntry.COLUMN_RELEASE + " TEXT NOT NULL, "+
                MovieContract.MovieDBEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieDBEntry.COLUMN_BACKDROP + " TEXT NOT NULL, "+
                MovieContract.MovieDBEntry.COLUMN_POSTER + " TEXT NOT NULL, "+
                MovieContract.MovieDBEntry.COLUMN_ADULT + " INTEGER NOT NULL, "+
                MovieContract.MovieDBEntry.COLUMN_MOVIEID + " INTEGER NOT NULL, "+
                MovieContract.MovieDBEntry.COLUMN_VOTE    + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieDBEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
