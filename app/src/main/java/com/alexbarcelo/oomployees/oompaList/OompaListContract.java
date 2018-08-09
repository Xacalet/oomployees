package com.alexbarcelo.oomployees.oompaList;

import com.alexbarcelo.commons.mvp.BasePresenter;
import com.alexbarcelo.commons.mvp.BaseView;
import com.alexbarcelo.oomployees.data.model.Oompa;

import java.util.List;

/**
 * Interficies que definen el contrato para las capas de vista y presentador para la lista de Oompa-Loompas
 */
public interface OompaListContract {

    interface View extends BaseView<Presenter> {
        void loadItems(List<Oompa> oompaList);

        void setLoadingIndicator(boolean active);

        void setRetryButton(boolean active);

        void showErrorMessage(String message);

        void openDetail(long id);

        boolean isActive();
    }

    interface Presenter extends BasePresenter<View> {
        void loadMoreItems();
    }
}
