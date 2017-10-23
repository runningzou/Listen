package com.runningzou.listen.playlist;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by zouzhihao on 2017/10/19.
 */

@Subcomponent(modules = PlayListFragmentModule.class)
public interface PlayListFragmentComponent extends AndroidInjector<PlayListFragment>{

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<PlayListFragment> {
    }
}
