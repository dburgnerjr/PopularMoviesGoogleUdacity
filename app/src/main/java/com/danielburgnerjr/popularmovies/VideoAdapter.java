package com.danielburgnerjr.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private ArrayList<Video> mVideoList;
    private Callbacks mCallbacks;
    private LayoutInflater mInflater;
    private Context mContext;

    public interface Callbacks {
        void watch(Video video, int position);
    }

    public VideoAdapter(ArrayList<Video> videos, Callbacks cb) {
        mVideoList = videos;
        mCallbacks = cb;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int nViewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View vView = mInflater.inflate(R.layout.trailer_list, parent, false);
        final VideoViewHolder vvhViewHolder = new VideoViewHolder(vView);
        return vvhViewHolder;
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

        Picasso.with(mContext)
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .into(holder.ivThumbnailView);

        holder.vView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.watch(video, holder.getAdapterPosition());
            }
        });
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
