package com.danielburgnerjr.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Review implements Parcelable {

    @SerializedName("id")
    private String strID;
    @SerializedName("author")
    private String strAuthor;
    @SerializedName("content")
    private String strContent;
    @SerializedName("url")
    private String strURL;

    public String getContent() {
        return strContent;
    }

    public String getAuthor() {
        return strAuthor;
    }

    public String getUrl() {
        return strURL;
    }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            Review review = new Review();
            review.strID = source.readString();
            review.strAuthor = source.readString();
            review.strContent = source.readString();
            review.strURL = source.readString();
            return review;
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(strID);
        parcel.writeString(strAuthor);
        parcel.writeString(strContent);
        parcel.writeString(strURL);
    }

    public class ReviewResult {

        @SerializedName("results")
        private List<Review> reviewList = new ArrayList<>();

        public List<Review> getReviews() {
            return reviewList;
        }
    }

}
