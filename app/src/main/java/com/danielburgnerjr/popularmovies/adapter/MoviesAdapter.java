package com.danielburgnerjr.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielburgnerjr.popularmovies.MovieDetailActivity;
import com.danielburgnerjr.popularmovies.R;
import com.danielburgnerjr.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;
    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    public MoviesAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int nViewType) {
        View vView = mInflater.inflate(R.layout.movie_list, parent, false);
        final MovieViewHolder mvhViewHolder = new MovieViewHolder(vView);
        vView.setOnClickListener(vView1 -> {
            int nPosition = mvhViewHolder.getAdapterPosition();
            Intent intI = new Intent(mContext, MovieDetailActivity.class);
            intI.putExtra(MovieDetailActivity.EXTRA_MOVIE, mMovieList.get(nPosition));
            mContext.startActivity(intI);
        });
        return mvhViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        Picasso.get()
                .load(TMDB_IMAGE_PATH + movie.getPoster())
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)
                .into(holder.ivImageView);
    }

    @Override
    public int getItemCount() {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> mMovie) {
        mMovieList = new ArrayList<>();
        mMovieList.addAll(mMovie);
        notifyDataSetChanged();
    }
}
