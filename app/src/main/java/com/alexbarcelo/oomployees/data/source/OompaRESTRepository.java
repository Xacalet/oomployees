package com.alexbarcelo.oomployees.data.source;

import com.alexbarcelo.oomployees.data.model.PaginatedOompaList;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Retrofit;

public class OompaRESTRepository implements OompaRepository {

    private ChocoFactoryAPI mRESTService;

    @Inject
    public OompaRESTRepository(Retrofit retrofit) {
        mRESTService = retrofit.create(ChocoFactoryAPI.class);
    }

    @Override
    public Single<PaginatedOompaList> getOompas(int page) {
        return mRESTService.getOompas(page);
    }
}
