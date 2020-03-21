package com.dk.devlighttest.database;

import android.os.AsyncTask;

import com.dk.devlighttest.model.MarvelCharacter;

public class InsertTransactionAsyncTask extends AsyncTask<MarvelCharacter, Void, Void> {

    private TransactionInsertDao transactionInsertDao;

    public InsertTransactionAsyncTask(TransactionInsertDao transactionInsertDao) {
        this.transactionInsertDao = transactionInsertDao;
    }

    @Override
    protected Void doInBackground(MarvelCharacter... marvelCharacters) {
        for (MarvelCharacter marvelCharacter : marvelCharacters) {
            transactionInsertDao.insertMarvelCharacter(marvelCharacter);
        }
        return null;
    }
}
