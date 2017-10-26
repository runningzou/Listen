package com.runningzou.listen.app;

import android.app.Activity;
import android.app.Service;

import com.runningzou.listen.main.MainActivity;
import com.runningzou.listen.main.MainActivityComponent;
import com.runningzou.listen.player.PlayerService;
import com.runningzou.listen.player.PlayerServiceComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.ServiceKey;
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

    @Binds
    @IntoMap
    @ServiceKey(PlayerService.class)
    abstract AndroidInjector.Factory<? extends Service> bindPlayerService(PlayerServiceComponent.Builder builder);

}
