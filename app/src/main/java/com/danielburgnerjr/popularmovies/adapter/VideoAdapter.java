package com.danielburgnerjr.popularmovies.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielburgnerjr.popularmovies.R;
import com.danielburgnerjr.popularmovies.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private ArrayList<Video> mVideoList;
    private Callbacks mCallbacks;
    LayoutInflater mInflater;
    Context mContext;

    public interface Callbacks {
        void watch(Video video, int position);
    }

    public VideoAdapter(ArrayList<Video> videos, Callbacks cb) {
        mVideoList = videos;
        mCallbacks = cb;
    }

    @Override
    @NonNull
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int nViewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View vView = mInflater.inflate(R.layout.trailer_list, parent, false);
        return new VideoViewHolder(vView);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        final Video video = mVideoList.get(position);
        mContext = holder.vView.getContext();

        float paddingLeft = 0;
        if (position == 0) {
            paddingLeft = mContext.getResources().getDimension(R.dimen.detail_horizontal_padding);
        }

        float paddingRight = 0;
        if (position + 1 != getItemCount()) {
            paddingRight = mContext.getResources().getDimension(R.dimen.detail_horizontal_padding) / 2;
        }

        holder.vView.setPadding((int) paddingLeft, 0, (int) paddingRight, 0);

        holder.viVideo = video;

        String thumbnailUrl = "http://img.youtube.com/vi/" + video.getKey() + "/0.jpg";

        Picasso.get()
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .into(holder.ivThumbnailView);

        holder.vView.setOnClickListener(v -> mCallbacks.watch(video, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return (mVideoList == null) ? 0 : mVideoList.size();
    }

    public void addVideo(List<Video> mVideo) {
        mVideoList.clear();
        mVideoList.addAll(mVideo);
        notifyDataSetChanged();
    }

    public void setVideoList(List<Video> mVideo) {
        mVideoList.clear();
        mVideoList.addAll(mVideo);
        notifyDataSetChanged();
    }
}
