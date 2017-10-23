package com.runningzou.listen.local;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by zouzhihao on 2017/10/21.
 */

@Subcomponent(modules = {LocalFileModule.class, AndroidInjectorBuilder.class})
public interface LocalFileComponent extends AndroidInjector<LocalFilesFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LocalFilesFragment> {
    }
}
