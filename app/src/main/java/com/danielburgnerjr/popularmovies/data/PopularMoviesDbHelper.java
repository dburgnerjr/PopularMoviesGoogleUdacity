package com.danielburgnerjr.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PopularMoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "popularmovie.db";
    private static final int DATABASE_VERSION = 2;

    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                PopularMoviesContract.PopularMoviesEntry.TABLE_NAME + " (" +
                //MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_ID + " TEXT NOT NULL PRIMARY KEY, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_ORIGINALTITLE + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_OVERVIEW + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_POSTERPATH + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_BACKDROP + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_RELEASEDATE + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_VOTEAVERAGE + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + PopularMoviesContract.PopularMoviesEntry.TABLE_NAME);
        onCreate(db);

    }

}
