package com.alexbarcelo.oomployees.oompaDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alexbarcelo.oomployees.GlideApp;
import com.alexbarcelo.oomployees.R;
import com.alexbarcelo.oomployees.data.model.Oompa;
import com.alexbarcelo.oomployees.data.source.OompaRepository;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableSingleObserver;

import static com.alexbarcelo.oomployees.di.RxModule.BACKGROUND_SCHEDULER_NAME;
import static com.alexbarcelo.oomployees.di.RxModule.MAIN_SCHEDULER_NAME;

/**
 * Interficies que definen el contrato para las capas de vista y presentador para la lista de Oompa-Loompas
 */
public class OompaDetailPresenter implements OompaDetailContract.Presenter {

    private OompaDetailContract.View mView;
    private OompaRepository mRepository;
    private Scheduler mMainScheduler;
    private Scheduler mBackgroundScheduler;
    private Context mContext;

    @Inject
    OompaDetailPresenter(@NonNull OompaRepository repository,
                         @NonNull @Named(BACKGROUND_SCHEDULER_NAME) Scheduler backgroundScheduler,
                         @NonNull @Named(MAIN_SCHEDULER_NAME) Scheduler mainScheduler,
                         @NonNull Context context) {
        this.mRepository = repository;
        this.mBackgroundScheduler = backgroundScheduler;
        this.mMainScheduler = mainScheduler;
        this.mContext = context;
    }

    @Override
    public void loadDetail(long id) {
        mView.setLoadingIndicator(true);

        DisposableSingleObserver<Oompa> mCurrentRequest = new DisposableSingleObserver<Oompa>() {

            @Override
            public void onSuccess(Oompa oompa) {
                showDetail(oompa);
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
                mView.showErrorMessage(e.getMessage());
                mView.setRetryButton(true);
            }
        };

        mRepository.getOompa(id)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(mCurrentRequest);
    }

    @Override
    public void takeView(OompaDetailContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @SuppressLint("DefaultLocale")
    private void showDetail(Oompa oompa) {
        mView.setFullName(String.format("%s, %s", oompa.lastName(), oompa.firstName()));
        GlideApp.with(mContext.getApplicationContext())
                .load(oompa.image())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mView.setImage(resource);
                    }
                });
        mView.setAge(String.format("%d years old", oompa.age()));
        mView.setProfession(oompa.profession());
        mView.setCountry(oompa.country());
        mView.setDescription(oompa.description());
        mView.setEmail(oompa.email());
        String food = oompa.favorite().food();
        if (!food.isEmpty())
            mView.setFavoriteFood(food.substring(0, 1).toUpperCase() + food.substring(1));
        String color = oompa.favorite().color();
        if (!color.isEmpty())
            mView.setFavoriteColor(color.substring(0, 1).toUpperCase() + color.substring(1));
        mView.setGender(oompa.gender().equals("F") ? "Female" : "Male");
        mView.setHeight(String.format("%d cm", oompa.height()));
        mView.setVisible(true);
    }
}
