package com.dk.devlighttest.model.json.objects;

import com.dk.devlighttest.model.json.arrays.Series;

import java.util.List;

public class SeriesCollection extends MarvelCollection<Series> {
    public SeriesCollection(int count, List<Series> list) {
        super(count, list);
    }
}
