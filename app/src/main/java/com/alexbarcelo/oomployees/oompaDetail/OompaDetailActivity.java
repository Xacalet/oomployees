package com.alexbarcelo.oomployees.oompaDetail;

import android.content.Intent;
import android.os.Bundle;

import com.alexbarcelo.oomployees.R;

import dagger.android.support.DaggerAppCompatActivity;

public class OompaDetailActivity extends DaggerAppCompatActivity {

    public static final String EXTRA_OOMPA_ID = "com.alexbarcelo.oomployees.OOMPA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oompa_detail);

        Intent intent = getIntent();
        Long oompaId = intent.getLongExtra(EXTRA_OOMPA_ID, -1);

        OompaDetailFragment oompaDetailFragment = (OompaDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);

        if (oompaDetailFragment == null) {
            oompaDetailFragment = OompaDetailFragment.newInstance(oompaId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, oompaDetailFragment)
                    .commit();
        }
    }
}
