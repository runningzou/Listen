package com.runningzou.listen.app;

import android.app.Activity;

import com.runningzou.listen.main.MainActivity;
import com.runningzou.listen.main.MainActivityComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by zouzhihao on 2017/10/18.
 */

@Module
public abstract class ActivityAndroidInjectorBuilder {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);

}
