package com.aspire.dubsmash.siavash;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.hojjat.ActivityAddSound;
import com.aspire.dubsmash.hojjat.ActivityFirstRun;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Endpoint;
import com.aspire.dubsmash.util.FragmentUtil;
import com.aspire.dubsmash.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMain extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    public static NetworkApi sNetworkApi;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawer;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.nav_view) NavigationView mNavigationView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        Endpoint endpoint = new Endpoint("http://www.api.farsi-dub.mokhi.ir");
        sNetworkApi = ServiceGenerator.createService(NetworkApi.class, endpoint);

        Util.changeEndpointIfNeeded(endpoint);

        if (Util.isFirstRun(this)) {
            startActivity(new Intent(this, ActivityFirstRun.class));
            finish();
        } else {
            switchFragmentTo(Constants.FRAGMENT_VIEW_PAGER);
        }
        Util.registerUserIfNeeded(this);
        Util.createDirectories();

        whichFragment(getIntent());

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @OnClick(R.id.toolbar_hamburger) void onToolbarToggleClick() {
        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        } else {
            mDrawer.openDrawer(GravityCompat.END);
        }
    }

    /**
     * compatibility method for calling fragments from other activities
     *
     * @param intent
     */
    private void whichFragment(Intent intent) {
        if (intent.getExtras() != null) {
            String fragmentName = intent.getExtras().getString(Constants.WHICH_FRAGMENT, Constants.NO_FRAGMENT);
            if (fragmentName.equals(Constants.FRAGMENT_MY_SOUNDS)) {
                switchFragmentTo(Constants.FRAGMENT_MY_SOUNDS);
            } else if (fragmentName.equals(Constants.FRAGMENT_MY_DUBS)) {
                switchFragmentTo(Constants.FRAGMENT_MY_DUBS);
            } else if (fragmentName.equals(Constants.FRAGMENT_DOWNLOADS)) {

            }
        }
    }

    @Override public void switchFragmentTo(String fragmentName, String... values) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentName.equals(Constants.FRAGMENT_VIEW_PAGER)) {
            FragmentUtil.replaceFragment(fragmentManager, R.id.container, new FragmentViewPager());
        } else if (fragmentName.equals(Constants.FRAGMENT_SOUNDS)) {
            FragmentUtil.replaceFragment(fragmentManager, R.id.container, new FragmentSounds());
        } else if (fragmentName.equals(Constants.FRAGMENT_VIDEOS)) {
            FragmentUtil.replaceFragment(fragmentManager, R.id.container, new FragmentVideos());
        } else if (fragmentName.equals(Constants.FRAGMENT_MY_SOUNDS)) {
            FragmentUtil.replaceFragment(fragmentManager, R.id.container, new FragmentMySounds());
        } else if (fragmentName.equals(Constants.FRAGMENT_MY_DUBS)) {
            FragmentUtil.replaceFragment(fragmentManager, R.id.container, new FragmentMyDubs());
        }
    }

    @Override public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_view_pager) {
            switchFragmentTo(Constants.FRAGMENT_VIEW_PAGER);
        } else if (id == R.id.nav_dubs) {
            switchFragmentTo(Constants.FRAGMENT_MY_DUBS);
        } else if (id == R.id.nav_sounds) {
            switchFragmentTo(Constants.FRAGMENT_MY_SOUNDS);
        } else if (id == R.id.nav_add_sounds) {
            startActivity(new Intent(this, ActivityAddSound.class));
        } else if (id == R.id.nav_download) {
            switchFragmentTo(Constants.FRAGMENT_DOWNLOADS);
        }

        mDrawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
