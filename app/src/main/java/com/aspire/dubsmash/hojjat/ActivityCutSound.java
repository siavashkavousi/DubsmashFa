package com.aspire.dubsmash.hojjat;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

import java.io.IOException;

/**
 * Created by hojjat on 10/2/15.
 */
public class ActivityCutSound extends AppCompatActivity {
    String Tag = this.getClass().getName() + "-hojjii-";

    ImageButton back;
    TextView title;
    Button done;
    ImageButton playPause;
    HorizontalScrollView scrollView;
    ImageView musicImage;
    SeekBar start;
    SeekBar end;
    SeekBar progress;

    String sourcePath;
    String resPath = Constants.TEMP_SOUND_PATH;

    boolean recording;
    boolean shortInput = false;
    boolean cuttingIsDone;
    boolean goNextAsCuttingFinished;

    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_sound);
        sourcePath = getIntent().getStringExtra(Constants.SOUND_PATH);
        maximizeDeviceVolume();
        initMediaPlayer();
        initVewis();
        initViewState();
        setListeners();
    }

    private void setListeners() {
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (start.getProgress() > end.getProgress()) {
                    SeekBar temp = start;
                    start = end;
                    end = temp;
                }
            }
        };
        start.setOnSeekBarChangeListener(onSeekBarChangeListener);
        end.setOnSeekBarChangeListener(onSeekBarChangeListener);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNextAsCuttingFinished = true;
                if (!recording) {
                    if (cuttingIsDone)
                        Next();
                    else {
                        startProcess();
                    }
                }
            }
        });
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProcess();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void Next() {
        shutdownMediaPlayer();
        startActivity(new Intent(this, ActivitySendSound.class).putExtra(Constants.SOUND_PATH, resPath));
        finish();
    }

    private void initMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(sourcePath));
        } catch (Exception e) {
            //problem opening the file
            e.printStackTrace();
        }
        if (mediaPlayer.getDuration() <= Constants.FILES_MAX_LENGTH)
            shortInput = true;
        Log.d(Tag, "shortInput: " + shortInput);
    }

    private void shutdownMediaPlayer() {
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void shutdownMediaRecorder() {
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    @Override
    public void onBackPressed() {
        if (!recording) {
            shutdownMediaPlayer();
            finish();
        }
    }

    private void maximizeDeviceVolume() {
        AudioManager mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int origionalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    private void startProcess() {
        if (getSelectedRangeLength() < Constants.FILES_MIN_LENGTH) {
            Toast.makeText(ActivityCutSound.this, "بازه انتخاب شده خیلی کوتاه است", Toast.LENGTH_LONG).show();
            return;
        }
        recording = true;
        initMediaRecorder();
        mediaPlayer.seekTo(getSelectedRangeStart());
        mediaPlayer.start();
        setupProgressBar(getSelectedRangeStart(), getSelectedRangeLength());
        mediaRecorder.start();
        setViewStateOnRecording();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(getSelectedRangeLength());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishProcess();
                    }
                });
            }
        }).start();
    }

    private void setupProgressBar(final int meidaStartPos, final int playingLength) {
        final int offset = start.getProgress();
        final int range = getSelectedRangeLength();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (recording) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(offset + (int) (((float) (mediaPlayer.getCurrentPosition() - meidaStartPos) / playingLength) * range));
                            progress.setVisibility(View.VISIBLE);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void finishProcess() {
        recording = false;
        mediaPlayer.stop();
        mediaRecorder.stop();
        shutdownMediaRecorder();
        shutdownMediaPlayer();
        initMediaPlayer();
        cuttingIsDone = true;
        setViewStateOnRecordingDone();
        if (goNextAsCuttingFinished)
            Next();
    }

    private void setViewStateOnRecordingDone() {
        playPause.setEnabled(true);
        progress.setVisibility(View.INVISIBLE);
    }

    private void setViewStateOnRecording() {
        playPause.setEnabled(false);
//        progressbar.setVisibility(View.VISIBLE);
    }


    private void initViewState() {
        initMusicImage();
        initSeekbars();
        setConsViews();
    }

    private void setConsViews() {
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Bold, this.title, done);
        Util.setText(this.title, "بریدن صدا", done, "ادامه");
    }

    private void initSeekbars() {
        start.setMax(shortInput ? mediaPlayer.getDuration() : Constants.FILES_MAX_LENGTH);
        end.setMax(start.getMax());
        start.setProgress(shortInput ? 0 : (int) (0.35 * start.getMax()));
        end.setProgress(shortInput ? end.getMax() : (int) (0.7 * end.getMax()));
        progress.setEnabled(false);
        progress.setMax(start.getMax());
    }

    private void initMusicImage() {
        setMusicImageSize();
        setMusicImageDrawable();
    }

    private int getDeviceWidthPx() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private void setMusicImageSize() {
        ViewGroup.LayoutParams layoutParams = musicImage.getLayoutParams();
        if (shortInput)
            layoutParams.width = getDeviceWidthPx();
        else
            layoutParams.width = (int) ((mediaPlayer.getDuration() / (float) Constants.FILES_MAX_LENGTH) * getDeviceWidthPx());
        layoutParams.height = BitmapFactory.decodeResource(getResources(), R.drawable.wave).getHeight();
        musicImage.setLayoutParams(layoutParams);
    }

    private void setMusicImageDrawable() {
        BitmapDrawable tileMe = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.wave));
        tileMe.setTileModeX(Shader.TileMode.REPEAT);
        musicImage.setBackgroundDrawable(tileMe);
    }

    private int getSelectedRangeStart() {
        if (shortInput)
            return start.getProgress();
        else
            return (int) (((float) scrollView.getScrollX() / musicImage.getWidth()) * mediaPlayer.getDuration()) + start.getProgress();
    }

    private int getSelectedRangeLength() {
        return end.getProgress() - start.getProgress();
    }

    private void initVewis() {
        playPause = (ImageButton) findViewById(R.id.play_pause);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        musicImage = (ImageView) findViewById(R.id.music_image);
        start = (SeekBar) findViewById(R.id.begin);
        end = (SeekBar) findViewById(R.id.end);
        done = (Button) findViewById(R.id.done);
        progress = (SeekBar) findViewById(R.id.progress);
        title = (TextView) findViewById(R.id.title);
        back = (ImageButton) findViewById(R.id.back);
    }

    private void initMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(99999999);
        mediaRecorder.setAudioSamplingRate(99999999);
        mediaRecorder.setOutputFile(resPath);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
