package com.alexbarcelo.oomployees.data.source;

import com.alexbarcelo.oomployees.data.model.Oompa;
import com.alexbarcelo.oomployees.data.model.PaginatedOompaList;

import io.reactivex.Single;

public interface OompaRepository {
    Single<PaginatedOompaList> getOompas(int page);

    Single<Oompa> getOompa(long id);
}
