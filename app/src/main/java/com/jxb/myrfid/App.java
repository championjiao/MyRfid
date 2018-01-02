package com.jxb.myrfid;

import android.app.Application;
import com.jxb.myrfid.entity.DaoMaster;
import com.jxb.myrfid.entity.DaoMaster.DevOpenHelper;
import com.jxb.myrfid.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DevOpenHelper helper = new DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}