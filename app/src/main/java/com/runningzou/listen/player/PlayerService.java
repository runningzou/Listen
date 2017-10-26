package com.runningzou.listen.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.runningzou.listen.model.DaoSession;
import com.runningzou.listen.model.PlayList;
import com.runningzou.listen.model.Song;

import javax.inject.Inject;


public class PlayerService extends Service {

    private static final int NOTIFICATION_ID = 1;

    private RemoteViews mContentViewBig, mContentViewSmall;

    private Player mPlayer;

    private PlayList mPlayList;

    @Inject
    DaoSession mDaoSession;

    private final Binder mBinder = new LocalBinder();

    public Song getPlayingSong() {
        return mPlayer.getSong();
    }


    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = Player.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean stopService(Intent name) {
        stopForeground(true);
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.releasePlayer();
    }


    public boolean play(PlayList list) {
        mPlayList = list;
        if (mPlayList.prepare()) {
            return mPlayer.play(mPlayList.getCurrentSong());
        }

        return false;
    }

    public boolean play(Song song) {
        PlayList playList = new PlayList();
        playList.addSongs(song);
        playList.__setDaoSession(mDaoSession);
        return play(playList);
    }


    public boolean playLast() {
        if (mPlayList.hasLast()) {
            return mPlayer.play(mPlayList.last());
        }
        return false;
    }


    public boolean playNext() {
        if (mPlayList.hasNext()) {
            return mPlayer.play(mPlayList.next());
        }
        return false;
    }


    public void pause() {
        mPlayer.pause();
    }

    public void resume() {
        mPlayer.resume();
    }


    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }


    public int getProgress() {
        return mPlayer.getProgress();
    }


    public boolean seekTo(int progress) {
        return mPlayer.seekTo(progress);
    }


    public void setPlayMode(PlayMode playMode) {
        mPlayList.setPlayMode(playMode);
    }


    public PlayMode nextMode() {
        PlayMode playMode = mPlayList.getPlayMode();
        PlayMode nextMode = PlayMode.switchNextMode(playMode);
        mPlayList.setPlayMode(nextMode);
        return nextMode;
    }


}
