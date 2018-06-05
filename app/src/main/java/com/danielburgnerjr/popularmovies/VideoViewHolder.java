package com.danielburgnerjr.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VideoViewHolder extends RecyclerView.ViewHolder {
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
