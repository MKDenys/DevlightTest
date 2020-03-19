package com.dk.devlighttest.database;

import androidx.lifecycle.MutableLiveData;

import com.dk.devlighttest.model.MarvelCharactersProvider;
import com.dk.devlighttest.model.json.objects.MarvelCharacter;

import java.util.List;

public class DBDataProvider implements MarvelCharactersProvider {

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacters(int limit, int offset) {
        return null;
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacterByName(String name) {
        return null;
    }
}
