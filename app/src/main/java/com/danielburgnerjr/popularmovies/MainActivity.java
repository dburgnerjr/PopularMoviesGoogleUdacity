package com.danielburgnerjr.popularmovies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.ButterKnife;

import com.danielburgnerjr.popularmovies.data.PopularMoviesContract;
import com.danielburgnerjr.popularmovies.data.PopularMoviesDbHelper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private static final String CURRENT_RECYCLER_VIEW_POSITION = "CurrentRecyclerViewPosition";
    RecyclerView rvRecyclerView;
    Spinner spnMenuOptions;
    private MoviesAdapter maAdapter;
    private SQLiteDatabase mDb;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, String.valueOf(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        PopularMoviesDbHelper pmDbHelper = new PopularMoviesDbHelper(this);
        mDb = pmDbHelper.getWritableDatabase();

        rvRecyclerView = findViewById(R.id.rvRecyclerView);
        spnMenuOptions = findViewById(R.id.spnMenuOptions);

        ButterKnife.bind(this);
        rvRecyclerView.setHasFixedSize(true);
        rvRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        if (rvRecyclerView.getLayoutManager() != null)
            rvRecyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        maAdapter = new MoviesAdapter(this);
        rvRecyclerView.setAdapter(maAdapter);

        String[] strOptions = getResources().getStringArray(R.array.sort_options);

        ArrayAdapter<String> arAdapter = new ArrayAdapter<>
                (this, R.layout.spinner_item, strOptions);

        spnMenuOptions.setAdapter(arAdapter);

        spnMenuOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        getPopularMovies();
                        break;
                    case 1:
                        getTopRatedMovies();
                        break;
                    case 2:
                        getFavoriteMovies();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void getPopularMovies() {
        RestAdapter raAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", getText(R.string.api_key).toString());
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MovieAPI mtaService = raAdapter.create(MovieAPI.class);
        mtaService.getPopularMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                maAdapter.setMovieList(movieResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void getTopRatedMovies() {
        RestAdapter raAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade rfRequest) {
                        rfRequest.addEncodedQueryParam("api_key", getText(R.string.api_key).toString());
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MovieAPI mtaService = raAdapter.create(MovieAPI.class);
        mtaService.getTopRatedMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                maAdapter.setMovieList(movieResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void getFavoriteMovies() {
        Cursor cursor = mDb.query(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_VOTEAVERAGE);

        List<Movie> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            Movie movC = new Movie(
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_ORIGINALTITLE)),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_POSTERPATH)),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_BACKDROP)),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_RELEASEDATE)),
                    cursor.getDouble(cursor.getColumnIndex(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_VOTEAVERAGE)),
                    true);
            System.out.println(movC.getPoster() + " " + movC.getBackdrop());
            result.add(movC);
        }
        cursor.close();

        maAdapter.setMovieList(result);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        GridLayoutManager layoutManager = (GridLayoutManager) rvRecyclerView.getLayoutManager();
        if(layoutManager != null)
            outState.putInt(CURRENT_RECYCLER_VIEW_POSITION, layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int currentPosition = savedInstanceState.getInt(CURRENT_RECYCLER_VIEW_POSITION);
        rvRecyclerView.scrollToPosition(currentPosition);
    }

}
