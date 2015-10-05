package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspire.dubsmash.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sia on 10/3/15.
 */
public class FragmentDownloads extends Fragment {
    private static final String TAG = FragmentSounds.class.getSimpleName();

    @Bind(R.id.items_recycler_view) RecyclerView mVideosRecyclerView;

    private AdapterMyDubs mAdapter;
    private List<Sound> mSounds;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sounds_videos, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mVideosRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterMyDubs(null);
        mVideosRecyclerView.setAdapter(mAdapter);
//        mSoundsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener<LinearLayoutManager>(linearLayoutManager) {
//            @Override public void onLoadMore(int currentPage) {
//                ActivityMain.sNetworkApi.downloadSound("latest", "0", "5", null, new Callback<Response>() {
//                    @Override public void success(Response response, Response response2) {
//
//                    }
//
//                    @Override public void failure(RetrofitError error) {
//
//                    }
//                });
//            }
//        });
    }
}
