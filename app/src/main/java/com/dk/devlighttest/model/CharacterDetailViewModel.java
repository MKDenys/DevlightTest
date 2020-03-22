package com.dk.devlighttest.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dk.devlighttest.database.DBMarvelCharacterProvider;
import com.dk.devlighttest.network.MarvelApiDataProvider;

import java.util.List;

public class CharacterDetailViewModel extends ViewModel {
    private MutableLiveData<List<MarvelCharacter>> charactersLiveData = new MutableLiveData<>();
    private MarvelCharactersProvider dataProvider;
    private boolean internetConnectionStatus;

    public void init(boolean internetConnectionStatus){
        this.internetConnectionStatus = internetConnectionStatus;
        if (charactersLiveData == null){
            charactersLiveData = new MutableLiveData<>();
        }
        setDataProvider();
    }

    private void setDataProvider(){
        if (internetConnectionStatus){
            dataProvider = new MarvelApiDataProvider();
        } else {
            dataProvider = new DBMarvelCharacterProvider();
            //dataProvider = new FakeDataProvider();
        }
    }

    public void loadCharacterById(long id){
        charactersLiveData = dataProvider.getCharacterById(charactersLiveData, id);
    }

    public MutableLiveData<List<MarvelCharacter>> getCharacterRepository() {
        return charactersLiveData;
    }
}
