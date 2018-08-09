package com.alexbarcelo.oomployees.oompaList;

import android.os.Bundle;

import com.alexbarcelo.oomployees.R;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class OompaListActivity extends DaggerAppCompatActivity {

    @Inject
    OompaListFragment injectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oompa_list);

        OompaListFragment oompaListFragment = (OompaListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);

        if (oompaListFragment == null) {
            oompaListFragment = injectedFragment;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, oompaListFragment)
                    .commit();
        }
    }
}
