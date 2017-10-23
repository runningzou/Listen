package com.runningzou.listen.music;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by zouzhihao on 2017/10/22.
 */

@Subcomponent
public interface MusicPlayerComponent extends AndroidInjector<MusicPlayerFragmet> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MusicPlayerFragmet> {
    }
}
