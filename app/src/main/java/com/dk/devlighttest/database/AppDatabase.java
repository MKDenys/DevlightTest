package com.dk.devlighttest.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dk.devlighttest.model.json.arrays.Comics;
import com.dk.devlighttest.model.json.arrays.MarvelUrl;
import com.dk.devlighttest.model.json.arrays.Series;
import com.dk.devlighttest.model.json.objects.MarvelCharacterJson;

@Database(entities = {MarvelCharacterJson.class, Comics.class, Series.class, MarvelUrl.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MarvelCharacterDao marvelCharacterDao();
    public abstract TransactionInsertDao transactionInsertDao();
}
