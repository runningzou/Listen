package com.runningzou.listen.playlist;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.runningzou.listen.R;
import com.runningzou.listen.base.rxjava.RxCommand;
import com.runningzou.listen.local.SongAddToPlayListEvent;
import com.runningzou.listen.model.DaoSession;
import com.runningzou.listen.model.PlayList;
import com.runningzou.listen.model.PlayListDao;
import com.runningzou.listen.model.Song;
import com.runningzou.listen.player.PlayMode;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zouzhihao on 2017/10/19.
 */

public class PlayListViewModel extends ViewModel {

    private DaoSession mDaoSession;

    @Inject
    public PlayListViewModel(DaoSession daoSession) {
        mDaoSession = daoSession;
    }

    private RxCommand<List<PlayList>, Object> mGetPlayListRxCommand;
    private RxCommand<PlayList, PlayList> mInsertPlayListRxCommand;
    private RxCommand<Boolean, Object> mPrepareFavariteRxCommand;
    private RxCommand<Long, Object> mPlayListCountRxCommand;
    private RxCommand<PlayList, PlayList> mDeleteRxCommand;
    private RxCommand<PlayList, PlayList> mUpdateRxCommand;
    private RxCommand<PlayList, SongAddToPlayListEvent> mAddToPlayListRxCommand;

    public RxCommand<List<PlayList>, Object> getPlayListRxCommand() {

        if (mGetPlayListRxCommand == null) {
            mGetPlayListRxCommand = RxCommand.create(new Function<Object, Observable<List<PlayList>>>() {
                @Override
                public Observable<List<PlayList>> apply(@NonNull Object o) throws Exception {

                    return Observable.create(new ObservableOnSubscribe<List<PlayList>>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<List<PlayList>> e) throws Exception {

                            List<PlayList> lists = mDaoSession
                                    .getPlayListDao()
                                    .queryBuilder()
                                    .orderAsc(PlayListDao.Properties.Id)
                                    .list();

                            for (PlayList playList : lists) {
                                playList.getSongs();
                                Logger.d("songs size " + playList.getSongs().size());
                            }
                            e.onNext(lists);
                            e.onComplete();
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());

                }
            });
        }

        return mGetPlayListRxCommand;
    }

    public RxCommand<Boolean, Object> prepapreFavariteRxCommand(final Context context) {

        if (mPrepareFavariteRxCommand == null) {
            mPrepareFavariteRxCommand = RxCommand.create(new Function<Object, Observable<Boolean>>() {
                @Override
                public Observable<Boolean> apply(@NonNull Object o) throws Exception {
                    return Observable.create(new ObservableOnSubscribe<Boolean>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                            PlayList list = mDaoSession.getPlayListDao().load(PlayList.NO_POSITION);

                            if (list == null) {
                                Date now = new Date();
                                list = new PlayList();
                                list.setNumOfSongs(0);
                                list.setPlayMode(PlayMode.LOOP);
                                list.setFavorite(true);
                                list.setCreatedAt(now);
                                list.setId(PlayList.NO_POSITION);
                                list.setName(context.getResources().getString(R.string.listen_item_title_collection));
                                list.setUpdatedAt(now);
                                mDaoSession.getPlayListDao().insert(list);
                            }

                            e.onNext(true);
                            e.onComplete();
                        }
                    });
                }
            });
        }

        return mPrepareFavariteRxCommand;
    }

    public RxCommand<PlayList, PlayList> insertPlayListRxCommand() {

        if (mInsertPlayListRxCommand == null) {
            mInsertPlayListRxCommand = RxCommand.create(new Function<PlayList, Observable<PlayList>>() {
                @Override
                public Observable<PlayList> apply(@NonNull final PlayList playList) throws Exception {

                    return Observable.create(new ObservableOnSubscribe<PlayList>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<PlayList> e) throws Exception {
                            mDaoSession.getPlayListDao().insert(playList);
                            e.onNext(playList);
                            e.onComplete();

                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());


                }
            });
        }

        return mInsertPlayListRxCommand;
    }

    public RxCommand<Long, Object> playListCountRxCommand() {

        if (mPlayListCountRxCommand == null) {
            mPlayListCountRxCommand = RxCommand.create(new Function<Object, Observable<Long>>() {
                @Override
                public Observable<Long> apply(@NonNull Object o) throws Exception {
                    return Observable.create(new ObservableOnSubscribe<Long>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<Long> e) throws Exception {
                            long count = mDaoSession.getPlayListDao().count();
                            e.onNext(count);
                            e.onComplete();
                        }
                    });
                }
            });
        }

        return mPlayListCountRxCommand;
    }

    public RxCommand<PlayList, PlayList> deleteRxCommand() {
        if (mDeleteRxCommand == null) {
            mDeleteRxCommand = RxCommand.create(new Function<PlayList, Observable<PlayList>>() {
                @Override
                public Observable<PlayList> apply(@NonNull final PlayList playList) throws Exception {

                    return Observable.create(new ObservableOnSubscribe<PlayList>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<PlayList> e) throws Exception {
                            mDaoSession.getPlayListDao().delete(playList);
                            e.onNext(playList);
                            e.onComplete();
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            });
        }

        return mDeleteRxCommand;
    }

    public RxCommand<PlayList, PlayList> updateRxCommand() {
        if (mUpdateRxCommand == null) {
            mUpdateRxCommand = RxCommand.create(new Function<PlayList, Observable<PlayList>>() {
                @Override
                public Observable<PlayList> apply(@NonNull final PlayList playList) throws Exception {
                    return Observable.create(new ObservableOnSubscribe<PlayList>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<PlayList> e) throws Exception {
                            mDaoSession.getPlayListDao().update(playList);
                            e.onNext(playList);
                            e.onComplete();
                        }
                    });

                }
            });
        }

        return mUpdateRxCommand;
    }

    public RxCommand<PlayList, SongAddToPlayListEvent> addToPlayListRxCommand() {
        if (mAddToPlayListRxCommand == null) {
            mAddToPlayListRxCommand = RxCommand.create(new Function<SongAddToPlayListEvent, Observable<PlayList>>() {
                @Override
                public Observable<PlayList> apply(@NonNull final SongAddToPlayListEvent songAddToPlayListEvent) throws Exception {
                    return Observable.create(new ObservableOnSubscribe<PlayList>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<PlayList> e) throws Exception {
                            Song song = songAddToPlayListEvent.mSong;
                            PlayList playList = songAddToPlayListEvent.mPlayList;
                            boolean needAdd = true;

                            for (Song item : playList.getSongs()) {

                                if (item.getPath().equals(song.getPath())) {
                                    needAdd = false;
                                    break;
                                }
                            }

                            if (needAdd) {
                                song.setPlay_list_id(playList.getId());
                                playList.getSongs().add(song);
                                playList.setNumOfSongs(playList.getSongs().size());
                                e.onNext(playList);
                                mDaoSession.getPlayListDao().update(playList);
                                mDaoSession.getSongDao().insert(song);
                            }


                            e.onComplete();

                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            });
        }

        return mAddToPlayListRxCommand;
    }

}
