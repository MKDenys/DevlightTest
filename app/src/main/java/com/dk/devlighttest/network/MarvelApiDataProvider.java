package com.dk.devlighttest.network;

import androidx.lifecycle.MutableLiveData;

import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.model.MarvelCharactersProvider;
import com.dk.devlighttest.model.json.arrays.MarvelResponseResult;
import com.dk.devlighttest.model.json.objects.MarvelCharacterJson;
import com.dk.devlighttest.model.json.objects.MarvelResponseData;
import com.dk.devlighttest.utils.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarvelApiDataProvider implements MarvelCharactersProvider {
    private static final int MAX_TRY_COUNT = 5;
    private int tryCounter;
    private MarvelApi marvelApi;

    public MarvelApiDataProvider(){
        marvelApi = App.getInstance().getMarvelApi();
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacters(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, final int limit, final int offset){
        marvelApi.getCharacters(limit, offset).enqueue(new Callback<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>>() {
            @Override
            public void onResponse(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> call,
                                   Response<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> response) {
                if (response.isSuccessful()){
                    tryCounter = 0;
                    List<MarvelCharacter> marvelCharacters = new ArrayList<>();
                    for (int i = 0; i < response.body().getData().getResults().size(); i++){
                        marvelCharacters.add(response.body().getData().getResults().get(i).convertToMarvelCharacter());
                    }
                    charactersLiveData.setValue(marvelCharacters);
                }
            }

            @Override
            public void onFailure(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> call, Throwable t) {
                tryCounter++;
                if (tryCounter < MAX_TRY_COUNT){
                    getCharacters(charactersLiveData, limit, offset);
                }
                charactersLiveData.setValue(Collections.<MarvelCharacter>emptyList());

            }
        });
        return charactersLiveData;
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacterByName(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, final String name) {
        marvelApi.getCharacterByName(name).enqueue(new Callback<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>>() {
            @Override
            public void onResponse(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> call,
                                   Response<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> response) {
                if (response.isSuccessful()){
                    tryCounter = 0;
                    List<MarvelCharacter> marvelCharacters = new ArrayList<>();
                    for (int i = 0; i < response.body().getData().getResults().size(); i++){
                        marvelCharacters.add(response.body().getData().getResults().get(i).convertToMarvelCharacter());
                    }
                    charactersLiveData.setValue(marvelCharacters);
                }
            }

            @Override
            public void onFailure(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> call, Throwable t) {
                tryCounter++;
                if (tryCounter < MAX_TRY_COUNT){
                    getCharacterByName(charactersLiveData, name);
                }
                charactersLiveData.setValue(Collections.<MarvelCharacter>emptyList());
            }
        });
        return charactersLiveData;
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacterById(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, final long id) {
        marvelApi.getCharacterById(id).enqueue(new Callback<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>>() {
            @Override
            public void onResponse(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> call,
                                   Response<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> response) {
                if (response.isSuccessful()){
                    tryCounter = 0;
                    List<MarvelCharacter> marvelCharacters = new ArrayList<>();
                    marvelCharacters.add(response.body().getData().getResults().get(0).convertToMarvelCharacter());
                    charactersLiveData.setValue(marvelCharacters);
                }
            }

            @Override
            public void onFailure(Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>> call, Throwable t) {
                tryCounter++;
                if (tryCounter < MAX_TRY_COUNT){
                    getCharacterById(charactersLiveData, id);
                }
                charactersLiveData.setValue(Collections.<MarvelCharacter>emptyList());
            }
        });
        return charactersLiveData;
    }
}
