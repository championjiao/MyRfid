package com.jxb.myrfid.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jxb.myrfid.R;

/**
 * Created by jxb on 2017-12-26.
 */

public class ThreeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container,false);
        return view;
    }
}
