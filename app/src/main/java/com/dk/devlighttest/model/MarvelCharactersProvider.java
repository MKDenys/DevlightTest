package com.dk.devlighttest.model;

import androidx.lifecycle.MutableLiveData;

import com.dk.devlighttest.model.json.objects.MarvelCharacter;

import java.util.List;

public interface MarvelCharactersProvider {
    MutableLiveData<List<MarvelCharacter>> getCharacters(int limit, int offset);

    MutableLiveData<List<MarvelCharacter>> getCharacterByName(String name);
}
