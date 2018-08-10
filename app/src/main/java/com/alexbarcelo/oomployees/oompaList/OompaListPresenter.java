package com.alexbarcelo.oomployees.oompaList;

import android.support.annotation.NonNull;

import com.alexbarcelo.oomployees.data.model.Oompa;
import com.alexbarcelo.oomployees.data.model.PaginatedOompaList;
import com.alexbarcelo.oomployees.data.source.OompaRepository;
import com.alexbarcelo.oomployees.oompaList.filter.OompaListFilter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableSingleObserver;

import static com.alexbarcelo.oomployees.di.RxModule.BACKGROUND_SCHEDULER_NAME;
import static com.alexbarcelo.oomployees.di.RxModule.MAIN_SCHEDULER_NAME;

/**
 * Interficies que definen el contrato para las capas de vista y presentador para la lista de Oompa-Loompas
 */
public class OompaListPresenter implements OompaListContract.Presenter {

    private OompaListContract.View mView;
    private OompaRepository mRepository;
    private Scheduler mMainScheduler;
    private Scheduler mBackgroundScheduler;

    private int mCurrentPage = 0;
    private int mPageCount = Integer.MAX_VALUE;
    private boolean mIsLoading = false;
    private List<Oompa> mItems = new ArrayList<>();
    private DisposableSingleObserver mCurrentRequest;
    private OompaListFilter mListFilter;

    @Inject
    OompaListPresenter(@NonNull OompaRepository repository,
                              @NonNull @Named(BACKGROUND_SCHEDULER_NAME) Scheduler backgroundScheduler,
                              @NonNull @Named(MAIN_SCHEDULER_NAME) Scheduler mainScheduler,
                              @NonNull OompaListFilter listFilter) {
        this.mRepository = repository;
        this.mBackgroundScheduler = backgroundScheduler;
        this.mMainScheduler = mainScheduler;
        this.mListFilter = listFilter;
    }

    @Override
    public void loadMoreItems(boolean reload) {

        if (reload) {
            if (mCurrentRequest != null)
                mCurrentRequest.dispose();
            mIsLoading = false;
            mCurrentPage = 0;
            mItems.clear();
            mView.loadItems(new ArrayList<>());
        }

        if (mCurrentPage >= mPageCount || mIsLoading) {
            return;
        }

        mView.setLoadingIndicator(true);
        mIsLoading = true;

        mCurrentRequest = new DisposableSingleObserver<PaginatedOompaList>(){

            @Override
            public void onSuccess(PaginatedOompaList paginatedOompaList) {
                mCurrentPage = paginatedOompaList.current();
                mPageCount = paginatedOompaList.total();
                mItems.addAll(mListFilter.filter(paginatedOompaList.results()));
                mView.loadItems(mItems);
                mView.setLoadingIndicator(false);
                mIsLoading = false;
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
                mView.showErrorMessage(e.getMessage());
                mView.setRetryButton(true);
                mIsLoading = false;
            }
        };

        mRepository.getOompas(mCurrentPage + 1)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(mCurrentRequest);
    }

    @Override
    public void applyFilter(OompaListFilter filter) {
        mListFilter = filter;
    }

    @Override
    public void takeView(OompaListContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
