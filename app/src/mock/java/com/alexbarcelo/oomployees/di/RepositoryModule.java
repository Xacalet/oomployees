package com.alexbarcelo.oomployees.di;

import com.alexbarcelo.oomployees.data.source.OompaMockRepository;
import com.alexbarcelo.oomployees.data.source.OompaRESTRepository;
import com.alexbarcelo.oomployees.data.source.OompaRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract OompaRepository provideOompaRepository(OompaMockRepository repo);
}
