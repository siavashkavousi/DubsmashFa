package com.aspire.dubsmash.hojjat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

/**
 * Created by hojjat on 9/30/15.
 */
public class ActivityMainTokhomV extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tokhom_v);
        if (Util.isFirstRun(this)) {
            startActivity(new Intent(this, ActivityFirstRun.class));
            finish();
        }
        Util.createDirectories();

        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMainTokhomV.this, ActivityRecordDub.class).putExtra(Constants.SOUND_PATH, Constants.TEMP_SOUND_PATH).putExtra(Constants.DUB_PATH, Constants.NOT_ASSIGNED));
            }
        });
        findViewById(R.id.b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMainTokhomV.this, ActivityAddSound.class));
            }
        });

    }
}