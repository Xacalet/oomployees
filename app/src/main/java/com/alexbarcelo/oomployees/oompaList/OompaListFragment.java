package com.alexbarcelo.oomployees.oompaList;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.oomployees.R;
import com.alexbarcelo.oomployees.data.model.Oompa;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class OompaListFragment extends DaggerFragment implements OompaListContract.View {

    @Inject
    public OompaListFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oompa_list, container, false);
    }

    @Override
    public void loadItems(List<Oompa> oompaList) {

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
    public void openDetail(long id) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
