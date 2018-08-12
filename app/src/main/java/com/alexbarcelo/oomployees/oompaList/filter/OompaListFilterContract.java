package com.alexbarcelo.oomployees.oompaList.filter;

import com.alexbarcelo.commons.mvp.BasePresenter;
import com.alexbarcelo.commons.mvp.BaseView;

public interface OompaListFilterContract {

    interface View extends BaseView<OompaListFilterContract.Presenter> {
        void setGender(String gender);

        void setProfession(String gender);
    }

    interface Presenter extends BasePresenter<OompaListFilterContract.View> {
        void loadFilter();

        void saveFilter(String gender, String profession);
    }
}
