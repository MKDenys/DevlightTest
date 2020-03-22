package com.dk.devlighttest.network;

import com.dk.devlighttest.model.json.arrays.MarvelResponseResult;
import com.dk.devlighttest.model.json.objects.MarvelCharacterJson;
import com.dk.devlighttest.model.json.objects.MarvelResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelApi {
    String BASE_URL = "https://gateway.marvel.com/";

    @GET("/v1/public/characters/{id}")
    Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>>
    getCharacterById(@Path("id") long characterId);

    @GET("/v1/public/characters")
    Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>>
    getCharacters(@Query("limit") int limit, @Query("offset") int offset);

    @GET("/v1/public/characters")
    Call<MarvelResponseData<MarvelResponseResult<MarvelCharacterJson>>>
    getCharacterByName(@Query("nameStartsWith") String name);
}
