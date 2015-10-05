package com.aspire.dubsmash.siavash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.hojjat.ActivityFirstRun;
import com.aspire.dubsmash.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityMain extends AppCompatActivity {

    public static NetworkApi sNetworkApi;
    public static SharedPreferences sSharedPreferences;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.drawer_list)
    RecyclerView mDrawerList;
    private AdapterDrawerList mAdapterDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Util.isFirstRun(this)) {
            startActivity(new Intent(this, ActivityFirstRun.class));
            finish();
        }
        Util.registerUserIfNeeded(this);
        Util.createDirectories();

        // Set up drawer layout
        List<String> drawerItems = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.drawer_list_items)));
        mAdapterDrawerList = new AdapterDrawerList(this, drawerItems);
        mDrawerList.setLayoutManager(new LinearLayoutManager(this));
        mDrawerList.setAdapter(mAdapterDrawerList);

        getFragmentManager().beginTransaction().add(R.id.container, new FragmentViewPager()).commit();

        sNetworkApi = ServiceGenerator.createService(NetworkApi.class, "http://www.api.farsi-dub.mokhi.ir");
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
}
