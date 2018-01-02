package com.jxb.myrfid.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jxb.myrfid.R;
import com.jxb.myrfid.entity.Fruit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

/**
 * Created by jxb on 2017-12-26.
 */

public class TwoFragment extends Fragment {

    @BindView(R.id.spinner)
    Spinner spinner;

    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    @OnItemSelected(R.id.spinner)
    void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                        long arg3) {
        Fruit fruit = new Fruit("123",99);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container,false);
        ButterKnife.bind(this, view);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("北京");
        data_list.add("上海");
        data_list.add("广州");
        data_list.add("深圳");

        //适配器
        arr_adapter= new ArrayAdapter<String>(getContext(), R.layout.spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.dropdown_stytle);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        return view;
    }

}
