package com.danielburgnerjr.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PopularMoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    Context context;

    PopularMoviesDbHelper movieDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_ENTRY, MOVIES);

        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_ENTRY + "/*", MOVIE_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new PopularMoviesDbHelper(getContext());
        context = getContext();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        if (match == MOVIES) {
            retCursor = db.query(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    PopularMoviesContract.PopularMoviesEntry.COLUMN_TIMESTAMP);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(context.getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        if (match == MOVIES) {
            long id = db.insert(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, null, values);
            if (id > 0) {
                returnUri = ContentUris.withAppendedId(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, id);
            } else {
                throw new android.database.SQLException("Failed to insert row into " + uri);
            }
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        context.getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int moviesDeleted; // starts as 0

        if (match == MOVIE_ID) {
            String id = uri.getPathSegments().get(1);
            moviesDeleted = db.delete(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, "id=?", new String[]{id});
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
