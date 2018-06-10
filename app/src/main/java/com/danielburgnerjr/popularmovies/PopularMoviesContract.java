package com.danielburgnerjr.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PopularMoviesContract {

    private PopularMoviesContract(){}

    public static final String AUTHORITY = "com.danielburgnerjr.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_ENTRY = "entry";

    /* Define table and content */
    public static class PopularMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRY).build();

        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_ORIGINALTITLE = "originalTitle";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_POSTERPATH = "posterPath";
        public static final String COLUMN_NAME_RELEASEDATE = "releaseDate";
        public static final String COLUMN_NAME_VOTEAVERAGE = "voteAverage";
        public static final String COLUMN_TIMESTAMP ="timestamp";
    }
}
