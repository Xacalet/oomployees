package com.alexbarcelo.oomployees.oompaList.filter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.alexbarcelo.oomployees.R;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatDialogFragment;

public class OompaListFilterFragment extends DaggerAppCompatDialogFragment implements OompaListFilterContract.View {

    @BindView(R.id.gender_spinner)
    AppCompatSpinner mGenderSpinner;

    @BindView(R.id.profession_spinner)
    AppCompatSpinner mProfessionSpinner;

    @BindView(R.id.apply_button)
    Button mApplyButton;

    @BindView(R.id.cancel_button)
    Button mCancelButton;

    @Inject
    OompaListFilterContract.Presenter mPresenter;

    @Inject
    Context mContext;

    private ArrayAdapter<CharSequence> mProfessionArrayAdapter;

    public OompaListFilterFragment() { }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragment_oompa_list_filter, null);

        ButterKnife.bind(this, v);

        ArrayAdapter<CharSequence> genderArrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.genders, android.R.layout.simple_spinner_item);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(genderArrayAdapter);

        mProfessionArrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.professions, android.R.layout.simple_spinner_item);
        mProfessionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProfessionSpinner.setAdapter(mProfessionArrayAdapter);

        mApplyButton.setOnClickListener(view -> {
            mPresenter.saveFilter(getGender(), getProfession());
            Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
            dismiss();
        });

        mCancelButton.setOnClickListener(view -> {
            Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
            dismiss();
        });

        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(v)
                .create();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mPresenter.dropView();
        super.onDestroy();
    }

    @Override
    public void setGender(String gender) {
        switch (gender) {
            case "":
                mGenderSpinner.setSelection(0);
                break;
            case "F":
                mGenderSpinner.setSelection(1);
                break;
            default:
                mGenderSpinner.setSelection(2);
                break;
        }
    }

    @Override
    public void setProfession(String profession) {
        if (profession.equals("")) {
            mProfessionSpinner.setSelection(0);
        } else {
            mProfessionSpinner.setSelection(mProfessionArrayAdapter.getPosition(profession));
        }
    }

    private String getGender() {
        switch (mGenderSpinner.getSelectedItemPosition()) {
            case 0:
                return "";
            case 1:
                return "F";
            default:
                return "M";
        }
    }

    private String getProfession() {
        if (mProfessionSpinner.getSelectedItemPosition() == 0) {
            return "";
        } else {
            return (String) mProfessionSpinner.getSelectedItem();
        }
    }
}
