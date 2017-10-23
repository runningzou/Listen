package com.runningzou.listen.local;

import android.arch.lifecycle.ViewModel;

import com.runningzou.listen.base.rxjava.RxCommand;
import com.runningzou.listen.data.LocalSongs;
import com.runningzou.listen.model.Song;

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
 * Created by zouzhihao on 2017/10/21.
 */

public class LocalFileViewModel extends ViewModel {

    private RxCommand<List<Song>, Object> mLocalSongsRxCommand;

    private LocalSongs mLocalSongs;

    @Inject
    public LocalFileViewModel(LocalSongs localSongs) {
        mLocalSongs = localSongs;
    }


    public RxCommand<List<Song>, Object> localSongsRxCommand() {
        if (mLocalSongsRxCommand == null) {
            mLocalSongsRxCommand = RxCommand.create(new Function<Object, Observable<List<Song>>>() {
                @Override
                public Observable<List<Song>> apply(@NonNull Object o) throws Exception {

                    return Observable.create(new ObservableOnSubscribe<List<Song>>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<List<Song>> e) throws Exception {
                            e.onNext(mLocalSongs.getLocalSongs());
                            e.onComplete();
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            });
        }

        return mLocalSongsRxCommand;
    }


}
