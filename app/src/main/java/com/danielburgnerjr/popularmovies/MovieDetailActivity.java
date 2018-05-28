package com.danielburgnerjr.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

public class MovieDetailActivity extends AppCompatActivity {
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
//        mVideoAdapter = new VideoAdapter(new ArrayList<Video>(), (VideoAdapter.Callbacks) this);
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

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public final View vView;
        @InjectView(R.id.trailer_thumbnail)
        public ImageView ivThumbnailView;
        public Video viVideo;

        public VideoViewHolder(View vItemView) {
            super(vItemView);
            ButterKnife.inject(ivThumbnailView);
            vView = vItemView;
        }
    }

    public static class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
        private ArrayList<Video> mVideoList;
        private final Callbacks mCallbacks;
        private LayoutInflater mInflater;
        private Context mContext;

        public interface Callbacks {
            void watch(Video video, int position);
        }

        public VideoAdapter(ArrayList<Video> videos, Callbacks cb) {
            mVideoList = videos;
            mCallbacks = cb;
        }

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int nViewType) {
            mInflater = LayoutInflater.from(parent.getContext());
            View vView = mInflater.inflate(R.layout.trailer_list, parent, false);
            final VideoViewHolder vvhViewHolder = new VideoViewHolder(vView);
            return vvhViewHolder;
        }

        @Override
        public void onBindViewHolder(final VideoViewHolder holder, final int position) {
            final Video video = mVideoList.get(position);
            mContext = holder.vView.getContext();

            float paddingLeft = 0;
            if (position == 0) {
                paddingLeft = mContext.getResources().getDimension(R.dimen.detail_horizontal_padding);
            }

            float paddingRight = 0;
            if (position + 1 != getItemCount()) {
                paddingRight = mContext.getResources().getDimension(R.dimen.detail_horizontal_padding) / 2;
            }

            holder.vView.setPadding((int) paddingLeft, 0, (int) paddingRight, 0);

            holder.viVideo = video;

            String thumbnailUrl = "http://img.youtube.com/vi/" + video.getKey() + "/0.jpg";

            Picasso.with(mContext)
                    .load(thumbnailUrl)
                    .config(Bitmap.Config.RGB_565)
                    .into(holder.ivThumbnailView);

            holder.vView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.watch(video, holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return (mVideoList == null) ? 0 : mVideoList.size();
        }

        public void addVideo(List<Video> mVideo) {
            mVideoList.clear();
            mVideoList.addAll(mVideo);
            notifyDataSetChanged();
        }

        public void setVideoList(List<Video> mVideo) {
            mVideoList.clear();
            mVideoList.addAll(mVideo);
            notifyDataSetChanged();
        }
    }

    public static class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

        private final ArrayList<Review> mReviews;
        private final Callbacks mCallbacks;

        public ReviewAdapter(ArrayList<Review> reviews, Callbacks callbacks) {
            mReviews = reviews;
            mCallbacks = callbacks;
        }

        public interface Callbacks {
            void read(Review review, int position);
        }

        @Override
        public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_list, parent, false);
            return new ReviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ReviewViewHolder holder, final int position) {
            final Review review = mReviews.get(position);

            holder.mReview = review;
            holder.mContentView.setText(review.getContent());
            holder.mAuthorView.setText(review.getAuthor());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.read(review, holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mReviews.size();
        }

        public class ReviewViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            @InjectView(R.id.review_content)
            TextView mContentView;
            @InjectView(R.id.review_author)
            TextView mAuthorView;
            public Review mReview;

            public ReviewViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
                mView = view;
            }
        }

        public void add(List<Review> reviews) {
            mReviews.clear();
            mReviews.addAll(reviews);
            notifyDataSetChanged();
        }

        public void setReviews(List<Review> mReview) {
            mReviews.clear();
            mReviews.addAll(mReview);
            notifyDataSetChanged();
        }

        public ArrayList<Review> getReviews() {
            return mReviews;
        }
    }
}