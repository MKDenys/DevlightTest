package com.dk.devlighttest.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.model.json.arrays.Comics;
import com.dk.devlighttest.model.json.arrays.MarvelUrl;
import com.dk.devlighttest.model.json.arrays.Series;
import com.dk.devlighttest.model.json.objects.MarvelCharacterJson;

import java.util.List;

@Dao
public abstract class TransactionInsertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertComics(List<Comics> comics);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSeries(List<Series> series);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMarvelUrl(List<MarvelUrl> marvelUrl);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertCharacterJson(MarvelCharacterJson... marvelCharactersJson);

    @Transaction
    public void insertMarvelCharacter(MarvelCharacter marvelCharacter){
        setCharacterIdForResource(marvelCharacter);
        insertComics(marvelCharacter.getComics());
        insertSeries(marvelCharacter.getSeries());
        insertMarvelUrl(marvelCharacter.getLinks());
        insertCharacterJson(marvelCharacter.convertToMarvelCharacterJson());
    }

    private void setCharacterIdForResource(MarvelCharacter marvelCharacter){
        for (int i = 0; i < marvelCharacter.getComics().size(); i++){
            marvelCharacter.getComics().get(i).setCharacterId(marvelCharacter.getId());
        }
        for (int i = 0; i < marvelCharacter.getSeries().size(); i++){
            marvelCharacter.getSeries().get(i).setCharacterId(marvelCharacter.getId());
        }
        for (int i = 0; i < marvelCharacter.getLinks().size(); i++){
            marvelCharacter.getLinks().get(i).setCharacterId(marvelCharacter.getId());
        }
    }

}
