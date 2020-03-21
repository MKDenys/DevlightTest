package com.dk.devlighttest.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dk.devlighttest.database.DBMarvelCharacterProvider;
import com.dk.devlighttest.network.MarvelApiDataProvider;

import java.util.List;

public class MainActivityViewModel extends ViewModel{
    private MutableLiveData<List<MarvelCharacter>> charactersLiveData;
    private MarvelCharactersProvider dataProvider;
    private ChangeType changeType;
    private boolean internetConnectionStatus = true;

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

    public MutableLiveData<List<MarvelCharacter>> getCharactersRepository() {
        return charactersLiveData;
    }

    public void loadCharacters(int limit, int offset){
        charactersLiveData = dataProvider.getCharacters(charactersLiveData, limit, offset);
    }

    public void loadCharacterByName(String name){
        charactersLiveData = dataProvider.getCharacterByName(charactersLiveData, name);
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public void setInternetConnectionStatus(boolean internetConnectionStatus) {
        this.internetConnectionStatus = internetConnectionStatus;
        setDataProvider();
    }

    public boolean isInternetConnectionStatus() {
        return internetConnectionStatus;
    }
}
