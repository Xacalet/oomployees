package com.alexbarcelo.oomployees.di;

import android.app.Application;

import com.alexbarcelo.commons.net.AutoValueGsonFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Módulo Dagger que proporciona las dependencias necesarias para crear el cliente REST
 */
@Module
abstract class NetworkModule {

    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static final String API_URL = "https://2q2woep105.execute-api.eu-west-1.amazonaws.com/";

    @Provides
    @Singleton
    static Cache provideHttpCache(Application application) {
        return new Cache(application.getCacheDir(), CACHE_SIZE);
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapterFactory(AutoValueGsonFactory.create());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_URL)
                .client(okHttpClient)
                .build();
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        int maxAge = 60; // read from cache for 1 minute
        return originalResponse.newBuilder()
                .header("Cache-Control", "public, max-age=" + maxAge)
                .build();
    };
}
