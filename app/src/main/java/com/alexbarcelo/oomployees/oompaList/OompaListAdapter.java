package com.alexbarcelo.oomployees.oompaList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alexbarcelo.oomployees.R;
import com.alexbarcelo.oomployees.data.model.Oompa;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OompaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Oompa> mOompas = new ArrayList<>();
    private Context mContext;
    private FooterItemType mFooterItemType = FooterItemType.NONE;
    private View.OnClickListener mOnRetryButtonClickListener;

    public OompaListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == FooterItemType.NONE.ViewType) {
            return new EmptyViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
        } else if (viewType == FooterItemType.LOADING_INDICATOR.ViewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.list_loading_indicator_item, parent, false));
        } else if (viewType == FooterItemType.RETRY_BUTTON.ViewType) {
            return new RetryButtonHolder(inflater.inflate(R.layout.list_retry_button_item, parent, false));
        } else {
            return new OompaViewHolder(inflater.inflate(R.layout.list_oompa_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount() - 1) {
            Oompa oompa = mOompas.get(position);
            OompaViewHolder oompaViewHolder = (OompaViewHolder) holder;
            oompaViewHolder.setName(oompa.firstName());
        } else if (holder.getItemViewType() == FooterItemType.RETRY_BUTTON.ViewType) {
            RetryButtonHolder retryButtonHolder = (RetryButtonHolder) holder;
            retryButtonHolder.setOnRetryButtonClickListener(mOnRetryButtonClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return mFooterItemType.ViewType;
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return mOompas.size() + 1;
    }

    public void setItems(List<Oompa> oompas) {
        mOompas  = new ArrayList<>(oompas);
        notifyDataSetChanged();
    }

    public void showLoadingIndicator(boolean active) {
        mFooterItemType = active ? FooterItemType.LOADING_INDICATOR : FooterItemType.NONE;
        notifyItemChanged(getItemCount() - 1);
    }

    public void showRetryButton(boolean active) {
        mFooterItemType = active ? FooterItemType.RETRY_BUTTON : FooterItemType.NONE;
        notifyItemChanged(getItemCount() - 1);
    }

    public void setOnRetryButtonClickListener(View.OnClickListener listener) {
        mOnRetryButtonClickListener = listener;
    }

    private enum FooterItemType {
        NONE, LOADING_INDICATOR, RETRY_BUTTON;

        private int ViewType;

        static {
            NONE.ViewType = 0;
            LOADING_INDICATOR.ViewType = 1;
            RETRY_BUTTON.ViewType = 2;
        }
    }

    public class OompaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.oompa_name) TextView mNameView;

        public OompaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setName(String text) {
            mNameView.setText(text);
        }
    }

    public class RetryButtonHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.retry_button) Button mRetryButton;

        public RetryButtonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setOnRetryButtonClickListener(View.OnClickListener listener) {
            mRetryButton.setOnClickListener(listener);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
