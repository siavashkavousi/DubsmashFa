package com.aspire.dubsmash.siavash;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aspire.dubsmash.R;
import com.aspire.dubsmash.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sia on 10/2/15.
 */
public class FragmentUsername extends Fragment {

    @Bind(R.id.username) EditText username;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_username, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.register_btn) void onClickRegister() {
//        Util.registerUser(getActivity(), ActivityMain.sSharedPreferences, username.getText().toString());
    }
}
