package com.alexbarcelo.oomployees.oompaDetail;

import android.support.annotation.NonNull;

import com.alexbarcelo.oomployees.data.model.Oompa;
import com.alexbarcelo.oomployees.data.source.OompaRepository;

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

    @Inject
    OompaDetailPresenter(@NonNull OompaRepository repository,
                         @NonNull @Named(BACKGROUND_SCHEDULER_NAME) Scheduler backgroundScheduler,
                         @NonNull @Named(MAIN_SCHEDULER_NAME) Scheduler mainScheduler) {
        this.mRepository = repository;
        this.mBackgroundScheduler = backgroundScheduler;
        this.mMainScheduler = mainScheduler;
    }

    @Override
    public void loadDetail(long id) {

        mView.setLoadingIndicator(true);

        DisposableSingleObserver mCurrentRequest = new DisposableSingleObserver<Oompa>(){

            @Override
            public void onSuccess(Oompa oompa) {
                //TO-DO: Populate data
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
}
