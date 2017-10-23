package com.runningzou.listen.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by zouzhihao on 2017/10/19.
 */

@Entity
public class Song implements Serializable {

    public static final long serialVersionUID = 12344;

    @Id(autoincrement = true)
    private Long song_id;

    private Long play_list_id;

    private String title;

    private String displayName;

    private String artist;

    private String album;

    @Unique
    private String path;

    private int duration;

    private int size;

    private boolean favorite;

    @Generated(hash = 890491061)
    public Song(Long song_id, Long play_list_id, String title, String displayName,
            String artist, String album, String path, int duration, int size,
            boolean favorite) {
        this.song_id = song_id;
        this.play_list_id = play_list_id;
        this.title = title;
        this.displayName = displayName;
        this.artist = artist;
        this.album = album;
        this.path = path;
        this.duration = duration;
        this.size = size;
        this.favorite = favorite;
    }

    @Generated(hash = 87031450)
    public Song() {
    }

    public Long getSong_id() {
        return this.song_id;
    }

    public void setSong_id(Long song_id) {
        this.song_id = song_id;
    }

    public Long getPlay_list_id() {
        return this.play_list_id;
    }

    public void setPlay_list_id(Long play_list_id) {
        this.play_list_id = play_list_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }



}

