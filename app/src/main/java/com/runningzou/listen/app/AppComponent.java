package com.runningzou.listen.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by zouzhihao on 2017/10/18.
 */

@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                AppModule.class,
                ActivityAndroidInjectorBuilder.class
        }
)
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(ListenApp app);
}
