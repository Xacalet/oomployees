package com.alexbarcelo.oomployees.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Módulo dagger que proporciona los schedulers de RxJava
 */
@Module
public abstract class RxModule {

    public static final String MAIN_SCHEDULER_NAME = "main";
    public static final String BACKGROUND_SCHEDULER_NAME = "background";

    @Provides
    @Singleton
    @Named(MAIN_SCHEDULER_NAME)
    static Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @Named(BACKGROUND_SCHEDULER_NAME)
    static Scheduler provideIoScheduler() {
        return Schedulers.io();
    }
}
