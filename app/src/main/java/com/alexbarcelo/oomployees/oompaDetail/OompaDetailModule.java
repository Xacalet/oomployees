package com.alexbarcelo.oomployees.oompaDetail;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.commons.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Módulo Dagger que proporciona las dependencias para la pantalla con la lista de Oompa-Loompas
 */
@Module
public abstract class OompaDetailModule {

    @FragmentScoped
    @ContributesAndroidInjector
    public abstract OompaDetailFragment oompaDetailFragment();

    @ActivityScoped
    @Binds
    public abstract OompaDetailContract.Presenter oompaDetailPresenter(OompaDetailPresenter presenter);
}
