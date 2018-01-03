package com.jxb.myrfid;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.jxb.myrfid.entity.DaoMaster;
import com.jxb.myrfid.entity.DaoMaster.DevOpenHelper;
import com.jxb.myrfid.entity.DaoSession;
import com.jxb.myrfid.series.reader.server.ReaderHelper;
import com.jxb.myrfid.series.utils.MusicPlayer;

import org.greenrobot.greendao.database.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UHFApplication extends Application {

    //数据库操作对象
    private DaoSession daoSession;
    public static Context applicationContext;
    private List<Activity> activities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=getApplicationContext();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(applicationContext);
        MusicPlayer.getInstance();
        try {
            //实例化ReaderHelper并setContext
            ReaderHelper.setContext(applicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //数据库对象
        DevOpenHelper helper = new DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    //数据库操作
    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : activities) {
            try {
                activity.finish();
            } catch (Exception e) {
                ;
            }
        }

        if (BluetoothAdapter.getDefaultAdapter() != null)
            BluetoothAdapter.getDefaultAdapter().disable();
        System.exit(0);
    }


    static public void saveBeeperState(int state){
        SharedPreferences spf = applicationContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("_state", state);
        editor.commit();
    }

    static int _SoftSound=2;
    static public int appGetSoftSound(){
        if(_SoftSound==2)
            _SoftSound=getSoftSound();
        return _SoftSound;
    }
    static public int getVeeperState(){
        SharedPreferences spf = applicationContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        //SharedPreferences.Editor editor = spf.edit();
        int state = spf.getInt("_state", 0);
        return state;
    }

    static public void saveSoftSound(int state){
        _SoftSound=state;
        SharedPreferences spf = applicationContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("_software_sound", state);
        editor.commit();
    }

    static public int getSoftSound(){
        SharedPreferences spf =applicationContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        //SharedPreferences.Editor editor = spf.edit();
        int state = spf.getInt("_software_sound", 1);
        return state;
    }

    static public void saveSessionState(int state){
        SharedPreferences spf = applicationContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("_session", state);
        editor.commit();
    }

    static public int getSessionState(){
        SharedPreferences spf = applicationContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        //SharedPreferences.Editor editor = spf.edit();
        int state = spf.getInt("_session", 0);
        return state;
    }
    static public void saveFlagState(int state){
        SharedPreferences spf = applicationContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("_flag", state);
        editor.commit();
    }

    static public int getFlagState(){
        SharedPreferences spf = applicationContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        //SharedPreferences.Editor editor = spf.edit();
        int state = spf.getInt("_flag", 0);
        return state;
    }
}