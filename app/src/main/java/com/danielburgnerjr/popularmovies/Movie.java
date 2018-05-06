package com.danielburgnerjr.popularmovies;

/**
 * Created by dburgnerjr on 5/5/18.
 */

public class Movie {
    private String strTitle;
    private String strPoster;
    private String strDescription;
    private String strUserRating;
    private String strReleaseDate;
    private String strBackdrop;

    private String getTitle() {
        return strTitle;
    }

    public void setTitle(String strT) {
        this.strTitle = strT;
    }

    public String getPoster() {
        return "http://t2.gstatic.com/images?q=tbn:ANd9GcQW3LbpT94mtUG1PZIIzJNxmFX399wr_NcvoppJ82k7z99Hx6in";
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

    public String getUserRating() {
        return strUserRating;
    }

    public void setReleaseDate(String strRD) {
        this.strReleaseDate = strRD;
    }

    public String getReleaseDate() {
        return strReleaseDate;
    }

    public void setUserRating(String strUR) {
        this.strUserRating = strUR;
    }

    public String getBackdrop() {
        return strBackdrop;
    }

    public void setBackdrop(String strB) {
        this.strBackdrop = strB;
    }
}
