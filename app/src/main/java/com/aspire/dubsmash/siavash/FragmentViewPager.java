package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspire.dubsmash.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sia on 10/3/15.
 */
public class FragmentViewPager extends Fragment {

    @Bind(R.id.view_pager_tab) SmartTabLayout mViewPagerTab;
    @Bind(R.id.view_pager) ViewPager mViewPager;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ButterKnife.bind(this, view);
        // Set up view pager
        setUpViewPager();

        return view;
    }

    private void setUpViewPager() {
        mViewPager.setAdapter(setUpViewPagerAdapter());
        mViewPagerTab.setViewPager(mViewPager);
    }

    @NonNull private FragmentPagerItemAdapter setUpViewPagerAdapter() {
        return new FragmentPagerItemAdapter(
                getFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("صدا ها", FragmentSounds.class)
                .add("داب ها", FragmentVideos.class)
                .create());
    }
}
