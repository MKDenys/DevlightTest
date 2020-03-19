package com.dk.devlighttest.model.json.objects;

import com.google.gson.annotations.SerializedName;

public class MarvelResponseData<T> {
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }
}
