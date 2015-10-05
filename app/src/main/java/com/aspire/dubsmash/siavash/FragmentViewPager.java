package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspire.dubsmash.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sia on 10/3/15.
 */
public class FragmentViewPager extends Fragment {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    private AdapterPager mAdapterPager;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ButterKnife.bind(this, view);

        // Set up toolbar
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        // Set up view pager
        List<Fragment> fragments = getFragments();
        mAdapterPager = new AdapterPager(getFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapterPager);
        return view;
    }

    @OnClick(R.id.toolbar_hamburger) void onDrawerClick() {
        if (getActivity() instanceof ActivityMain){
            ((ActivityMain) getActivity()).mDrawerLayout.openDrawer(GravityCompat.END);
        }
    }

    private List<Fragment> getFragments() {
        FragmentSounds fragmentSounds = new FragmentSounds();
//        FragmentVideos fragmentVideos = new FragmentVideos();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragmentSounds);
//        fragments.add(fragmentVideos);
        return fragments;
    }
}
