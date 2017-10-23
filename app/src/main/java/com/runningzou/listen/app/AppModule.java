package com.runningzou.listen.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.runningzou.listen.main.MainActivityComponent;
import com.runningzou.listen.model.DaoMaster;
import com.runningzou.listen.model.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zouzhihao on 2017/10/18.
 */

@Module(subcomponents = {MainActivityComponent.class})
public class AppModule {

    @Singleton
    @Provides
    public DaoSession provideDaoSession(Application app) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(app, "listen.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);

        return daoMaster.newSession();
    }

    @Singleton
    @Provides
    public Context provideContext(Application app) {
        return app.getApplicationContext();
    }

}
