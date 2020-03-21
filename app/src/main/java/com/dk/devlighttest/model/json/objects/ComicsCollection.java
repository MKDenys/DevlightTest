package com.dk.devlighttest.model.json.objects;

import com.dk.devlighttest.model.json.arrays.Comics;

import java.util.List;

public class ComicsCollection extends MarvelCollection<Comics> {
    public ComicsCollection(int count, List<Comics> list) {
        super(count, list);
    }
}
