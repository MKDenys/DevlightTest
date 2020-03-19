package com.dk.devlighttest.model.json.objects;


import com.dk.devlighttest.model.json.arrays.MarvelUrl;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarvelCharacter {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("comics")
    private ComicsCollection comics;
    @SerializedName("series")
    private SeriesCollection series;
    @SerializedName("urls")
    private List<MarvelUrl> links;
    @SerializedName("thumbnail")
    private CharacterImage image;

    public String getName() {
        return name;
    }

    public ComicsCollection getComics() {
        return comics;
    }

    public SeriesCollection getSeries() {
        return series;
    }

    public CharacterImage getImage() {
        return image;
    }
}
