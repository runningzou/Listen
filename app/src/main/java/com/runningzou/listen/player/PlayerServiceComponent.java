package com.runningzou.listen.player;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by zouzhihao on 2017/10/25.
 */

@Subcomponent
public interface PlayerServiceComponent extends AndroidInjector<PlayerService> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<PlayerService> {

    }

}
