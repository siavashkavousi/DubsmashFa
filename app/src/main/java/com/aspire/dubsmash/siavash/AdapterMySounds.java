package com.aspire.dubsmash.siavash;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aspire.dubsmash.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sia on 10/3/15.
 */
public class AdapterMySounds extends RecyclerView.Adapter<AdapterMySounds.SoundsViewHolder> {
    private List<String> mSounds;
    private boolean isPlaying;
    private ImageButton mPlayingSoundButton;
    private MediaPlayer mMediaPlayer;

    public AdapterMySounds(List<String> sounds) {
        mSounds = sounds;
    }

    @Override
    public SoundsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sounds, parent, false);
        return new SoundsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SoundsViewHolder holder, int position) {
        String soundTitle = mSounds.get(position);
        holder.bindViews(soundTitle, null);
    }

    @Override
    public int getItemCount() {
        return mSounds.isEmpty() ? 1 : mSounds.size();
    }

    public void addData(List<String> data) {
        mSounds.addAll(data);
        notifyDataSetChanged();
    }

    public class SoundsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.title) TextView mTitle;
        @Bind(R.id.play_sound) ImageButton mPlaySound;

        public SoundsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindViews(String title, String path) {
            mTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == mPlaySound.getId()) {
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

//                File file = new File()
//                ActivityMain.sNetworkApi.downloadSingleData(mSound.getSoundUrl(), new Callback<Response>() {
//                    @Override public void success(Response response, Response response2) {
//                        try {
//                            Util.saveToFile(Util.getFromStream(response.getBody().in()), Util.createFile(Constants.PRIVATE_SOUND).getPath());
//                            if (mPlayingSoundButton == mPlaySound) {
//                                mPlaySound.setImageResource(R.drawable.ic_pause);
//                                mMediaPlayer = MediaPlayer.create(mContext, Uri.parse(Util.createFile(Constants.PRIVATE_SOUND).getPath()));
//                                mMediaPlayer.start();
//                                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                    @Override public void onCompletion(MediaPlayer mp) {
//                                        mPlaySound.setImageResource(R.drawable.ic_play);
//                                        mMediaPlayer.reset();
//                                        mMediaPlayer.release();
//                                        mMediaPlayer = null;
//                                        isPlaying = false;
//                                    }
//                                });
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override public void failure(RetrofitError error) {
//
//                    }
//                });
            }
        }
    }
}
