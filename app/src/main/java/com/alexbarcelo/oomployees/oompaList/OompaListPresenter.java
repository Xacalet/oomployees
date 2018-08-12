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
 * Presenter para la vista que muestra una lista de oompa-loompas con filtro
 */
public class OompaListPresenter implements OompaListContract.Presenter {

    // Dependencias
    private OompaListContract.View mView;
    private OompaRepository mRepository;
    private Scheduler mMainScheduler;
    private Scheduler mBackgroundScheduler;

    // Control de paginación
    private int mCurrentPage = 0;
    private int mPageCount = Integer.MAX_VALUE;

    // Flags
    private boolean mIsLoading = false;
    private boolean mIsOnError = false;

    // Guardamos el observer de la petición en una variable miembro por si debemos cancelar
    // la suscripción en algun momento.
    private DisposableSingleObserver<PaginatedOompaList> mCurrentRequest;


    private OompaListFilter mListFilter;
    private List<Oompa> mOompaList = new ArrayList<>();

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

    /**
     * Busca ítems en el repositorio y actualiza la vista
     * @param forceRetry Fuerza la carga de ítems aun en caso de error en la última solicitud
     */
    @Override
    public void loadOompas(boolean forceRetry) {
        // mIsOnError fuerza a la vista a pulsar retry para recargar los datos. En caso de error,
        // el evento de scroll de la lista no hará nada.
        if (forceRetry) {
            mIsOnError = false;
            if (mView != null && mView.isActive()){
                mView.showRetryButton(false);
            }
        }

        if (mCurrentPage >= mPageCount || mIsLoading || mIsOnError) {
            return;
        }

        if (mView != null && mView.isActive()) {
            mView.showLoadingIndicator(true);
        }
        mIsLoading = true;

        mCurrentRequest = new DisposableSingleObserver<PaginatedOompaList>(){

            @Override
            public void onSuccess(PaginatedOompaList paginatedOompaList) {
                // Aumentamos en 1 la última página consultada, añadimos los resultados a la lista
                // y actualizamos la vista
                mCurrentPage = paginatedOompaList.current();
                mPageCount = paginatedOompaList.total();
                mOompaList.addAll(mListFilter.filter(paginatedOompaList.results()));
                if (mView != null && mView.isActive()){
                    mView.showOompas(mOompaList);
                    mView.showLoadingIndicator(false);
                }
                mIsLoading = false;
            }

            @Override
            public void onError(Throwable e) {
                // Activamos el flag de error en el presenter y mostramos mensaje de error
                // en la vista
                mIsOnError = true;
                if (mView != null && mView.isActive()){
                    mView.showLoadingIndicator(false);
                    mView.showErrorMessage(e.getMessage());
                    mView.showRetryButton(true);
                }
                mIsLoading = false;
            }
        };

        mRepository.getOompas(mCurrentPage + 1)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(mCurrentRequest);
    }

    /**
     * Resetemos los flags de paginación para volver a obtener la primera página, con el filtro
     * ya modificado
     */
    @Override
    public void applyFilter() {
        resetPageLoad();
        loadOompas(true);
    }

    /**
     * Asignamos la vista al presenter
     * @param view the view associated with this presenter
     */
    @Override
    public void takeView(OompaListContract.View view) {
        mView = view;
    }

    /**
     * Desvinculamos la vista del presenter
     */
    @Override
    public void dropView() {
        mView = null;
        if (mCurrentRequest != null)
            mCurrentRequest.dispose();
    }

    /**
     * Cancelamos cualquier solicitud en proceso, reseteamos los contadores de paginación y
     * vaciamos la lista de ítems (también en la lista) para poder iniciar otra búsqueda desde 0.
     */
    private void resetPageLoad() {
        if (mCurrentRequest != null)
            mCurrentRequest.dispose();
        mCurrentPage = 0;
        mPageCount = Integer.MAX_VALUE;
        mOompaList.clear();
        if (mView != null && mView.isActive()){
            mView.showOompas(new ArrayList<>());
        }

    }
}
