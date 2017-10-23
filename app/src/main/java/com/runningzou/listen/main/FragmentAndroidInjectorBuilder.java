package com.runningzou.listen.main;

import android.support.v4.app.Fragment;

import com.runningzou.listen.local.LocalFileComponent;
import com.runningzou.listen.local.LocalFileViewModel;
import com.runningzou.listen.local.LocalFilesFragment;
import com.runningzou.listen.music.MusicPlayerComponent;
import com.runningzou.listen.music.MusicPlayerFragmet;
import com.runningzou.listen.playlist.PlayListFragment;
import com.runningzou.listen.playlist.PlayListFragmentComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;

import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Created by zouzhihao on 2017/10/19.
 */

@Module
public abstract class FragmentAndroidInjectorBuilder {
    @Binds
    @IntoMap
    @FragmentKey(PlayListFragment.class)
    public abstract AndroidInjector.Factory<? extends Fragment> bindPlayListFragment(PlayListFragmentComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(LocalFilesFragment.class)
    public abstract AndroidInjector.Factory<? extends Fragment> bindLocalFileFragment(LocalFileComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(MusicPlayerFragmet.class)
    public abstract AndroidInjector.Factory<? extends Fragment> bindMusicPlayerFragment(MusicPlayerComponent.Builder builder);

}
