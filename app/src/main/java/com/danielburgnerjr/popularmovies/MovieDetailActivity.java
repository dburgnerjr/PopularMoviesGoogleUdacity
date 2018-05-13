package com.danielburgnerjr.popularmovies;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.squareup.picasso.Picasso;
/**
 * Created by dburgnerjr on 6/5/17.
 */

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";

    private Movie mMovie;

    @InjectView(R.id.ivBackdrop)
    ImageView ivBackdrop;
    @InjectView(R.id.movie_poster)
    ImageView ivPoster;
    @InjectView(R.id.movie_title)
    TextView tvTitle;
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

        ButterKnife.inject(this);

        tvTitle.setText(mMovie.getTitle());
        tvDescription.setText(mMovie.getDescription());
        tvReleaseDate.setText(mMovie.getReleaseDate());
        rbRating.setRating(Float.parseFloat(mMovie.getUserRating()));
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
}