package com.danielburgnerjr.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class Video implements Parcelable {
    @SerializedName("id")
    private String strID;
    @SerializedName("key")
    private String strKey;
    @SerializedName("name")
    private String strName;
    @SerializedName("site")
    private String strSite;
    @SerializedName("size")
    private String strSize;

    private Video() {}

    public String getKey() {
        return strKey;
    }

    public static final Parcelable.Creator<Video> CREATOR = new Creator<Video>() {
        public Video createFromParcel(Parcel source) {
            Video video = new Video();
            video.strID = source.readString();
            video.strKey = source.readString();
            video.strName = source.readString();
            video.strSite = source.readString();
            video.strSize = source.readString();
            return video;
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(strID);
        parcel.writeString(strKey);
        parcel.writeString(strName);
        parcel.writeString(strSite);
        parcel.writeString(strSize);
    }

    public class VideoResult {
        @SerializedName("results")
        private List<Video> videoList = new ArrayList<>();

        public List<Video> getVideoList() {
            return videoList;
        }
    }
}
