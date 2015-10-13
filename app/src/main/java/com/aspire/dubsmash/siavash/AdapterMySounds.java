package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.hojjat.ActivityRecordDub;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sia on 10/3/15.
 */
public class AdapterMySounds extends RecyclerView.Adapter<AdapterMySounds.SoundsViewHolder> {
    private List<String> mSoundsPath, mSoundsTitle;
    private boolean isPlaying;
    private ImageButton mPlayingSoundButton;
    private MediaPlayer mMediaPlayer;
    private Fragment mFragment;

    public AdapterMySounds(Fragment fragment, List<String> soundsPath, List<String> soundsTitle) {
        mSoundsPath = soundsPath;
        mSoundsTitle = soundsTitle;
        mFragment = fragment;
    }

    @Override
    public SoundsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mysounds, parent, false);
        return new SoundsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SoundsViewHolder holder, int position) {
        String soundTitle = mSoundsTitle.get(position);
        String soundPath = mSoundsPath.get(position);
        holder.bindViews(soundTitle, soundPath);
    }

    @Override
    public int getItemCount() {
        return mSoundsPath.isEmpty() ? 1 : mSoundsPath.size();
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public class SoundsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.item) RelativeLayout mItem;
        @Bind(R.id.title) TextView mTitle;
        @Bind(R.id.play_sound) ImageButton mPlaySound;

        private Context mContext;
        private String mPath;

        public SoundsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
            setListeners();
        }

        private void bindViews(String title, String path) {
            mTitle.setText(title);
            mPath = path;
        }

        private void setListeners() {
            mPlaySound.setOnClickListener(this);
            mItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == mPlaySound.getId()) {
                if (isPlaying) {
                    Util.stopAndReleaseMediaPlayer(mMediaPlayer);
                    mPlayingSoundButton.setImageResource(R.drawable.ic_play);
                    mPlayingSoundButton = mPlaySound;
                    mPlaySound.setImageResource(R.drawable.download);
                } else {
                    mPlayingSoundButton = mPlaySound;
                    mPlaySound.setImageResource(R.drawable.download);
                    isPlaying = true;
                }

                if (mPlayingSoundButton == mPlaySound) {
                    mPlaySound.setImageResource(R.drawable.ic_pause);
                    mMediaPlayer = MediaPlayer.create(mContext, Uri.parse(mPath));
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
            } else if (id == mItem.getId()) {
                mContext.startActivity(new Intent(mContext, ActivityRecordDub.class).putExtra(Constants.SOUND_PATH, mPath).putExtra(Constants.DUB_PATH, Constants.NOT_ASSIGNED));
            }
        }
    }
}
