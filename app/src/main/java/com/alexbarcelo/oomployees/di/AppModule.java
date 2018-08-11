package com.alexbarcelo.oomployees.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 * Módulo Dagger que proporciona contexto a toda la aplicación
 */
@Module
public abstract class AppModule {
    @Binds
    abstract Context bindContext(Application application);
}
