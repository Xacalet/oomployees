
package com.alexbarcelo.oomployees.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Favorite {

    public abstract String color();

    public abstract String food();

    public abstract String randomString();

    public abstract String song();

    public static TypeAdapter<Favorite> typeAdapter(Gson gson) {
        return new AutoValue_Favorite.GsonTypeAdapter(gson);
    }
}
