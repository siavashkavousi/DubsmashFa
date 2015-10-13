package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;
import com.aspire.dubsmash.util.Util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sia on 10/3/15.
 */
public class FragmentMySounds extends Fragment {
    private static final String TAG = FragmentMySounds.class.getSimpleName();

    @Bind(R.id.items_recycler_view)
    RecyclerView mSoundsRecyclerView;

    private AdapterMySounds mAdapter;
    private List<String> mSoundsPath;
    private List<String> mSoundsTitle;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sounds_videos, container, false);
        ButterKnife.bind(this, view);
        getSoundFiles();
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mSoundsRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterMySounds(this, mSoundsPath, mSoundsTitle);
        mSoundsRecyclerView.setAdapter(mAdapter);
    }

    private void getSoundFiles() {
        File soundsDirectory = new File(Constants.MY_SOUNDS_PATH);
        File[] files = soundsDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getAbsolutePath().endsWith(".m4a");
            }
        });
        if (files == null) return;
        mSoundsPath = new ArrayList<>();
        mSoundsTitle = new ArrayList<>();
        for (File f : files) {
            mSoundsPath.add(f.getAbsolutePath());
            mSoundsTitle.add(f.getName());
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        Util.stopAndReleaseMediaPlayer(mAdapter.getMediaPlayer());
    }
}
