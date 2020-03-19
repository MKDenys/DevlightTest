package com.dk.devlighttest.model.json.arrays;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MarvelResponseResult<T> {
    @SerializedName("offset")
    private int offset;
    @SerializedName("results")
    private List<T> results = new ArrayList<>();

    public List<T> getResults() {
        return results;
    }

    public int getOffset() {
        return offset;
    }
}
