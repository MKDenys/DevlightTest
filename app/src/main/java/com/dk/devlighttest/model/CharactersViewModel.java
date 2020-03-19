package com.dk.devlighttest.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dk.devlighttest.network.MarvelApiDataProvider;
import com.dk.devlighttest.model.json.objects.MarvelCharacter;

import java.util.List;

public class CharactersViewModel extends ViewModel{
    private MutableLiveData<List<MarvelCharacter>> charactersLiveData;
    private MarvelCharactersProvider dataProvider;
    private ChangeType changeType;

    public void init(){
        if (charactersLiveData != null){
            return;
        }
        dataProvider = MarvelApiDataProvider.getInstance();
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public LiveData<List<MarvelCharacter>> getCharactersRepository() {
        return charactersLiveData;
    }

    public void loadCharacters(int limit, int offset){
        charactersLiveData = dataProvider.getCharacters(limit, offset);
    }

    public void loadCharacterByName(String name){
        charactersLiveData = dataProvider.getCharacterByName(name);
    }
}
