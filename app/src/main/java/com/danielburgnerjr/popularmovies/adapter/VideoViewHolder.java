package com.danielburgnerjr.popularmovies.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.danielburgnerjr.popularmovies.R;
import com.danielburgnerjr.popularmovies.model.Video;

import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class VideoViewHolder extends RecyclerView.ViewHolder {
    public final View vView;
    public ImageView ivThumbnailView;
    public Video viVideo;

    public VideoViewHolder(View vItemView) {
        super(vItemView);
        ivThumbnailView = vItemView.findViewById(R.id.trailer_thumbnail);
        ButterKnife.bind(this, vItemView);
        vView = vItemView;
    }
}
