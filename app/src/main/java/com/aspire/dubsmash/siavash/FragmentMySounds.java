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
public class FragmentMySounds extends Fragment {
    private static final String TAG = FragmentMySounds.class.getSimpleName();

    @Bind(R.id.items_recycler_view)
    RecyclerView mSoundsRecyclerView;

    private AdapterMySounds mAdapter;
    private List<String> mSoundsPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sounds_videos, container, false);
        ButterKnife.bind(this, view);
        File mSoundsDirectory = new File(Constants.MY_SOUNDS_PATH);
        File[] files = mSoundsDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.getAbsolutePath().endsWith(".m4a"))
                    return true;
                return false;
            }
        });
        mSoundsPath = new ArrayList<>();
        for (File f : files)
            mSoundsPath.add(f.getAbsolutePath());
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mSoundsRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterMySounds(mSoundsPath);
        mSoundsRecyclerView.setAdapter(mAdapter);
    }
}
