package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sia on 9/30/15.
 */
public class FragmentSounds extends Fragment {
    private static final String TAG = FragmentSounds.class.getSimpleName();

    @Bind(R.id.switch_lt) Switch mSwitchLT;
    @Bind(R.id.items_recycler_view) RecyclerView mSoundsRecyclerView;

    private AdapterSounds mAdapter;
    private List<Sound> mSounds;
    private boolean isViewShown = false;
    private boolean isFirstRun = true;
    private int group = 0, quantity = 15;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sounds_videos, container, false);
        ButterKnife.bind(this, view);
        mSounds = new ArrayList<>();
        setUpSwitch();
        if (isFirstRun) downloadInitialSounds();
        return view;
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null) {
            downloadInitialSounds();
        }
    }

    private void setUpSwitch() {
        mSwitchLT.setTextOff(Constants.TRENDING);
        mSwitchLT.setTextOn(Constants.LATEST);
        mSwitchLT.setChecked(true);
    }

    private void setUpRecyclerView() {
        isFirstRun = false;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mSoundsRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterSounds(mSounds);
        mSoundsRecyclerView.setAdapter(mAdapter);
        mSoundsRecyclerView.addItemDecoration(new SpacesItemDecoration(Constants.RECYCLE_VIEW_SPACE));
        mSoundsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener<LinearLayoutManager>(linearLayoutManager) {
            @Override public void onLoadMore(int currentPage) {
                if (mSwitchLT.isChecked()) {
                    downloadSounds(Constants.LATEST, String.valueOf(++group), String.valueOf(quantity));
                } else {
                    downloadSounds(Constants.TRENDING, String.valueOf(++group), String.valueOf(quantity));
                }
            }
        });
    }

    private void downloadSounds(final String retrieveMode, String group, String quantity) {
        ActivityMain.sNetworkApi.downloadSound(retrieveMode, group, quantity, null, new Callback<MultipleResult<Sound>>() {
            @Override public void success(MultipleResult<Sound> soundMultipleResult, Response response) {
                if (soundMultipleResult.getOk().equals(Constants.OK)) {
                    mSounds.addAll(soundMultipleResult.getItems());
                    if (isFirstRun) setUpRecyclerView();
                    else mAdapter.addData(mSounds);
                }
            }

            @Override public void failure(RetrofitError error) {
            }
        });
    }

    private void downloadInitialSounds() {
        if (mSwitchLT.isChecked()) {
            downloadSounds(Constants.LATEST, String.valueOf(group), String.valueOf(quantity));
        } else {
            downloadSounds(Constants.TRENDING, String.valueOf(group), String.valueOf(quantity));
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        if (mAdapter != null && mAdapter.getMediaPlayer() != null) {
            Util.stopAndReleaseMediaPlayer(mAdapter.getMediaPlayer());
        }
    }
}
