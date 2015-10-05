package com.aspire.dubsmash.siavash;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.aspire.dubsmash.R;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sia on 9/30/15.
 */
public class AdapterVideos extends RecyclerView.Adapter<AdapterVideos.VideosViewHolder> {
    private List<Video> mVideos;

    public AdapterVideos(List<Video> videos) {
        mVideos = videos;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        holder.bindViews(mVideos.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideos.isEmpty() ? 0 : mVideos.size();
    }

    public void addData(List<Video> data) {
        mVideos = data;
        notifyDataSetChanged();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.item_video) VideoView videoItem;

        private Video mVideo;

        public VideosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setListeners();
        }

        private void bindViews(Video video) {
            mVideo = video;
            ActivityMain.sNetworkApi.downloadSingleData(video.getVideoThumbnail(), new Callback<Response>() {
                @Override public void success(Response response, Response response2) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.getBody().in());
                        videoItem.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override public void failure(RetrofitError error) {

                }
            });
        }

        private void setListeners() {
            videoItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == videoItem.getId()) {
                videoItem.setVideoPath(mVideo.getVideoUrl());
                videoItem.start();
            }
        }
    }
}

