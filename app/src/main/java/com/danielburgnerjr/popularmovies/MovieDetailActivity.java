package com.danielburgnerjr.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

    private Movie mMovie;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    @InjectView(R.id.ivBackdrop)
    ImageView ivBackdrop;
    @InjectView(R.id.movie_poster)
    ImageView ivPoster;
    @InjectView(R.id.movie_description_heading)
    TextView tvDescriptionHeading;
    @InjectView(R.id.movie_description)
    TextView tvDescription;
    @InjectView(R.id.release_date_heading)
    TextView tvReleaseDateHeading;
    @InjectView(R.id.release_date)
    TextView tvReleaseDate;
    @InjectView(R.id.rating_heading)
    TextView tvRatingHeading;
    @InjectView(R.id.rating)
    RatingBar rbRating;
    @InjectView(R.id.videos_heading)
    TextView tvVideosHeading;
    @InjectView(R.id.video_list)
    RecyclerView rvVideoList;
    @InjectView(R.id.watch_trailer)
    Button mButtonWatchTrailer;
    @InjectView(R.id.reviews_heading)
    TextView tvReviewsHeading;
    @InjectView(R.id.reviews)
    RecyclerView rvReviews;

    @InjectView(R.id.favorite_button)
    Button mFavoriteButton;
    @InjectView(R.id.unfavorite_button)
    Button mUnfavoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        } else {
            throw new IllegalArgumentException("Detail activity must receive a movie parcelable");
        }

        Toolbar tbToolbar = (Toolbar) findViewById(R.id.tbToolbar);
        setSupportActionBar(tbToolbar);
        CollapsingToolbarLayout ctlToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ctlToolbarLayout.setTitle(mMovie.getTitle());
        ctlToolbarLayout.setExpandedTitleColor(Color.WHITE);
        ctlToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        LinearLayout llMovieLayout = new LinearLayout(getApplicationContext());
        llMovieLayout.setOrientation(LinearLayout.HORIZONTAL);

        ButterKnife.inject(this);

        tvDescription.setText(mMovie.getDescription());
        tvReleaseDate.setText(mMovie.getReleaseDate());
        rbRating.setRating(Float.parseFloat(mMovie.getUserRating()));

        // For horizontal list of trailers
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        rvVideoList.setLayoutManager(layoutManager);
//        mVideoAdapter = new VideoAdapter(new ArrayList<Video>(), this);
//        rvVideoList.setAdapter(mVideoAdapter);
//        rvVideoList.setNestedScrollingEnabled(false);
//
//        // Fetch trailers only if savedInstanceState == null
//        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
//            List<Video> videos = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
//            mVideoAdapter.addVideo(videos);
//            mButtonWatchTrailer.setEnabled(true);
//        } else {
//            fetchTrailers(Long.parseLong(mMovie.getId()));
//        }

        // For vertical list of reviews
        mReviewAdapter = new ReviewAdapter(new ArrayList<Review>(), (ReviewAdapter.Callbacks) this);
        rvReviews.setAdapter(mReviewAdapter);

        // Fetch reviews only if savedInstanceState == null
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_REVIEWS)) {
            List<Review> reviews = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
            mReviewAdapter.add(reviews);
        } else {
            fetchReviews(Long.parseLong(mMovie.getId()));
        }

        Picasso.with(this)
                .load(mMovie.getPoster())
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)
                .into(ivPoster);
        Picasso.with(this)
                .load(mMovie.getBackdrop())
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

    public void watch(Video video, int nPosition) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                video.getKey())));
    }

    @Override
    public void read(Review review, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
    }
}