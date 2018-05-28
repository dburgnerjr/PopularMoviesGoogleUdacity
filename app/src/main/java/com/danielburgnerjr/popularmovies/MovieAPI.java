package com.danielburgnerjr.popularmovies;

/**
 * Created by dburgnerjr on 5/6/18.
 */

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface MovieAPI {
    @GET("/movie/popular")
    void getPopularMovies(Callback<Movie.MovieResult> cb);

    @GET("/movie/top_rated")
    void getTopRatedMovies(Callback<Movie.MovieResult> cb);

    @GET("/movie/{id}/videos")
    void getMovieVideos(@Path("id") long movieId, Callback<Video.VideoResult> cb);

    @GET("/movie/{id}/reviews")
    void getMovieReviews(@Path("id") long movieId, Callback<Review.ReviewResult> cb);

}
