package com.alexbarcelo.oomployees.oompaList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexbarcelo.oomployees.GlideApp;
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
    //    private View.OnClickListener mOnRetryButtonClickListener;
    private OnItemClickListener mOnItemClickListener;

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

            oompaViewHolder.setFullNameText(String.format("%s, %s", oompa.lastName(), oompa.firstName()));

            oompaViewHolder.setProfessionAndAgeText(String.format("%s, %d", oompa.profession(), oompa.age()));

            oompaViewHolder.setGender(oompa.gender());

            oompaViewHolder.mCardView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onOompaClick(oompa.id());
                }
            });

            Drawable drawable = DrawableCompat.wrap(oompaViewHolder.mGenderView.getBackground()).mutate();
            if (oompa.gender().equals("F")) {
                DrawableCompat.setTint(drawable, mContext.getResources().getColor(R.color.femaleColour));
            } else {
                DrawableCompat.setTint(drawable, mContext.getResources().getColor(R.color.maleColour));
            }

            GlideApp.with(mContext.getApplicationContext())
                    .load(oompa.image())
                    .into(oompaViewHolder.mImageView);

        } else if (holder.getItemViewType() == FooterItemType.RETRY_BUTTON.ViewType) {
            RetryButtonHolder retryButtonHolder = (RetryButtonHolder) holder;
            retryButtonHolder.mRetryButton.setOnClickListener(view -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onRetryButtonClick();
                }
            });
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

    //    public void setOnRetryButtonClickListener(View.OnClickListener listener) {
//        mOnRetryButtonClickListener = listener;
//    }
//
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public boolean isRetryButtonActive() {
        return mFooterItemType.ViewType == FooterItemType.RETRY_BUTTON.ViewType;
    }

    public interface OnItemClickListener {
        void onOompaClick(long id);

        void onRetryButtonClick();
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

        @BindView(R.id.oompa_card) RelativeLayout mCardView;
        @BindView(R.id.oompa_card_image) ImageView mImageView;
        @BindView(R.id.oompa_card_full_name) TextView mFullNameView;
        @BindView(R.id.oompa_card_profession_and_age) TextView mProfessionAndAgeView;
        @BindView(R.id.oompa_card_gender) TextView mGenderView;

        public OompaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setFullNameText(String text) {
            mFullNameView.setText(text);
        }

        public void setProfessionAndAgeText(String text) {
            mProfessionAndAgeView.setText(text);
        }

        public void setGender(String text) {
            mGenderView.setText(text);
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
