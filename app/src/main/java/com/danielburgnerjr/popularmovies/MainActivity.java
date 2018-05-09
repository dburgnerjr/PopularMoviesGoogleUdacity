package com.danielburgnerjr.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvRecyclerView;
    private MoviesAdapter maAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        rvRecyclerView = (RecyclerView) findViewById(R.id.rvRecyclerView);
        rvRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        maAdapter = new MoviesAdapter(this);
        rvRecyclerView.setAdapter(maAdapter);
        getPopularMovies();
    }

    private void getPopularMovies() {
        RestAdapter raAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", "8a6f42fe5f7efc6139cda365db5c89a1");
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MovieAPI mtaService = raAdapter.create(MovieAPI.class);
        mtaService.getPopularMovies(new Callback<Movie.MovieResult>() {     // NPE
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                maAdapter.setMovieList(movieResult.getResults());           // NPE
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImageView;
        public MovieViewHolder(View vItemView) {
            super(vItemView);
            ivImageView = (ImageView) vItemView.findViewById(R.id.ivImageView);
        }
    }

    public static class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
        private List<Movie> mMovieList;
        private LayoutInflater mInflater;
        private Context mContext;

        public MoviesAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int nViewType) {
            View vView = mInflater.inflate(R.layout.movie_list, parent, false);
            final MovieViewHolder mvhViewHolder = new MovieViewHolder(vView);
/*          vView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vView) {
                    int nPosition = mvhViewHolder.getAdapterPosition();
                    Intent intI = new Intent(mContext, MovieDetailActivity.class);
                    intI.putExtra(MovieDetailActivity.EXTRA_MOVIE, mMovieList.get(nPosition));
                    mContext.startActivity(intI);
                }
            });
*/         return mvhViewHolder;
        }

        @Override
        public void onBindViewHolder(MovieViewHolder holder, int position) {
            Movie movie = mMovieList.get(position);
            Picasso.with(mContext)
                    .load(movie.getPoster())
                    .placeholder(R.color.colorAccent)
                    .into(holder.ivImageView);
        }

        @Override
        public int getItemCount() {
            return (mMovieList == null) ? 0 : mMovieList.size();
        }

        public void setMovieList(List<Movie> mMovie) {
            mMovieList = new ArrayList<Movie>();
            if (mMovie == null) {
                List<Movie> movies = new ArrayList<>();

                for (int i = 0; i < 2; i++) {
                    movies.add(new Movie());
                }
                mMovieList.addAll(movies);
            } else {
                mMovieList.addAll(mMovie);                             // NPE
                notifyDataSetChanged();
            }
        }
    }
}
