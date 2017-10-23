package com.runningzou.listen.app;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.runningzou.listen.R;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by zouzhihao on 2017/10/18.
 */

public class ListenApp extends Application implements HasActivityInjector {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Inject
    DispatchingAndroidInjector<Activity> mActivityDispatchingAndroidInjector;

    public static ListenApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;

        // Custom fonts
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Monospace-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        Logger.addLogAdapter(new AndroidLogAdapter());
        AppInjector.init(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mActivityDispatchingAndroidInjector;
    }

    public static ListenApp getApp() {
        return mApp;
    }
}
