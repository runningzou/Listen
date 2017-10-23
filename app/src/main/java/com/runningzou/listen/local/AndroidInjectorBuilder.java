package com.runningzou.listen.local;

import android.support.v4.app.Fragment;


import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Created by zouzhihao on 2017/10/19.
 */

@Module
public abstract class AndroidInjectorBuilder {
    @Binds
    @IntoMap
    @FragmentKey(AddPlayListDialog.class)
    public abstract AndroidInjector.Factory<? extends Fragment> bindPlayListFragment(AddPlayListDialogComponent.Builder builder);


}
