package com.aspire.dubsmash.hojjat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.aspire.dubsmash.siavash.ActivityMain;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by hojjat on 9/29/15.
 */
public class ActivityProcessDub extends AppCompatActivity {
    private String videoPath = Constants.TEMP_VIDEO_PATH;
    private String audioPath;
    private String resultDubPath = Util.getNextDubPath();

    Button done;
    LinearLayout tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_dub);
        initViews();
        initConsViews();
        setListeners();
        audioPath = getIntent().getStringExtra(Constants.SOUND_PATH);
        if (!getIntent().getStringExtra(Constants.DUB_PATH).equals(Constants.NOT_ASSIGNED))
            resultDubPath = getIntent().getStringExtra(Constants.DUB_PATH);
        new ProcessDub().execute();
    }

    private void setListeners() {
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityProcessDub.this , ActivityRecordDub.class).putExtra(Constants.SOUND_PATH, audioPath).putExtra(Constants.DUB_PATH , resultDubPath));
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : upload video done notification and other stuff
                ActivityMain.sNetworkApi.uploadVideo(Util.getUserId(ActivityProcessDub.this), new TypedFile("video/*", new File(resultDubPath)), null, new TypedFile("image/*", new File(Util.getVideoThumbnailFilePath(resultDubPath))), null, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                Toast.makeText(ActivityProcessDub.this, "داب شما با موفقیت ارسال شد", Toast.LENGTH_LONG).show();
                // TODO : go to FragmentMyDubs from ActivityMain
            }
        });
    }

    private void initConsViews() {
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Bold , done, tryAgain);
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Regular, findViewById(R.id.try_again_text));
        Util.setText(done , "آپلود به برنامه", findViewById(R.id.try_again_text), "خوب نشده؟! دوباره امتحان کن!");
    }

    private void initViews() {
        done = (Button) findViewById(R.id.done);
        tryAgain = (LinearLayout) findViewById(R.id.try_again);
    }

    class ProcessDub extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ActivityProcessDub.this);
            progressDialog.setMessage("در حال پردازش داب شما!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Movie movie = MovieCreator.build(videoPath);
                Movie audio = new MovieCreator().build(audioPath);
                Track a = audio.getTracks().get(0);
                movie.addTrack(a);
                Container mp4file = new DefaultMp4Builder().build(movie);
                FileChannel fc = new FileOutputStream(new File(resultDubPath)).getChannel();
                mp4file.writeContainer(fc);
                fc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            playResult();
        }
    }

    private void playResult() {
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath(resultDubPath);
//        videoView.setBackgroundDrawable(new BitmapDrawable(ThumbnailUtils.createVideoThumbnail(resultDubPath,
//                MediaStore.Images.Thumbnails.MINI_KIND)));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        videoView.start();

    }
}
