package com.dk.devlighttest.model.json.arrays;

import com.google.gson.annotations.SerializedName;

public abstract class MarvelResource {
    @SerializedName("name")
    private String name;
    @SerializedName("resourceURI")
    private String detailUrl;

    public String getName() {
        return name;
    }

    public String getDetailUrl() {
        return detailUrl;
    }
}
