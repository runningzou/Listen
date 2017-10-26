package com.runningzou.listen.player;

import android.media.MediaPlayer;

import com.runningzou.listen.model.Song;

import java.io.IOException;

public class Player {

    private static volatile Player sInstance;

    private MediaPlayer mPlayer;

    private Song mSong;

    private Player() {
        mPlayer = new MediaPlayer();
    }

    public static Player getInstance() {
        if (sInstance == null) {
            synchronized (Player.class) {
                if (sInstance == null) {
                    sInstance = new Player();
                }
            }
        }
        return sInstance;
    }


    public boolean play(Song song) {

        mSong = song;

        try {
            mPlayer.reset();
            mPlayer.setDataSource(song.getPath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }


    public void resume() {
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }


    public boolean isPlaying() {
        if (mSong == null) {
            return false;
        }
        return mPlayer.isPlaying();
    }


    public int getProgress() {
        return mPlayer.getCurrentPosition();
    }


    public boolean seekTo(int progress) {

        if (mSong != null) {
            if (mSong.getDuration() <= progress) {

            } else {
                mPlayer.seekTo(progress);
            }
            return true;
        }
        return false;
    }

    public void releasePlayer() {
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        sInstance = null;
    }

    public Song getSong() {
        return mSong;
    }
}
