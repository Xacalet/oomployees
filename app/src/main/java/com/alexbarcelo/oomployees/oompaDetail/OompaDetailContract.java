package com.alexbarcelo.oomployees.oompaDetail;

import android.graphics.drawable.Drawable;

import com.alexbarcelo.commons.mvp.BasePresenter;
import com.alexbarcelo.commons.mvp.BaseView;
import com.alexbarcelo.oomployees.data.model.Oompa;
import com.alexbarcelo.oomployees.oompaList.filter.OompaListFilter;

import java.util.List;

/**
 * Interficies que definen el contrato para las capas de vista y presentador para la lista de Oompa-Loompas
 */
public interface OompaDetailContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void setRetryButton(boolean active);

        void showErrorMessage(String message);

        void setFullName(String text);

        void setGender(String text);

        void setFavoriteFood(String text);

        void setFavoriteColor(String text);

        void setProfession(String text);

        void setEmail(String text);

        void setAge(String text);

        void setCountry(String text);

        void setHeight(String text);

        void setDescription(String text);

        void setImage(Drawable image);

        void setVisible(boolean active);
    }

    interface Presenter extends BasePresenter<View> {
        void loadDetail(long id);
    }
}
