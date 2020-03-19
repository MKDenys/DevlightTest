package com.dk.devlighttest.model.json.objects;

import com.google.gson.annotations.SerializedName;

public class CharacterImage {
    @SerializedName("path")
    private String path;
    @SerializedName("extension")
    private String extension;
    private static final String MEDIUM_SIZE = "standard_large";
    private static final String LARGE_SIZE = "standard_fantastic";
    private static final String DOT = ".";
    private static final String SEPARATOR = "/";

    //Algorithm url build https://developer.marvel.com/documentation/images
    public String getUrlSmall() {
        return path + SEPARATOR + MEDIUM_SIZE + DOT + extension;
    }

    public String getUrlLarge() {
        return path + SEPARATOR + LARGE_SIZE + DOT + extension;
    }
}
