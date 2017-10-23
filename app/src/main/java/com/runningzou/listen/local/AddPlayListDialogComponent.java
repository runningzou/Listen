package com.runningzou.listen.local;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by zouzhihao on 2017/10/22.
 */

@Subcomponent
public interface AddPlayListDialogComponent extends AndroidInjector<AddPlayListDialog> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AddPlayListDialog> {
    }

}
