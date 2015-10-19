package com.aspire.dubsmash.siavash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.DatabaseUtil;
import com.aspire.dubsmash.util.Util;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observer;

/**
 * Created by sia on 9/30/15.
 */
public class AdapterVideos extends RecyclerView.Adapter<AdapterVideos.VideosViewHolder> {
    private static final String TAG = AdapterVideos.class.getSimpleName();
    private List<Video> mVideos;
    private Context mContext;
    private int counter = 0;

    public AdapterVideos(List<Video> videos) {
        mVideos = videos;
    }

    @Override public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos, parent, false);
        mContext = parent.getContext();
        return new VideosViewHolder(view);
    }

    @Override public void onBindViewHolder(VideosViewHolder holder, int position) {
        holder.bindViews(mVideos.get(position));
    }

    @Override public int getItemCount() {
        return mVideos.isEmpty() ? 0 : mVideos.size();
    }

    public void addData(List<Video> data) {
        mVideos = data;
        notifyDataSetChanged();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder implements Observer {
        @Bind(R.id.item_image) ImageButton imageItem;
        @Bind(R.id.item_progress_bar) ProgressBar progressBar;

        private Video mVideo;
        private String mName = "video" + counter + ".mp4";
        private String mPath = Constants.TEMP_PATH + File.separator + mName;

        public VideosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindViews(Video video) {
            mVideo = video;
            Glide.with(mContext)
                    .load(Util.BASE_URL + File.separator + video.getVideoThumbnail())
                    .crossFade()
                    .into(imageItem);
        }

        @OnClick(R.id.item_image) void onClick() {
            imageItem.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            RealmVideo video = containsVideoInDatabase();
            if (video == null) {
                ActivityMain.sNetworkApi.downloadSingleData(mVideo.getVideoUrl(), new Callback<Response>() {
                    @Override public void success(Response response, Response response2) {
                        try {
                            Util.saveToFileAsync(mContext, response.getBody().in(), mPath, VideosViewHolder.this);
                            DatabaseUtil.writeObjectToDatabase(new RealmVideo(mVideo.getVideoId(), mPath));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override public void failure(RetrofitError error) {

                    }
                });
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                sendVideoIntent(mPath);
            }
        }

        @Nullable private RealmVideo containsVideoInDatabase() {
            RealmVideo video = ApplicationBase.getRealm().where(RealmVideo.class).contains("id", mVideo.getVideoId()).findFirst();
            if (video != null) {
                if (Constants.IS_DEBUG)
                    Log.d(TAG, "found video with id : " + video.getId() + " and path : " + video.getPath());
                return video;
            }
            return null;
        }

        @Override public void onCompleted() {
            sendVideoIntent(mPath);
            counter++;
        }

        @Override public void onError(Throwable e) {

        }

        @Override public void onNext(Object o) {

        }

        private void sendVideoIntent(String path) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
            mContext.startActivity(intent);
        }
    }
}

