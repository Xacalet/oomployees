package com.alexbarcelo.oomployees.di;

import android.app.Application;

import com.alexbarcelo.commons.di.CustomApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Componente principal de Dagger para la app
 */
@Singleton
@Component(modules = {
        AppModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        RxModule.class})
public interface AppComponent extends AndroidInjector<CustomApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}