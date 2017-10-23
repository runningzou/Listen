package com.runningzou.listen.local;

import android.databinding.ViewDataBinding;

import com.runningzou.listen.R;
import com.runningzou.listen.base.list.FooterAdapter;
import com.runningzou.listen.data.TimeUtils;
import com.runningzou.listen.databinding.ItemLocalMusicBinding;
import com.runningzou.listen.model.Song;

/**
 * Created by zouzhihao on 2017/10/22.
 */

public class LocalMusicAdapter extends FooterAdapter<Song> {


    @Override
    protected int itemLayout() {
        return R.layout.item_local_music;
    }

    @Override
    protected void bindItem(ViewDataBinding binding, Song item) {
        ItemLocalMusicBinding bind = (ItemLocalMusicBinding) binding;
        bind.textViewArtist.setText(item.getArtist());
        bind.textViewName.setText(item.getTitle());
    }

    @Override
    public void bindFooter(ViewDataBinding binding) {

    }

    @Override
    protected int footerLayout() {
        return R.layout.item_local_music_footer;
    }
}
