package com.runningzou.listen.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by zouzhihao on 2017/10/21.
 */

@Entity
public class Folder {
    @Id(autoincrement = true)
    private Long folder_id;

    private String name;

    @Unique
    private String path;

    private int numOfSongs;


    @ToMany(referencedJoinProperty = "song_id")
    private List<Song> songs = new ArrayList<>();

    private Date createAt;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2091473052)
    private transient FolderDao myDao;

    @Generated(hash = 316049470)
    public Folder(Long folder_id, String name, String path, int numOfSongs, Date createAt) {
        this.folder_id = folder_id;
        this.name = name;
        this.path = path;
        this.numOfSongs = numOfSongs;
        this.createAt = createAt;
    }

    @Generated(hash = 1947132626)
    public Folder() {
    }

    public Folder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Folder)) return false;

        Folder folder = (Folder) o;

        if (numOfSongs != folder.numOfSongs) return false;
        if (folder_id != null ? !folder_id.equals(folder.folder_id) : folder.folder_id != null)
            return false;
        if (name != null ? !name.equals(folder.name) : folder.name != null) return false;
        if (path != null ? !path.equals(folder.path) : folder.path != null) return false;
        if (songs != null ? !songs.equals(folder.songs) : folder.songs != null) return false;
        return createAt != null ? createAt.equals(folder.createAt) : folder.createAt == null;

    }

    @Override
    public int hashCode() {
        int result = folder_id != null ? folder_id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + numOfSongs;
        result = 31 * result + (songs != null ? songs.hashCode() : 0);
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        return result;
    }

    public Long getFolder_id() {
        return this.folder_id;
    }

    public void setFolder_id(Long folder_id) {
        this.folder_id = folder_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNumOfSongs() {
        return this.numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public Date getCreateAt() {
        return this.createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1821470386)
    public List<Song> getSongs() {
        if (songs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongDao targetDao = daoSession.getSongDao();
            List<Song> songsNew = targetDao._queryFolder_Songs(folder_id);
            synchronized (this) {
                if (songs == null) {
                    songs = songsNew;
                }
            }
        }
        return songs;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
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

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1822270472)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFolderDao() : null;
    }
}
