package com.jxb.myrfid.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jxb.myrfid.R;
import com.jxb.myrfid.UHFApplication;
import com.jxb.myrfid.fragment.ConcentratedSettingFragment;
import com.jxb.myrfid.fragment.InventoryFragment;
import com.jxb.myrfid.fragment.ReadAndWriteFragment;
import com.jxb.myrfid.series.adapter.MyFragmentManagerAdapter;
import com.jxb.myrfid.series.model.Message;
import com.jxb.myrfid.series.operation.IUSeries;
import com.jxb.myrfid.series.operation.U8Series;
import com.jxb.myrfid.series.reader.model.InventoryBuffer;
import com.jxb.myrfid.series.reader.server.ReaderHelper;

import java.util.ArrayList;

import cn.fuen.xmldemo.activity.SetAndSaveActivity;

public class SeriesActivity extends FragmentActivity {
    private static String TAG = "SeriesActivity";
    private ArrayList<Fragment> fragmentList;
    private ViewPager mPager;// 页卡内容
    private ImageView cursor;// 动画图片
    private TextView t1, t2, t3;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    private String model = "U8";
    private String packageName = "com.jxb.myrfid";
    private String activityName = "com.jxb.myrfid.activity.SeriesActivity";
    private boolean isJumpExit;

    private static InventoryBuffer m_curInventoryBuffer = null;

    //读写
    private ReadAndWriteFragment rwFragment = null;
    //盘询
    private InventoryFragment inventoryFragment = null;
    //设置
    private ConcentratedSettingFragment concentratedSettingFragment;
    private IUSeries mUSeries;

    @Override
    protected void onResume() {
        Message powerOnMessage = mUSeries.modulePowerOn(model);
        if (powerOnMessage.getCode() != 0)
            jumpToConfigurationTool();
        super.onResume();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        ((UHFApplication) getApplication()).addActivity(this);

        //1、初始化串口
        mUSeries = U8Series.getInstance();
        U8Series.setContext(this);
        //2、初始化页面资源
        InitImageView();
        InitTextView();
        InitViewPager();

        //3、打开配置文件
        Message openSerialPortMeaasge = mUSeries.openSerialPort(model);
        //打开失败 跳转到配置界面
        if (openSerialPortMeaasge.getCode() != 0) {
            jumpToConfigurationTool();
            return;
        }

        //盘询buf
        try {
            m_curInventoryBuffer = ReaderHelper.getDefaultHelper().getCurInventoryBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 若配置文件丟失,跳转到配置工具
     */
    private void jumpToConfigurationTool() {
        Intent intent = new Intent();
        intent.putExtra("modelName", model);
        intent.putExtra("packageName", packageName);
        intent.putExtra("activityName", activityName);
        intent.setClass(this, SetAndSaveActivity.class);
        startActivity(intent);
        isJumpExit = true;
        finish();
    }

    /**
     * 模块下电，关闭串口
     */
    private void EndWork() {
        mUSeries.closeSerialPort();
        mUSeries.modulePowerOff(model);
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.tab_index1);
        t2 = (TextView) findViewById(R.id.tab_index2);
        t3 = (TextView) findViewById(R.id.tab_index3);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
        setTabColor(0);
    }

    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);

        fragmentList = new ArrayList<Fragment>();// 保存碎片

        //读写
        rwFragment = new ReadAndWriteFragment(this);
        fragmentList.add(rwFragment);
        //盘询
        inventoryFragment = new InventoryFragment(this);
        fragmentList.add(inventoryFragment);
        //设置
        concentratedSettingFragment = new ConcentratedSettingFragment(this);
        fragmentList.add(concentratedSettingFragment);

        // 得到碎片管理器
        FragmentManager fm = getSupportFragmentManager();

        // 创建碎片管理器
        MyFragmentManagerAdapter adapter = new MyFragmentManagerAdapter(fm, fragmentList);
        mPager.setAdapter(adapter);

        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            setTabColor(position);
            switch (position) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    InventoryFragment.btnStop.setEnabled(false);
                    InventoryFragment.btnStart.setEnabled(true);
                    if (InventoryFragment.isInventory) {
                        try {
                            if (!mUSeries.stopInventory()) {
                                Toast.makeText(SeriesActivity.this, "停止盘询失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    rwFragment.addEpcList(null, m_curInventoryBuffer);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (InventoryFragment.isInventory) {
                        try {
                            if (!mUSeries.stopInventory()) {
                                Toast.makeText(SeriesActivity.this, "停止盘询失败", Toast.LENGTH_SHORT).show();
                            }else {
                                InventoryFragment.btnStop.setEnabled(false);
                                InventoryFragment.btnStart.setEnabled(true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    concentratedSettingFragment.init();

                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = position;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * 通过设置Tab enable/disenable改变tab颜色
     *
     * @param disenablePosition
     */
    private void setTabColor(int disenablePosition) {
        switch (disenablePosition) {
            case 0:
                t1.setEnabled(false);
                t2.setEnabled(true);
                t3.setEnabled(true);
                break;
            case 1:
                t1.setEnabled(true);
                t2.setEnabled(false);
                t3.setEnabled(true);
                break;
            case 2:
                t1.setEnabled(true);
                t2.setEnabled(true);
                t3.setEnabled(false);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (InventoryFragment.isInventory) {
            try {
                if (!mUSeries.stopInventory()) {
                    Toast.makeText(SeriesActivity.this, "停止盘询失败", Toast.LENGTH_SHORT).show();
                }
                mUSeries.modulePowerOff(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isJumpExit) {
            EndWork();
            System.exit(0);
        }
    }
}
