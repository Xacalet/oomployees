package com.alexbarcelo.oomployees.data.source;

import com.alexbarcelo.oomployees.data.model.Oompa;
import com.alexbarcelo.oomployees.data.model.PaginatedOompaList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChocoFactoryAPI {

    @GET("/napptilus/oompa-loompas")
    Single<PaginatedOompaList> getOompas(@Query("page") int page);

    @GET("/napptilus/oompa-loompas/{id}")
    Single<Oompa> getOompa(@Path("id") long id);
}
