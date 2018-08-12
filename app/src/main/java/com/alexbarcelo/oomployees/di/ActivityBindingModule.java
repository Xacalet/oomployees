package com.alexbarcelo.oomployees.di;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.oomployees.oompaDetail.OompaDetailActivity;
import com.alexbarcelo.oomployees.oompaDetail.OompaDetailModule;
import com.alexbarcelo.oomployees.oompaList.OompaListActivity;
import com.alexbarcelo.oomployees.oompaList.OompaListModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * ActivityBindingModule crea los subcomponentes para el componente Dagger asociado a este módulo,
 * en este caso, el componente AppComponent. Creamos un subcomponente para cada feature: la lista
 * de Oompa-Loompas y el detalle.
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = OompaListModule.class)
    abstract OompaListActivity oompaListActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = OompaDetailModule.class)
    abstract OompaDetailActivity oompaDetailActivity();
}
