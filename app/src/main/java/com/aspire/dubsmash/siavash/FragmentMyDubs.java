package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Constants;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sia on 10/3/15.
 */
public class FragmentMyDubs extends Fragment {
    private static final String TAG = FragmentSounds.class.getSimpleName();

    @Bind(R.id.items_recycler_view)
    RecyclerView mVideosRecyclerView;

    private AdapterMyDubs mAdapter;
    private List<Sound> mSounds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sounds_videos, container, false);
        ButterKnife.bind(this, view);

        File mDubsDirectory = new File(Constants.MY_DUBS_PATH);
        File[] files = mDubsDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.getAbsolutePath().endsWith(".mp4"))
                    return true;
                return false;
            }
        });
        List mDubsPath = new ArrayList<>();
        for (File f : files)
            mDubsPath.add(f.getAbsolutePath());
        setUpRecyclerView(mDubsPath);
        return view;
    }

    private void setUpRecyclerView(List<String> data) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mVideosRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new AdapterMyDubs(data);
        mVideosRecyclerView.setAdapter(mAdapter);
        mVideosRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener<GridLayoutManager>(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
//                List<String> names = new ArrayList<>();
//                for (int i = size; i < size + 60; i++) {
//                    names.add("kir e asb " + i);
//                }
//                mAdapter.addData(names);
            }
        });
    }
}
