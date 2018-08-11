package com.alexbarcelo.oomployees.oompaList.filter;

import javax.inject.Inject;

public class OompaListFilterPresenter implements OompaListFilterContract.Presenter {

    private OompaListFilterContract.View mView;
    private OompaListFilter mFilter;

    @Inject
    OompaListFilterPresenter(OompaListFilter filter){
        mFilter = filter;
    }

    @Override
    public void loadFilter() {
        mView.setGender(mFilter.getGender());
        mView.setProfession(mFilter.getProfession());
    }

    @Override
    public void saveFilter(String gender, String profession) {
        mFilter.setGender(gender);
        mFilter.setProfession(profession);
    }

    @Override
    public void takeView(OompaListFilterContract.View view) {
        mView = view;
        loadFilter();
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
