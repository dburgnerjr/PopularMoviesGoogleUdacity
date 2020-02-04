package com.danielburgnerjr.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.danielburgnerjr.popularmovies.adapter.ReviewAdapter;
import com.danielburgnerjr.popularmovies.adapter.VideoAdapter;
import com.danielburgnerjr.popularmovies.api.MovieAPI;
import com.danielburgnerjr.popularmovies.model.Movie;
import com.danielburgnerjr.popularmovies.model.Review;
import com.danielburgnerjr.popularmovies.model.Video;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.danielburgnerjr.popularmovies.data.PopularMoviesContract;
import com.danielburgnerjr.popularmovies.data.PopularMoviesDbHelper;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by dburgnerjr on 6/5/17.
 */

public class MovieDetailActivity extends AppCompatActivity implements VideoAdapter.Callbacks, ReviewAdapter.Callbacks {
    public static final String EXTRA_MOVIE = "movie";
    public static final String EXTRA_TRAILERS = "trailer";
    public static final String EXTRA_REVIEWS = "review";
    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    private Movie mMovie;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    private SQLiteDatabase mDb;
    AdView mAdView;

    ImageView ivBackdrop;
    ImageView ivPoster;
    TextView tvDescriptionHeading;
    TextView tvDescription;
    TextView tvReleaseDateHeading;
    TextView tvReleaseDate;
    TextView tvRatingHeading;
    RatingBar rbRating;
    TextView tvVideosHeading;
    RecyclerView rvVideoList;
    TextView tvReviewsHeading;
    RecyclerView rvReviews;

    Button mFavoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        MobileAds.initialize(this, String.valueOf(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError("Intent is null");
        }

        Bundle data = getIntent().getExtras();
        if (data == null) {
            closeOnError(getString(R.string.Data_Not_Found));
            return;
        }
        mMovie = data.getParcelable("movie");
        if (mMovie == null) {
            closeOnError(getString(R.string.Data_Not_Found));
            return;
        }

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        } else {
            throw new IllegalArgumentException("Detail activity must receive a movie parcelable");
        }

        ivBackdrop = findViewById(R.id.ivBackdrop);
        ivPoster = findViewById(R.id.movie_poster);
        tvDescriptionHeading = findViewById(R.id.movie_description_heading);
        tvDescription = findViewById(R.id.movie_description);
        tvReleaseDateHeading = findViewById(R.id.release_date_heading);
        tvReleaseDate = findViewById(R.id.release_date);
        tvRatingHeading = findViewById(R.id.rating_heading);
        rbRating = findViewById(R.id.rating);
        tvVideosHeading = findViewById(R.id.videos_heading);
        rvVideoList = findViewById(R.id.video_list);
        tvReviewsHeading = findViewById(R.id.reviews_heading);
        rvReviews = findViewById(R.id.reviews);
        mFavoriteButton = findViewById(R.id.favorite_button);

        Toolbar tbToolbar = findViewById(R.id.tbToolbar);
        setSupportActionBar(tbToolbar);
        CollapsingToolbarLayout ctlToolbarLayout = findViewById(R.id.toolbar_layout);
        ctlToolbarLayout.setTitle(mMovie.getTitle());
        ctlToolbarLayout.setExpandedTitleColor(Color.WHITE);
        ctlToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        LinearLayout llMovieLayout = new LinearLayout(getApplicationContext());
        llMovieLayout.setOrientation(LinearLayout.HORIZONTAL);

        ButterKnife.bind(this);

        tvDescription.setText(mMovie.getDescription());
        tvReleaseDate.setText(mMovie.getReleaseDate());
        rbRating.setRating((float)mMovie.getUserRating());

        if (!mMovie.isFavorite()) {
            mFavoriteButton.setText(R.string.favorite);
        } else {
            mFavoriteButton.setText(R.string.unfavorite);
        }

        // For horizontal list of trailers
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvVideoList.setLayoutManager(layoutManager);
        mVideoAdapter = new VideoAdapter(new ArrayList<Video>(), this);
        rvVideoList.setAdapter(mVideoAdapter);
        rvVideoList.setNestedScrollingEnabled(false);

        // Fetch trailers only if savedInstanceState == null
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
            List<Video> videos = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
            mVideoAdapter.addVideo(videos);
        } else {
            fetchTrailers(Long.parseLong(mMovie.getId()));
        }

        // For vertical list of reviews
        LinearLayoutManager llmReviews
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvReviews.setLayoutManager(llmReviews);
        mReviewAdapter = new ReviewAdapter(new ArrayList<Review>(), this);
        rvReviews.setAdapter(mReviewAdapter);

        // Fetch reviews only if savedInstanceState == null
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_REVIEWS)) {
            List<Review> reviews = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
            mReviewAdapter.add(reviews);
        } else {
            fetchReviews(Long.parseLong(mMovie.getId()));
        }

        PopularMoviesDbHelper pmDbHelper = new PopularMoviesDbHelper(this);
        mDb = pmDbHelper.getWritableDatabase();

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mMovie.isFavorite()) {
                    mMovie.setFavorite(false);
                    mFavoriteButton.setText(R.string.favorite);
                    if (removeFromFavorites() != 0)
                        Toast.makeText(MovieDetailActivity.this, "This movie was successfully removed from your favorites.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MovieDetailActivity.this, "This movie was unsuccessfully removed from your favorites.", Toast.LENGTH_LONG).show();
                } else {
                    mMovie.setFavorite(true);
                    mFavoriteButton.setText(R.string.unfavorite);
                    if (addToFavorites() != 0)
                        Toast.makeText(MovieDetailActivity.this, "This movie was successfully added to your favorites.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MovieDetailActivity.this, "This movie was unsuccessfully added to your favorites.", Toast.LENGTH_LONG).show();
                }

            }
        });

        Picasso.get()
                .load(TMDB_IMAGE_PATH + mMovie.getPoster())
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)
                .into(ivPoster);
        Picasso.get()
                .load(TMDB_IMAGE_PATH + mMovie.getBackdrop())
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)
                .into(ivBackdrop);

    }

    private void fetchTrailers(long lMovieId) {
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
        mtaService.getMovieVideos(lMovieId, new Callback<Video.VideoResult>() {
            @Override
            public void success(Video.VideoResult videoResult, Response response) {
                mVideoAdapter.setVideoList(videoResult.getVideoList());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void fetchReviews(long lMovieId) {
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
        mtaService.getMovieReviews(lMovieId, new Callback<Review.ReviewResult>() {
            @Override
            public void success(Review.ReviewResult reviewResult, Response response) {
                mReviewAdapter.setReviews(reviewResult.getReviews());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void closeOnError(String msg) {
        finish();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private long addToFavorites() {
        ContentValues cv = new ContentValues();
        cv.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_ID, mMovie.getId());
        cv.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_ORIGINALTITLE, mMovie.getTitle());
        cv.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_OVERVIEW, mMovie.getDescription());
        cv.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_POSTERPATH, mMovie.getPoster());
        cv.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_BACKDROP, mMovie.getBackdrop());
        cv.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_RELEASEDATE, mMovie.getReleaseDate());
        cv.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_NAME_VOTEAVERAGE, mMovie.getUserRating());

        return mDb.insert(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, null, cv);
    }

    private int removeFromFavorites() {
        Uri uri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(mMovie.getId()).build();
        return getContentResolver().delete(uri, null, null);
    }

    public void watch(Video video, int nPosition) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                video.getKey())));
    }

    @Override
    public void read(Review review, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
    }
}