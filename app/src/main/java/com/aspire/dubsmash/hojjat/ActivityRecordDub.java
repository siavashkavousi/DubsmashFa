package com.aspire.dubsmash.hojjat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import java.io.IOException;
import java.util.List;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

public class ActivityRecordDub extends AppCompatActivity implements SurfaceHolder.Callback {
    public static final String TAG = "hojjat";
    Toolbar toolbar;
    ImageButton switchCamera;

    private final String resultVideoPath = Constants.TEMP_VIDEO_PATH;
    private String audioPath;

    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private ToggleButton toggleButton;
    private SeekBar progressBar;

    Camera.Size prefferedVideoSize;
    Camera.Size prefferedPreviewSize;

    private boolean initSuccesful;
    private boolean processCanceled;
    private boolean recording;

    private int currentCameraId = 1; //front camera as default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_dub);
        audioPath = getIntent().getStringExtra(Constants.SOUND_PATH);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        switchCamera = (ImageButton) findViewById(R.id.switch_Camera);
        switchCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording)
                    switchCamera();
            }
        });
        PackageManager pm = getPackageManager();
        if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
            switchCamera.setVisibility(View.GONE);
            currentCameraId = 0; // main camera
        }

        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Bold, toggleButton, findViewById(R.id.title));
        Util.setText(findViewById(R.id.title), "ظبط داب جدید");

        mediaPlayer = MediaPlayer.create(this, Uri.parse(audioPath));
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        progressBar = (SeekBar) findViewById(R.id.seekBar);

        toggleButton = (ToggleButton) findViewById(R.id.toggleRecordingButton);
        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    mediaPlayer.start();
                    mediaRecorder.start();
                    recording = true;
                    setupProgressBar();
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(mediaPlayer.getDuration());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!processCanceled) {
                                shutdown();
                                ActivityRecordDub.this.startActivity(new Intent(ActivityRecordDub.this, ActivityProcessDub.class).putExtra(Constants.SOUND_PATH, Constants.TEMP_SOUND_PATH).putExtra(Constants.DUB_PATH, getIntent().getStringExtra(Constants.DUB_PATH)));
                                finish();
                            }
                        }
                    }.start();
                } else {
                    cancel();
                }
            }
        });
    }

    private void prepareToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    private void cancel() {
        processCanceled = true;
        shutdown();
        finish();
    }

    private void setupProgressBar() {
        progressBar.setMax(mediaPlayer.getDuration());
        new Thread() {
            public void run() {
                while (mediaPlayer != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mediaPlayer != null && mediaPlayer.isPlaying())
                                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /* Init the MediaRecorder, the order the methods are called is vital to
  * its correct functioning */
    private void initRecorder(Surface surface) throws IOException {
        if (camera == null) {
            camera = Camera.open(currentCameraId);
            camera.setDisplayOrientation(90);
            Camera.Parameters parameters = camera.getParameters();
            prefferedVideoSize = getPrefferedVideoSize(parameters);
            prefferedPreviewSize = getPrefferedPreviewSize(parameters);
            parameters.setPreviewSize(prefferedPreviewSize.width, prefferedPreviewSize.height);
            camera.setParameters(parameters);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            camera.unlock();
        }

        if (mediaRecorder == null) mediaRecorder = new MediaRecorder();
//        mediaRecorder.setPreviewDisplay(surface);
        mediaRecorder.setCamera(camera);
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        //       mMediaRecorder.setOutputFormat(8);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

//        mediaRecorder.setVideoEncodingBitRate(2048 * 10000);
        mediaRecorder.setVideoEncodingBitRate(3000000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(prefferedVideoSize.width, prefferedVideoSize.height);
//        mediaRecorder.setVideoSize(640, 480);
        if (currentCameraId == 1)
            mediaRecorder.setOrientationHint(270);
        else
            mediaRecorder.setOrientationHint(90);
        mediaRecorder.setOutputFile(resultVideoPath);
        mediaRecorder.setPreviewDisplay(surface);
//        setSurfaceViewSize();
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        initSuccesful = true;
        Log.d(TAG, "initializing done!");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (!initSuccesful)
                initRecorder(this.holder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        shutdown();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    private void shutdown() {
        shutDownMediaPlayer();
        shutDownMediaRecorder();
        shutDownCamera();
        initSuccesful = false;
    }

    private void shutDownMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }

    private void shutDownMediaRecorder() {
        if (mediaRecorder != null) {
            if (recording)
                mediaRecorder.stop();
            recording = false;
            mediaRecorder.reset();
            mediaRecorder.release();
        }
        mediaRecorder = null;
    }

    private void shutDownCamera() {
        if (camera != null) {
            camera.lock();
            camera.stopPreview();
            camera.release();
        }
        camera = null;
    }

    private void switchCamera() {
        shutDownCamera();
        shutDownMediaRecorder();
        currentCameraId = (currentCameraId + 1) % 2;
        try {
            initRecorder(this.holder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setProgress(0);
        mediaPlayer = MediaPlayer.create(this, Uri.parse(audioPath));
        toggleButton.setChecked(false);
    }

    private Camera.Size getPrefferedVideoSize(Camera.Parameters parameters) {
        List<Camera.Size> supportedVideoSizes = parameters.getSupportedVideoSizes();
        if (supportedVideoSizes != null)
            return getPrefferedSize(supportedVideoSizes);
        else return getPrefferedPreviewSize(parameters);
    }

    private Camera.Size getPrefferedPreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        return getPrefferedSize(supportedPreviewSizes);
    }

    private Camera.Size getPrefferedSize(List<Camera.Size> sizes) {
        float[] ratios = new float[sizes.size()];
        float width, height;
        for (int i = 0; i < sizes.size(); i++) {
            width = sizes.get(i).width;
            height = sizes.get(i).height;
            Log.d(TAG, "supported Sizes >> width : " + width + "     height : " + height);
            ratios[i] = (width > height) ? width / height : height / width;
        }
        return sizes.get(getIndexOfMin(ratios));
    }

    private int getIndexOfMin(float[] items) {
        float temp = 99;
        int res = 0;
        for (int i = 0; i < items.length; i++)
            if (items[i] < temp) {
                temp = items[i];
                res = i;
            }
        return res;
    }

    private void setSurfaceViewSize() {
        surfaceView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    surfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    surfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
                lp.height = (int) ((float) prefferedPreviewSize.height / prefferedPreviewSize.width * lp.width);
                lp.width = (int) ((float) prefferedPreviewSize.height / prefferedPreviewSize.width * lp.height);
                surfaceView.setLayoutParams(lp);
            }
        });
    }
}