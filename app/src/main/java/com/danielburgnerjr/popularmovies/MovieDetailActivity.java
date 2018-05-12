package com.danielburgnerjr.popularmovies;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
/**
 * Created by dburgnerjr on 6/5/17.
 */

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";

    private Movie mMovie;
    ImageView ivBackdrop;
    ImageView ivPoster;
    TextView tvTitle;
    TextView tvDescriptionHeading;
    TextView tvDescription;
    TextView tvReleaseDateHeading;
    TextView tvReleaseDate;
    TextView tvRatingHeading;
    RatingBar rbRating;

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

        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);
        tvTitle = (TextView) findViewById(R.id.movie_title);
        tvDescriptionHeading = (TextView) findViewById(R.id.movie_description_heading);
        tvDescription = (TextView) findViewById(R.id.movie_description);
        tvReleaseDateHeading = (TextView) findViewById(R.id.release_date_heading);
        tvReleaseDate = (TextView) findViewById(R.id.release_date);
        tvRatingHeading = (TextView) findViewById(R.id.rating_heading);
        rbRating = (RatingBar) findViewById(R.id.rating);
        ivPoster = (ImageView) findViewById(R.id.movie_poster);

        tvTitle.setText(mMovie.getTitle());
        tvDescription.setText(mMovie.getDescription());
        tvReleaseDate.setText(mMovie.getReleaseDate());
        rbRating.setRating(Float.parseFloat(mMovie.getUserRating()));
        Picasso.with(this)
                .load(mMovie.getPoster())
                .into(ivPoster);
        Picasso.with(this)
                .load(mMovie.getBackdrop())
                .into(ivBackdrop);

    }
}