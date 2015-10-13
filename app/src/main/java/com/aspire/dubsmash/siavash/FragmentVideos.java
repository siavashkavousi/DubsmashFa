package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FragmentVideos extends Fragment {
    private static final String TAG = FragmentVideos.class.getSimpleName();
    @Bind(R.id.items_recycler_view) RecyclerView mVideosRecyclerView;
    @Bind(R.id.switch_lt) Switch mSwitchLT;

    private AdapterVideos mAdapter;
    private List<Video> mVideos;
    private boolean isFirstRun = true;
    private int group = 0, quantity = 15;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sounds_videos, container, false);
        ButterKnife.bind(this, view);
        mVideos = new ArrayList<>();

        setUpSwitch();
        return view;
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null) {
            downloadInitialVideos();
            downloadVideos(Constants.LATEST, "1", "1");
        }
    }

    private void setUpSwitch() {
        mSwitchLT.setTextOff(Constants.TRENDING);
        mSwitchLT.setTextOn(Constants.LATEST);
        mSwitchLT.setChecked(true);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mVideosRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterVideos(mVideos);
        mVideosRecyclerView.setAdapter(mAdapter);
        mVideosRecyclerView.addItemDecoration(new SpacesItemDecoration(Constants.RECYCLE_VIEW_SPACE));
//        mVideosRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener<LinearLayoutManager>(linearLayoutManager) {
//            @Override public void onLoadMore(int currentPage) {
//                if (mSwitchLT.isChecked()) {
//                    downloadVideos(Constants.LATEST, String.valueOf(++group), String.valueOf(quantity));
//                } else {
//                    downloadVideos(Constants.TRENDING, String.valueOf(++group), String.valueOf(quantity));
//                }
//            }
//        });
    }

    private void downloadVideos(String retrieveMode, String group, String quantity) {
        ActivityMain.sNetworkApi.downloadVideo(retrieveMode, group, quantity, null, new Callback<MultipleResult<Video>>() {
            @Override public void success(MultipleResult<Video> videoMultipleResult, Response response) {
                if (videoMultipleResult.getOk().equals(Constants.OK)) {
                    mVideos.addAll(videoMultipleResult.getItems());
                    if (isFirstRun) setUpRecyclerView();
                    else mAdapter.addData(mVideos);
                }
            }

            @Override public void failure(RetrofitError error) {

            }
        });
    }

    private void downloadInitialVideos() {
        if (mSwitchLT.isChecked()) {
            downloadVideos(Constants.LATEST, String.valueOf(group), String.valueOf(quantity));
        } else {
            downloadVideos(Constants.TRENDING, String.valueOf(group), String.valueOf(quantity));
        }
    }
}
