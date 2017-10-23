package com.runningzou.listen.local;

import com.runningzou.listen.model.PlayList;
import com.runningzou.listen.model.Song;

/**
 * Created by zouzhihao on 2017/10/22.
 */

public class SongAddToPlayListEvent {
    public Song mSong;
    public PlayList mPlayList;

    public SongAddToPlayListEvent(Song song, PlayList playList) {
        mSong = song;
        mPlayList = playList;
    }
}
