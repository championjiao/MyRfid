package com.jxb.myrfid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hjm.bottomtabbar.BottomTabBar;
import com.jxb.myrfid.MainActivity;
import com.jxb.myrfid.R;
import com.jxb.myrfid.entity.Fruit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by jxb on 2017-12-26.
 */

public class OneFragment extends Fragment {

    @BindView(R.id.list_view)
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container,false);
        ButterKnife.bind(this, view);

        FruitAdapter adapter = new FruitAdapter(getContext(), R.layout.item, getData());
        listView.setAdapter(adapter);
        return view;
    }

    private List<Fruit> getData(){
        List<Fruit> data = new ArrayList<Fruit>();
        for(int i = 0;i < 10;i++) {
            data.add(new Fruit("123",i));
        }
        return data;
    }


    @OnItemClick(R.id.list_view)
    void itemClicked(AdapterView<?> parent, View view,
                     int position, long id) {
        Fruit fruit = new Fruit("123",99);
    }

    public class FruitAdapter extends ArrayAdapter<Fruit> {

        private int resourceId;
        /**
         *context:当前活动上下文
         *textViewResourceId:ListView子项布局的ID
         *objects：要适配的数据
         */
        public FruitAdapter(Context context, int textViewResourceId,
                            List<Fruit> objects) {
            super(context, textViewResourceId, objects);
            //拿取到子项布局ID
            resourceId = textViewResourceId;
        }

        /**
         * LIstView中每一个子项被滚动到屏幕的时候调用
         * position：滚到屏幕中的子项位置，可以通过这个位置拿到子项实例
         * convertView：之前加载好的布局进行缓存
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Fruit fruit = getItem(position);  //获取当前项的Fruit实例
            //为子项动态加载布局
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            //ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            TextView fruitName = (TextView) view.findViewById(R.id.fruit_name);
            //fruitImage.setImageResource(fruit.getImageId());
            fruitName.setText(fruit.getName());
            return view;
        }

    }
}
