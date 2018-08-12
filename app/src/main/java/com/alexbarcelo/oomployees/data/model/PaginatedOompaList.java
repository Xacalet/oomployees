
package com.alexbarcelo.oomployees.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

@AutoValue
public abstract class PaginatedOompaList {

    public abstract int current();

    public abstract int total();

    public abstract List<Oompa> results();

    public static TypeAdapter<PaginatedOompaList> typeAdapter(Gson gson) {
        return new AutoValue_PaginatedOompaList.GsonTypeAdapter(gson);
    }

    public static PaginatedOompaList create (int current, int total, List<Oompa> results) {
        return new AutoValue_PaginatedOompaList(current, total, results);
    }
}
