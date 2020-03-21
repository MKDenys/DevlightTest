package com.dk.devlighttest.model;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface MarvelCharactersProvider {

    MutableLiveData<List<MarvelCharacter>>
    getCharacters(final MutableLiveData<List<MarvelCharacter>> charactersLiveData, int limit, int offset);

    MutableLiveData<List<MarvelCharacter>>
    getCharacterByName(final MutableLiveData<List<MarvelCharacter>> charactersLiveData, String name);

    MutableLiveData<List<MarvelCharacter>>
    getCharacterById(final MutableLiveData<List<MarvelCharacter>> charactersLiveData, long id);
}
