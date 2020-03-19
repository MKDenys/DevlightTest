package com.dk.devlighttest.model.json.arrays;

import com.google.gson.annotations.SerializedName;

public class MarvelUrl {
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
