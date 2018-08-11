package com.alexbarcelo.oomployees.oompaDetail;

import android.os.Bundle;

import com.alexbarcelo.oomployees.R;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class OompaDetailActivity extends DaggerAppCompatActivity {

    public static final String EXTRA_OOMPA_ID = "com.alexbarcelo.oomployees.OOMPA_ID";

    @Inject
    OompaDetailFragment injectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oompa_detail);

        OompaDetailFragment oompaDetailFragment = (OompaDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);

        if (oompaDetailFragment == null) {
            oompaDetailFragment = injectedFragment;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, oompaDetailFragment)
                    .commit();
        }
    }
}
