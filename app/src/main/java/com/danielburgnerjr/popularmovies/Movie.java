package com.danielburgnerjr.popularmovies;

/**
 * Created by dburgnerjr on 5/5/18.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie implements Parcelable {
    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    @SerializedName("id")
    private String strId;

    @SerializedName("title")
    private String strTitle;

    @SerializedName("poster_path")
    private String strPoster;

    @SerializedName("overview")
    private String strDescription;

    @SerializedName("backdrop_path")
    private String strBackdrop;

    @SerializedName("release_date")
    private String strReleaseDate;

    @SerializedName("vote_average")
    private double dUserRating;

    private boolean isFavorite;

    public Movie() {}

    public Movie(String id, String originalTitle, String overview,
                     String posterPath, String releaseDate, double voteAverage, boolean isFavorite) {
        this.strTitle = originalTitle;
        this.strDescription = overview;
        this.strPoster = posterPath;
        this.strReleaseDate = releaseDate;
        this.dUserRating = voteAverage;
        this.strId = id;
        this.isFavorite = isFavorite;
    }
    protected Movie(Parcel in) {
        strId = in.readString();
        strTitle = in.readString();
        strPoster = in.readString();
        strDescription = in.readString();
        strBackdrop = in.readString();
        strReleaseDate = in.readString();
        dUserRating = in.readDouble();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) { return new Movie(in); }

        @Override
        public Movie[] newArray(int nSize) { return new Movie[nSize]; }
    };

    public String getId() {
        return strId;
    }

    public void setId(String strI) {
        this.strId = strI;
    }

    public String getTitle() {
        return strTitle;
    }

    public void setTitle(String strT) {
        this.strTitle = strT;
    }

    public String getPoster() {
        return TMDB_IMAGE_PATH + strPoster;
    }

    public void setPoster(String strP) {
        this.strPoster = strP;
    }

    public String getDescription() {
        return strDescription;
    }

    public void setDescription(String strD) {
        this.strDescription = strD;
    }

    public String getBackdrop() {
        return TMDB_IMAGE_PATH + strBackdrop;
    }

    public void setBackdrop(String strB) {
        this.strBackdrop = strB;
    }

    public void setReleaseDate(String strRD) {
        this.strReleaseDate = strRD;
    }

    public String getReleaseDate() {
        return strReleaseDate;
    }

    public void setUserRating(double dUR) {
        this.dUserRating = dUR;
    }

    public double getUserRating() {
        return dUserRating;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean bFavorite) {
        isFavorite = bFavorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parP, int nI) {
        parP.writeString(strId);
        parP.writeString(strTitle);
        parP.writeString(strPoster);
        parP.writeString(strDescription);
        parP.writeString(strBackdrop);
        parP.writeString(strReleaseDate);
        parP.writeDouble(dUserRating);
        parP.writeByte((byte) (isFavorite ? 1 : 0));
    }

    public static class MovieResult {
        @SerializedName("results")
        private List<Movie> mResults;

        public List<Movie> getResults() {
            return mResults;
        }

        public int getSize() {
            return mResults.size();
        }
    }
}
