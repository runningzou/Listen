package com.runningzou.listen.main;

import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by zouzhihao on 2017/10/18.
 */

@Subcomponent(modules = {MainActivityModule.class, FragmentAndroidInjectorBuilder.class})
public interface MainActivityComponent extends AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {

    }
}
