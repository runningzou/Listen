package com.runningzou.listen.local;

import android.arch.lifecycle.ViewModel;

import com.runningzou.listen.base.rxjava.RxCommand;
import com.runningzou.listen.model.DaoSession;
import com.runningzou.listen.model.PlayList;
import com.runningzou.listen.model.PlayListDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zouzhihao on 2017/10/22.
 */

@Singleton
public class AddPlayListDialogViewModel extends ViewModel {

    private DaoSession mDaoSession;

    @Inject
    public AddPlayListDialogViewModel(DaoSession daoSession) {
        mDaoSession = daoSession;
    }

    private RxCommand<List<PlayList>, Object> mGetPlayListRxCommand;

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

}

