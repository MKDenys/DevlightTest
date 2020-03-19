package com.dk.devlighttest.network;

import androidx.lifecycle.MutableLiveData;

import com.dk.devlighttest.utils.App;
import com.dk.devlighttest.model.MarvelCharactersProvider;
import com.dk.devlighttest.model.json.arrays.MarvelResponseResult;
import com.dk.devlighttest.model.json.objects.MarvelCharacter;
import com.dk.devlighttest.model.json.objects.MarvelResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarvelApiDataProvider implements MarvelCharactersProvider {
    private static MarvelApiDataProvider instance;

    public static MarvelApiDataProvider getInstance(){
        if (instance == null){
            instance = new MarvelApiDataProvider();
        }
        return instance;
    }

    private MarvelApi marvelApi;
    private MutableLiveData<List<MarvelCharacter>> charactersLiveData = new MutableLiveData<>();

    private MarvelApiDataProvider(){
        marvelApi = App.getMarvelApi();
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacters(int limit, int offset){
        marvelApi.getCharacters(limit, offset).enqueue(new Callback<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>>() {
            @Override
            public void onResponse(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> call,
                                   Response<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> response) {
                if (response.isSuccessful()){
                    charactersLiveData.setValue(response.body().getData().getResults());
                }
            }

            @Override
            public void onFailure(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> call, Throwable t) {
                charactersLiveData.setValue(null);
            }
        });
        return charactersLiveData;
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacterByName(String name) {
        marvelApi.getCharacterByName(name).enqueue(new Callback<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>>() {
            @Override
            public void onResponse(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> call,
                                   Response<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> response) {
                if (response.isSuccessful()){
                    charactersLiveData.setValue(response.body().getData().getResults());
                }
            }

            @Override
            public void onFailure(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> call, Throwable t) {
                charactersLiveData.setValue(null);
            }
        });
        return charactersLiveData;
    }
}
