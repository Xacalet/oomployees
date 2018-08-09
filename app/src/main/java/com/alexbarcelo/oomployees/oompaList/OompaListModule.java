package com.alexbarcelo.oomployees.oompaList;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.commons.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Módulo Dagger que proporciona las dependencias para la pantalla con la lista de Oompa-Loompas
 */
@Module
public abstract class OompaListModule {

    @FragmentScoped
    @ContributesAndroidInjector
    public abstract OompaListFragment oompaListFragment();

    @ActivityScoped
    @Binds
    public abstract OompaListContract.Presenter oompaListPresenter(OompaListPresenter presenter);
}
