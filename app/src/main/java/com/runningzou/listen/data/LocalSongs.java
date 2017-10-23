package com.runningzou.listen.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;

import com.runningzou.listen.app.ListenApp;
import com.runningzou.listen.model.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zouzhihao on 2017/10/21.
 */

public class LocalSongs {

    private static final int URL_LOAD_LOCAL_MUSIC = 0;
    private static final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final String WHERE = MediaStore.Audio.Media.IS_MUSIC + "=1 AND "
            + MediaStore.Audio.Media.SIZE + ">0";
    private static final String ORDER_BY = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

    private static String[] PROJECTIONS = {
            MediaStore.Audio.Media.DATA, // the real path
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };

    @Inject
    public LocalSongs() {

    }

    public List<Song> getLocalSongs() {

        Cursor cursor = ContentResolverCompat.query(ListenApp.getApp().getContentResolver(),
                MEDIA_URI,
                PROJECTIONS,
                WHERE,
                null,
                ORDER_BY, null);

        List<Song> songs = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Song song = cursorToMusic(cursor);
                songs.add(song);
            } while (cursor.moveToNext());
        }

        return songs;

    }

    private Song cursorToMusic(Cursor cursor) {
        String realPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        File songFile = new File(realPath);
        Song song;
        if (songFile.exists()) {
            // Using song parsed from file to avoid encoding problems
            song = FileUtils.fileToMusic(songFile);
            if (song != null) {
                return song;
            }
        }
        song = new Song();
        song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
        String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
        if (displayName.endsWith(".mp3")) {
            displayName = displayName.substring(0, displayName.length() - 4);
        }
        song.setDisplayName(displayName);
        song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
        song.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
        song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
        song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
        song.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
        return song;
    }
}
