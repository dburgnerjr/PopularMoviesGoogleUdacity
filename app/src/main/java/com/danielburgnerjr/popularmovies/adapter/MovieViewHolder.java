package com.danielburgnerjr.popularmovies.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.danielburgnerjr.popularmovies.R;

@SuppressWarnings("WeakerAccess")
public class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivImageView;
    public MovieViewHolder(View vItemView) {
        super(vItemView);
        ivImageView = vItemView.findViewById(R.id.ivImageView);
    }
}

