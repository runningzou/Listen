package com.runningzou.listen.main;

import com.runningzou.listen.local.LocalFileComponent;
import com.runningzou.listen.music.MusicPlayerComponent;
import com.runningzou.listen.playlist.PlayListFragmentComponent;

import dagger.Module;

/**
 * Created by zouzhihao on 2017/10/18.
 */

@Module(subcomponents = {PlayListFragmentComponent.class, LocalFileComponent.class, MusicPlayerComponent.class})
public class MainActivityModule {
}
