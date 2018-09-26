package com.alexbarcelo.oomployees.oompaDetail;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.oomployees.R;
import com.alexbarcelo.oomployees.dialogs.ErrorDialogFragment;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class OompaDetailFragment extends DaggerFragment implements OompaDetailContract.View {

    public final static String ARGUMENT_OOMPA_ID = "OOMPA_ID";

    @Inject
    OompaDetailContract.Presenter mPresenter;

    @BindView(R.id.detail_image) ImageView mImageView;
    @BindView(R.id.detail_fullname) TextView mFullnameView;
    @BindView(R.id.detail_age) TextView mAgeView;
    @BindView(R.id.detail_country) TextView mCountryView;
    @BindView(R.id.detail_favorite_color) TextView mFavoriteColorView;
    @BindView(R.id.detail_favorite_food) TextView mFavoriteFoodView;
    @BindView(R.id.detail_description) TextView mDescriptionView;
    @BindView(R.id.detail_gender) TextView mGenderView;
    @BindView(R.id.detail_height) TextView mHeightView;
    @BindView(R.id.detail_profession) TextView mProfessionView;
    @BindView(R.id.detail_email) TextView mEmailView;
    @BindView(R.id.detail_view) View mDetailView;
    @BindView(R.id.detail_retry_button) View mRetryButton;
    @BindView(R.id.detail_loading_indicator) View mLoadingIndicator;

    private long mOompaId;

    @Inject
    public OompaDetailFragment() {

    }

    public static OompaDetailFragment newInstance(Long oompaId) {
        OompaDetailFragment fragment = new OompaDetailFragment();

        Bundle args = new Bundle();
        args.putLong(ARGUMENT_OOMPA_ID, oompaId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mOompaId = getArguments().getLong(ARGUMENT_OOMPA_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_oompa_detail, container, false);

        ButterKnife.bind(this, v);

        mEmailView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, ((TextView) view).getText());
            if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        mRetryButton.setOnClickListener(view -> {
            OompaDetailFragment.this.setRetryButton(false);
            mPresenter.loadDetail(mOompaId);
        });

        mDetailView.setVisibility(View.INVISIBLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
        if (mRetryButton.getVisibility() == View.INVISIBLE)
            mPresenter.loadDetail(mOompaId);
    }

    @Override
    public void onDestroy() {
        mPresenter.dropView();
        super.onDestroy();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mLoadingIndicator.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setRetryButton(boolean active) {
        mRetryButton.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showErrorMessage(String message) {
        ErrorDialogFragment dialogFragment = ErrorDialogFragment.newInstance(message);
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), null);
    }

    @Override
    public void setFullName(String text) {
        mFullnameView.setText(text);
    }

    @Override
    public void setGender(String text) {
        mGenderView.setText(text);
    }

    @Override
    public void setFavoriteFood(String text) {
        mFavoriteFoodView.setText(text);
    }

    @Override
    public void setFavoriteColor(String text) {
        mFavoriteColorView.setText(text);
    }

    @Override
    public void setProfession(String text) {
        mProfessionView.setText(text);
    }

    @Override
    public void setEmail(String text) {
        mEmailView.setText(text);
    }

    @Override
    public void setAge(String text) {
        mAgeView.setText(text);
    }

    @Override
    public void setCountry(String text) {
        mCountryView.setText(text);
    }

    @Override
    public void setHeight(String text) {
        mHeightView.setText(text);
    }

    @Override
    public void setDescription(String text) {
        mDescriptionView.setText(text);
    }

    @Override
    public void setImage(Drawable image) {
        mImageView.setImageDrawable(image);
    }

    @Override
    public void setVisible(boolean active) {
        mDetailView.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }
}