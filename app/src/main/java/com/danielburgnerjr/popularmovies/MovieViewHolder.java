package com.danielburgnerjr.popularmovies;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

@SuppressWarnings("WeakerAccess")
public class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivImageView;
    public MovieViewHolder(View vItemView) {
        super(vItemView);
        ivImageView = vItemView.findViewById(R.id.ivImageView);
    }
}

