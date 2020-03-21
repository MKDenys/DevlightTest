package com.dk.devlighttest.database;


import androidx.lifecycle.MutableLiveData;

import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.model.MarvelCharactersProvider;
import com.dk.devlighttest.utils.App;

import java.util.List;

public class DBMarvelCharacterProvider implements MarvelCharactersProvider {
    private TransactionInsertDao transactionInsertDao;
    private MarvelCharacterDao marvelCharacterDao;

    public DBMarvelCharacterProvider() {
        transactionInsertDao = App.getInstance().getAppDatabase().transactionInsertDao();
        marvelCharacterDao = App.getInstance().getAppDatabase().marvelCharacterDao();
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacters(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, final int limit, final int offset) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MarvelCharacter> marvelCharacterList = marvelCharacterDao.getAll();
                charactersLiveData.postValue(marvelCharacterList);
            }
        }).start();
        return charactersLiveData;
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacterByName(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MarvelCharacter> marvelCharacterList = marvelCharacterDao.getCharacterByName(name);
                charactersLiveData.postValue(marvelCharacterList);
            }
        }).start();
        return charactersLiveData;
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacterById(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, final long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MarvelCharacter> marvelCharacterList = marvelCharacterDao.getCharacterById(id);
                charactersLiveData.postValue(marvelCharacterList);
            }
        }).start();
        return charactersLiveData;
    }

    public void insertTask(MarvelCharacter marvelCharacter){
        new InsertTransactionAsyncTask(transactionInsertDao).execute(marvelCharacter);
    }
}
