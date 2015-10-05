package com.aspire.dubsmash.siavash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.hojjat.ActivityRecordDub;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.PersianReshape;
import com.aspire.dubsmash.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sia on 9/30/15.
 */
public class AdapterSounds extends RecyclerView.Adapter<AdapterSounds.SoundsViewHolder> {
    private static final String TAG = AdapterSounds.class.getSimpleName();
    private List<Sound> mSounds;
    private boolean isPlaying;
    private ImageButton mPlayingSoundButton;
    private MediaPlayer mMediaPlayer;

    public AdapterSounds(List<Sound> sounds) {
        mSounds = sounds;
    }

    @Override
    public SoundsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sounds, parent, false);
        return new SoundsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SoundsViewHolder holder, int position) {
        holder.bindViews(mSounds.get(position));
    }

    @Override
    public int getItemCount() {
        return mSounds.isEmpty() ? 1 : mSounds.size();
    }

    public void addData(List<Sound> data) {
        mSounds = data;
        Log.d(TAG, mSounds.size() + "");
        notifyDataSetChanged();
    }

    public class SoundsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.item) RelativeLayout mItem;
        @Bind(R.id.title) TextView mTitle;
        @Bind(R.id.uploaded_by) TextView mUploadedBy;
        @Bind(R.id.like) ImageButton mLikeButton;
        @Bind(R.id.play_sound) ImageButton mPlaySound;

        private Context mContext;
        private Sound mSound;
        private boolean isLiked;

        public SoundsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
            isLiked = false;
            setListeners();
        }

        private void bindViews(Sound sound) {
            mTitle.setText(sound.getSoundTitle());
            mUploadedBy.setText(sound.getSoundSendername());
            mSound = sound;
        }

        private void setListeners() {
            mItem.setOnClickListener(this);
            mLikeButton.setOnClickListener(this);
            mPlaySound.setOnClickListener(this);
        }

        @OnClick(R.id.go_to_dubmaker) void goToDubMaker() {

        }

        @Override public void onClick(View v) {
            int id = v.getId();
            if (id == mLikeButton.getId()) {
                if (!isLiked) {
                    mLikeButton.setImageResource(R.drawable.favorite_selected);
                    ActivityMain.sNetworkApi.getSoundLikes(Util.getUserId(mContext), mSound.getSoundId(), new Callback<Response>() {
                        @Override public void success(Response response, Response response2) {

                        }

                        @Override public void failure(RetrofitError error) {

                        }
                    });
                    isLiked = true;
                } else {
                    mLikeButton.setImageResource(R.drawable.favorite_unselected);
                    isLiked = false;
                }
            } else if (id == mPlaySound.getId()) {
                if (isPlaying) {
                    if (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying())
                            mMediaPlayer.stop();
                        mMediaPlayer.reset();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                    mPlayingSoundButton.setImageResource(R.drawable.ic_play);
                    mPlayingSoundButton = mPlaySound;
                    mPlaySound.setImageResource(R.drawable.download);
                } else {
                    mPlayingSoundButton = mPlaySound;
                    mPlaySound.setImageResource(R.drawable.download);
                    isPlaying = true;
                }
                ActivityMain.sNetworkApi.downloadSingleData(mSound.getSoundUrl(), new Callback<Response>() {
                    @Override public void success(Response response, Response response2) {
                        try {
                            Util.saveToFile(Util.getFromStream(response.getBody().in()), new File(Constants.TEMP_SOUND_PATH).getPath());
                            if (mPlayingSoundButton == mPlaySound) {
                                mPlaySound.setImageResource(R.drawable.ic_pause);
                                mMediaPlayer = MediaPlayer.create(mContext, Uri.parse(Constants.TEMP_SOUND_PATH));
                                mMediaPlayer.start();
                                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override public void onCompletion(MediaPlayer mp) {
                                        mPlaySound.setImageResource(R.drawable.ic_play);
                                        mMediaPlayer.reset();
                                        mMediaPlayer.release();
                                        mMediaPlayer = null;
                                        isPlaying = false;
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override public void failure(RetrofitError error) {

                    }
                });
            } else if (id == mItem.getId()) {
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage(PersianReshape.reshape("کمی صبر کنید"));
                progressDialog.setCancelable(false);
                progressDialog.show();
                ActivityMain.sNetworkApi.downloadSingleData(mSound.getSoundUrl(), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        progressDialog.dismiss();
                        try {
                            Util.saveToFile(Util.getFromStream(response.getBody().in()), new File(Constants.TEMP_SOUND_PATH).getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mContext.startActivity(new Intent(mContext, ActivityRecordDub.class).putExtra(Constants.SOUND_PATH, Constants.TEMP_SOUND_PATH).putExtra(Constants.DUB_PATH, Constants.NOT_ASSIGNED));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "خطا", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
