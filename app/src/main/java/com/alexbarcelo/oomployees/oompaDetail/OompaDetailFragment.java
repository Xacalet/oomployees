package com.alexbarcelo.oomployees.oompaDetail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.oomployees.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class OompaDetailFragment extends DaggerFragment implements OompaDetailContract.View {

    @Inject
    OompaDetailContract.Presenter mPresenter;

    @Inject
    public OompaDetailFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_oompa_detail, container, false);

        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        mPresenter.dropView();
        super.onDestroy();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setRetryButton(boolean active) {

    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void setFullName(String text) {

    }

    @Override
    public void setGender(String text) {

    }

    @Override
    public void setFavoriteFood(String text) {

    }

    @Override
    public void setFavoriteColor(String text) {

    }

    @Override
    public void setProfession(String text) {

    }

    @Override
    public void setEmail(String text) {

    }

    @Override
    public void setAge(String text) {

    }

    @Override
    public void setCountry(String text) {

    }

    @Override
    public void setHeight(String text) {

    }

    @Override
    public void setDescription(String text) {

    }
}