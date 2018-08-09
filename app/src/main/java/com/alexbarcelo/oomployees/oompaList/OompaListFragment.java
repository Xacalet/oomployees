package com.alexbarcelo.oomployees.oompaList;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.oomployees.R;
import com.alexbarcelo.oomployees.data.model.Oompa;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class OompaListFragment extends DaggerFragment implements OompaListContract.View {

    @Inject
    OompaListContract.Presenter mPresenter;

    OompaListAdapter mListAdapter;

    @BindView(R.id.oompa_list_view)
    RecyclerView mOompaListView;

    @Inject
    public OompaListFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_oompa_list, container, false);

        ButterKnife.bind(this, v);

        mListAdapter = new OompaListAdapter(this.getActivity());
        mListAdapter.setOnRetryButtonClickListener(view -> mPresenter.loadMoreItems());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mOompaListView.setLayoutManager(linearLayoutManager);

        mOompaListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastItemIndex = mListAdapter.getItemCount() - 1;
                if (layoutManager.findLastVisibleItemPosition() == lastItemIndex) {
                    mPresenter.loadMoreItems();
                }
            }
        });

        mOompaListView.setAdapter(mListAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void loadItems(List<Oompa> oompaList) {
        mListAdapter.setItems(oompaList);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mListAdapter.showLoadingIndicator(active);
    }

    @Override
    public void setRetryButton(boolean active) {
        mListAdapter.showRetryButton(active);
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
