package com.aspire.dubsmash.hojjat;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

/**
 * Created by hojjat on 10/4/15.
 */
public class ActivityAddSound extends AppCompatActivity {
    Button record;
    Button import_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sound);
        initViews();
        initConsViews();
        setListenrs();
    }

    private void initConsViews() {
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Bold, record, import_);
        Util.setText(record, "ضبط صدا", import_, "انتخاب از آهنگ ها");
    }

    private void setListenrs() {
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityAddSound.this, ActivityRecordSound.class));
            }
        });

        import_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "انتخاب فایل صوتی"), 1);
            }
        });
    }

    private void initViews() {
        record = (Button) findViewById(R.id.record);
        import_ = (Button) findViewById(R.id.import_);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                startActivity(new Intent(ActivityAddSound.this, ActivityCutSound.class).putExtra(Constants.SOUND_PATH, getRealPathFromURI(uri)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
