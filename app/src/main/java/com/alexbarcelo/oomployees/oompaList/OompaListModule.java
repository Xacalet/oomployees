package com.alexbarcelo.oomployees.oompaList;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.commons.di.FragmentScoped;
import com.alexbarcelo.oomployees.oompaList.filter.OompaListFilterContract;
import com.alexbarcelo.oomployees.oompaList.filter.OompaListFilterFragment;
import com.alexbarcelo.oomployees.oompaList.filter.OompaListFilterPresenter;

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

    @FragmentScoped
    @ContributesAndroidInjector
    public abstract OompaListFilterFragment oompaListFilterFragment();

    @ActivityScoped
    @Binds
    public abstract OompaListContract.Presenter oompaListPresenter(OompaListPresenter presenter);

    @ActivityScoped
    @Binds
    public abstract OompaListFilterContract.Presenter oompaListFilterPresenter(OompaListFilterPresenter presenter);
}
