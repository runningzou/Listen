package com.runningzou.listen.app;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by zouzhihao on 2017/10/22.
 */

public class RxBus {

    private static volatile RxBus sInstance;

    public static RxBus getInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    private Subject<Object> mSubject = PublishSubject.create().toSerialized();

    public void post(Object event) {
        mSubject.onNext(event);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mSubject.ofType(eventType);
    }

}
