package com.dk.devlighttest.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dk.devlighttest.network.MarvelApiDataProvider;

import java.util.List;

public class CharacterDetailViewModel extends ViewModel {
    private MutableLiveData<List<MarvelCharacter>> charactersLiveData = new MutableLiveData<>();
    private MarvelCharactersProvider dataProvider;

    public void init(){
        if (charactersLiveData == null){
            charactersLiveData = new MutableLiveData<>();
        }
        dataProvider = new MarvelApiDataProvider();
    }

    public void loadCharacterById(long id){
        charactersLiveData = dataProvider.getCharacterById(charactersLiveData, id);
    }

    public MutableLiveData<List<MarvelCharacter>> getCharacterRepository() {
        return charactersLiveData;
    }
}
