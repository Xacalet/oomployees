package com.alexbarcelo.oomployees.oompaList;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.oomployees.R;
import com.alexbarcelo.oomployees.data.model.Oompa;
import com.alexbarcelo.oomployees.dialogs.ErrorDialogFragment;
import com.alexbarcelo.oomployees.oompaDetail.OompaDetailActivity;
import com.alexbarcelo.oomployees.oompaList.filter.OompaListFilterFragment;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class OompaListFragment extends DaggerFragment implements OompaListContract.View {

    public static final int FILTER_DIALOG_FRAGMENT = 1;

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

        setHasOptionsMenu(true);

        mListAdapter = new OompaListAdapter(this.getActivity());
        mListAdapter.setOnItemClickListener(new OompaListAdapter.OnItemClickListener() {
            @Override
            public void onOompaClick(long id) {
                openDetail(id);
            }

            @Override
            public void onRetryButtonClick() {
                mPresenter.loadMoreItems(false);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mOompaListView.setLayoutManager(linearLayoutManager);

        /*
         * Comprobamos si el último elemento de la lista es visible, y en tal caso, pedimos más
         * ítems al presenter.
         */
        mOompaListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastVisibleItemPosition() == mListAdapter.getItemCount() - 1 &&
                        !mListAdapter.isRetryButtonActive()) {
                    /*
                     * Indicamos al presenter que cargue más ítems a través de la cola de mensajes,
                     * ya que en algunos casos, durante la ejecución del callback de scroll,
                     * la recycler view puede no aceptar cambios en los datos.
                     */
                    recyclerView.post(() -> mPresenter.loadMoreItems(false));
                }
            }
        });
        mOompaListView.setHasFixedSize(false);
        mOompaListView.setAdapter(mListAdapter);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_oompa_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.open_filter_menu_item) {
            openFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_DIALOG_FRAGMENT && resultCode == Activity.RESULT_OK) {
            mPresenter.loadMoreItems(true);
        }
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
        ErrorDialogFragment dialogFragment = ErrorDialogFragment.newInstance(message);
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), null);
    }

    @Override
    public void openDetail(long id) {
        Intent intent = new Intent(getActivity(), OompaDetailActivity.class);
        intent.putExtra(OompaDetailActivity.EXTRA_OOMPA_ID, id);
        startActivity(intent);
    }

    @Override
    public void openFilterDialog() {
        OompaListFilterFragment dialogFragment = new OompaListFilterFragment();
        dialogFragment.setTargetFragment(OompaListFragment.this, FILTER_DIALOG_FRAGMENT);
        dialogFragment.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
