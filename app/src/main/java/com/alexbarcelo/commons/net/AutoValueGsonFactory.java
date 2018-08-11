package com.alexbarcelo.commons.net;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * El plugin de Gson-AutoValue generará la implementación para esta clase abstracta que incluirá
 * los TypeAdapters de todas las clases anotadas con @AutoValue, lo que permite indicar una factory
 * en la instancia de Gson en lugar de tener que añadir el TypeAdapter de cada clase uno por uno.
 */
@GsonTypeAdapterFactory
public abstract class AutoValueGsonFactory implements TypeAdapterFactory {

    public static TypeAdapterFactory create() {
        return new AutoValueGson_AutoValueGsonFactory();
    }
}