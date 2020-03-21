package com.dk.devlighttest.model.json.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class MarvelCollection<T> {
    @SerializedName("available")
    private int count;
    @SerializedName("items")
    private List<T> items;

    public MarvelCollection(int count, List<T> list) {
        this.count = count;
        items = list;
    }

    public List<T> getItems() {
        return items;
    }

    public int getCount() {
        return count;
    }
}
