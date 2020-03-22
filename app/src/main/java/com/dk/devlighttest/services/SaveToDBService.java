package com.dk.devlighttest.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.dk.devlighttest.database.DBMarvelCharacterProvider;
import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.utils.ImageLazyLoader;
import com.dk.devlighttest.utils.PicassoImageLazyLoader;

import java.util.ArrayList;
import java.util.List;

public class SaveToDBService extends IntentService {
    private static final String TAG = "SaveToDBService";
    private static final String CHARACTERS_KEY = "characters";

    public static Intent getIntentForCharacters(Context context, List<MarvelCharacter> marvelCharacters){
        Intent intent = new Intent(context, SaveToDBService.class);
        return intent.putParcelableArrayListExtra(CHARACTERS_KEY, (ArrayList<? extends Parcelable>) marvelCharacters);
    }

    public SaveToDBService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            if (intent.hasExtra(CHARACTERS_KEY)){
                List<MarvelCharacter> characters = intent.getParcelableArrayListExtra(CHARACTERS_KEY);
                DBMarvelCharacterProvider provider = new DBMarvelCharacterProvider();
                assert characters != null;
                for (int i = 0; i < characters.size(); i++){
                    provider.insertTask(characters.get(i));
                    cachingLargeImage(characters.get(i).getImage().getUrlLarge());
                }
            }
        }
    }

    private void cachingLargeImage(String imageUrl){
        ImageLazyLoader imageLazyLoader = new PicassoImageLazyLoader(this);
        imageLazyLoader.cachingImage(imageUrl);
    }
}
