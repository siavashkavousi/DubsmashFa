package com.aspire.dubsmash.hojjat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.siavash.ActivityMain;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by hojjat on 10/3/15.
 */
public class ActivitySendSound extends AppCompatActivity {
    Button done;
    ImageButton playPause;
    SeekBar progressbar;
    EditText soundTitle;
    EditText tags;

    String audioPath;
    String finalSoundPath;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sound);
        audioPath = getIntent().getStringExtra(Constants.SOUND_PATH);
        initViews();
        initConsViews();
        setListeners();
    }

    private void setListeners() {
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null)
                    startPlaying();
                else finishPlaying();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundTitle.getText().toString().trim().length() > 0) {
                    Next();
                } else {
                    Toast.makeText(ActivitySendSound.this, "عنوان صدا را وارد کنید!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Next() {
//        addMetadata(); problem with samsung
        saveToMySounds();
        sendSoundToServer();
        Intent intent = new Intent(this, ActivityMain.class);
        intent.putExtra(Constants.WHICH_FRAGMENT, Constants.FRAGMENT_MY_SOUNDS);
        intent.putExtra(Constants.SOUND_PATH, audioPath);
        startActivity(intent);
        finish();
    }

    private void sendSoundToServer() {
        ActivityMain.sNetworkApi.uploadSound(Util.getUserId(this), new TypedFile("audio/*", new File(finalSoundPath)), soundTitle.getText().toString(), tags.getText().toString(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void saveToMySounds() {
        try {
            InputStream in = new FileInputStream(audioPath);
            finalSoundPath = Util.getNextSoundPath(soundTitle.getText().toString().trim());
            OutputStream out = new FileOutputStream(finalSoundPath);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void addMetadata() {
//        try {
//            TagOptionSingleton.getInstance().setAndroid(true);
//            AudioFile audioFile = AudioFileIO.read(new File(audioPath));
//            Tag newTag = audioFile.getTag();
//            newTag.setField(FieldKey.TITLE, soundTitle.getText().toString());
//            newTag.setField(FieldKey.ARTIST, Util.getUsersName(ActivitySendSound.this));
//            audioFile.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(ActivitySendSound.this, "Exception!   1", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void startPlaying() {
        playPause.setImageResource(R.drawable.pause_big);
        mediaPlayer = MediaPlayer.create(ActivitySendSound.this, Uri.parse(audioPath));
        mediaPlayer.start();
        progressbar.setMax(mediaPlayer.getDuration());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                progressbar.setProgress(mediaPlayer.getCurrentPosition());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finishPlaying();
            }
        });
    }

    private void finishPlaying() {
        playPause.setImageResource(R.drawable.play_big);
        progressbar.setProgress(0);
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        Character c;
    }

    private void initConsViews() {
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Bold, done, findViewById(R.id.title), findViewById(R.id.tags));
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Regular, soundTitle, tags);
        Util.setText(done, "تایید", findViewById(R.id.title), "ارسال صدا");
    }

    private void initViews() {
        done = (Button) findViewById(R.id.done);
        playPause = (ImageButton) findViewById(R.id.play_pause);
        progressbar = (SeekBar) findViewById(R.id.progress);
        soundTitle = (EditText) findViewById(R.id.sound_title);
        tags = (EditText) findViewById(R.id.tags_input);
    }
}
