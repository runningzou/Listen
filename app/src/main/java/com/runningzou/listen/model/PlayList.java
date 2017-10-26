package com.runningzou.listen.model;

import com.runningzou.listen.player.PlayMode;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by zouzhihao on 2017/10/19.
 */

@Entity
public class PlayList {

    // Play List: Favorite
    public static final long NO_POSITION = -1;

    @Id(autoincrement = true)
    //据说必须是 Long，自增特性才能生效
    private Long id;

    private String name;

    private int numOfSongs;

    private boolean favorite;

    private Date createdAt;

    private Date updatedAt;


    @ToMany(referencedJoinProperty = "play_list_id")
    private List<Song> songs;

    @Transient
    private int playingIndex = -1;

    @Convert(converter = PlayModeConvert.class, columnType = Integer.class)
    private PlayMode playMode = PlayMode.LOOP;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 472247056)
    private transient PlayListDao myDao;

    @Generated(hash = 513233386)
    public PlayList(Long id, String name, int numOfSongs, boolean favorite, Date createdAt,
                    Date updatedAt, PlayMode playMode) {
        this.id = id;
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.favorite = favorite;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.playMode = playMode;
    }

    @Generated(hash = 438209239)
    public PlayList() {
    }

    public boolean prepare() {
        if (songs == null || songs.isEmpty()) return false;
        if (playingIndex == NO_POSITION) {
            playingIndex = 0;
        }
        return true;
    }

    public Song getCurrentSong() {
        if (playingIndex != NO_POSITION) {
            return songs.get(playingIndex);
        }
        return null;
    }


    public Song last() {
        switch (playMode) {
            case LOOP:
            case LIST:
                int newIndex = playingIndex - 1;
                if (newIndex < 0) {
                    newIndex = songs.size() - 1;
                }
                playingIndex = newIndex;
                break;
            case SINGLE:
                break;
            case SHUFFLE:
                playingIndex = randomPlayIndex();
                break;
        }
        return songs.get(playingIndex);
    }

    public boolean hasNext() {
        if (songs.isEmpty()) return false;
        if (playMode == PlayMode.LIST && playingIndex + 1 >= songs.size()) return false;
        return true;
    }

    public boolean hasLast() {
        return songs != null && songs.size() != 0;
    }

    public Song next() {
        switch (playMode) {
            case LOOP:
            case LIST:
                int newIndex = playingIndex + 1;
                if (newIndex >= songs.size()) {
                    newIndex = 0;
                }
                playingIndex = newIndex;
                break;
            case SINGLE:
                break;
            case SHUFFLE:
                playingIndex = randomPlayIndex();
                break;
        }
        return songs.get(playingIndex);
    }

    private int randomPlayIndex() {
        int randomIndex = new Random().nextInt(songs.size());
        // Make sure not play the same song twice if there are at least 2 songs
        if (songs.size() > 1 && randomIndex == playingIndex) {
            randomPlayIndex();
        }
        return randomIndex;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    public int getPlayingIndex() {
        return playingIndex;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return this.numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PlayMode getPlayMode() {
        return this.playMode;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1449635378)
    public List<Song> getSongs() {
        if (songs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongDao targetDao = daoSession.getSongDao();
            List<Song> songsNew = targetDao._queryPlayList_Songs(id);
            synchronized (this) {
                if (songs == null) {
                    songs = songsNew;
                }
            }
        }
        return songs;
    }

    public void addSongs(Song song) {
        if (songs == null) {
            songs = new ArrayList<>();
            songs.add(song);
        }

        songs.add(song);
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 432021166)
    public synchronized void resetSongs() {
        songs = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 469739525)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlayListDao() : null;
    }


    public static class PlayModeConvert implements PropertyConverter<PlayMode, Integer> {

        @Override
        public PlayMode convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }

            switch (databaseValue) {
                case 0:
                    return PlayMode.SINGLE;
                case 1:
                    return PlayMode.LOOP;
                case 2:
                    return PlayMode.LIST;
                case 3:
                    return PlayMode.SHUFFLE;

            }

            return PlayMode.LOOP;
        }

        @Override
        public Integer convertToDatabaseValue(PlayMode entityProperty) {

            switch (entityProperty) {
                case SINGLE:
                    return 0;
                case LOOP:
                    return 1;
                case LIST:
                    return 2;
                case SHUFFLE:
                    return 3;
            }

            return 1;
        }
    }

}

