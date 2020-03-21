package com.dk.devlighttest.database;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.dk.devlighttest.model.MarvelCharacter;

import java.util.List;

@Dao
public interface MarvelCharacterDao {

    @Transaction
    @Query("SELECT * FROM characters ORDER BY name")
    List<MarvelCharacter> getAll();

    @Transaction
    @Query("SELECT * FROM characters ORDER BY name LIMIT :limit OFFSET :offset")
    List<MarvelCharacter> getCharacters(int limit, int offset);

    @Transaction
    @Query("SELECT * FROM characters WHERE name = :name")
    List<MarvelCharacter> getCharacterByName(String name);

    @Transaction
    @Query("SELECT * FROM characters WHERE id = :id")
    List<MarvelCharacter> getCharacterById(long id);
}
