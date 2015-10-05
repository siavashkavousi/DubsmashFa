package com.aspire.dubsmash.hojjat;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

import java.io.IOException;

/**
 * Created by hojjat on 9/30/15.
 */
public class ActivityRecordSound extends AppCompatActivity {
    TextView title;
    TextView text;
    LinearLayout record;
    TextView timer;
    ImageButton mic;
    TextView startDone;

    MediaRecorder mediaRecorder;
    final String resultSoundPath = Constants.TEMP_SOUND_PATH;

    boolean recording;
    long recordingStartTime;
    long length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sound);
        initViews();
        setTexts();
        setListeners();
        initRecorder();
    }

    private void initViews() {
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        record = (LinearLayout) findViewById(R.id.record);
        timer = (TextView) findViewById(R.id.timer);
        mic = (ImageButton) findViewById(R.id.mic);
        startDone = (TextView) findViewById(R.id.start_done);
    }

    private void setTexts() {
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Regular, text, timer);
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Bold, startDone);
        Util.setText(text, "شروع را لمس کنید تا ضبط صدا آغاز شود", startDone, "شروع", timer, "10 ثانیه");
    }

    private void setListeners() {
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording) {
                    if (System.currentTimeMillis() - recordingStartTime > 1500)
                        finishRecording();
                } else
                    startRecording();
            }
        });
    }

    private void startRecording() {
        mic.setImageResource(R.drawable.mic_on);
        timer.setVisibility(View.VISIBLE);
        Util.setText(startDone, "تمام", text , "برای پایان ضبط تمام را لمس کنید");
        startDone.setTextColor(getResources().getColor(R.color.mic_on));
        recordingStartTime = System.currentTimeMillis();
        recording = true;
        mediaRecorder.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (recording) {
                    if ((length = System.currentTimeMillis() - recordingStartTime) < 10 * 1000) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Util.setText(timer, String.format("%.1f", (10000 - length) / (float)1000) + " ثانیه");
                            }
//                            new DecimalFormat("#.#").format((10000 - length) / (float)1000)
                        });
                    } else {
                        finishRecording();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void finishRecording() {
        recording = false;
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
//        startActivity(new Intent(this, ActivityRecordDub.class));
        Intent intent = new Intent(this, ActivitySendSound.class);
        intent.putExtra(Constants.SOUND_PATH, resultSoundPath);
        startActivity(intent);
        finish();
    }

    private void initRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(99999999);
        mediaRecorder.setAudioSamplingRate(99999999);
        mediaRecorder.setOutputFile(resultSoundPath);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecorder();
        mic.setImageResource(R.drawable.mic_off);
        startDone.setTextColor(getResources().getColor(R.color.mic_off));
        Util.setText(startDone, "شروع", text, "شروع را لمس کنید تا ضبط صدا آغاز شود");
    }
}