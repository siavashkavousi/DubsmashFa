package com.aspire.dubsmash.hojjat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.siavash.ActivityMain;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

/**
 * Created by hojjat on 10/3/15.
 */
public class ActivityFirstRun extends AppCompatActivity {
    Button done;
    TextView title;
    EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        initConsViews();
        setListeners();
    }

    private void setListeners() {
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nameInput.getText().toString().trim().length() > 0)
                    done.setEnabled(true);
                else done.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (done.isEnabled())
                        done.performClick();
                }
                return false;
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.setUserName(ActivityFirstRun.this, nameInput.getText().toString());
                Util.registerUser(ActivityFirstRun.this);
                Util.setIsFirstRun(ActivityFirstRun.this, false);
                ActivityFirstRun.this.startActivity(new Intent(ActivityFirstRun.this, ActivityMain.class));
                finish();
            }
        });
    }

    private void initConsViews() {
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Bold, done, title, findViewById(R.id.your_name));
        Util.setFont(this, Util.FontFamily.Default, Util.FontWeight.Regular, findViewById(R.id.text), nameInput);
        Util.setText(done, "ورود", title, Constants.APP_NAME, findViewById(R.id.your_name), "نام شما", findViewById(R.id.text), "این نام در کنار داب های آپلود شده توسط شما به دیگران نمایش داده خواهد شد");
    }

    private void initViews() {
        done = (Button) findViewById(R.id.done);
        title = (TextView) findViewById(R.id.title);
        nameInput = (EditText) findViewById(R.id.editText);
    }
}