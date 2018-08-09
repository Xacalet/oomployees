
package com.alexbarcelo.oomployees.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Oompa {

    @SerializedName("first_name")
    public abstract String firstName();

    @SerializedName("last_name")
    public abstract String lastName();

    public abstract Favorite favorite();

    public abstract String gender();

    public abstract String image();

    public abstract String profession();

    public abstract String email();

    public abstract int age();

    public abstract String country();

    public abstract int height();

    public abstract long id();

    public static TypeAdapter<Oompa> typeAdapter(Gson gson) {
        return new AutoValue_Oompa.GsonTypeAdapter(gson);
    }
}
