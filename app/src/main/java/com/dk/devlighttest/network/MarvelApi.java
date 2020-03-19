package com.dk.devlighttest.network;

import com.dk.devlighttest.model.json.objects.MarvelCharacter;
import com.dk.devlighttest.model.json.objects.MarvelResponseData;
import com.dk.devlighttest.model.json.arrays.MarvelResponseResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelApi {
    String BASE_URL = "https://gateway.marvel.com/";

    @GET("/v1/public/characters/{id}")
    Call<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> getCharacterById(@Path("id") long characterId);

    @GET("/v1/public/characters")
    Call<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> getCharacters(@Query("limit") int limit, @Query("offset") int offset);

    @GET("/v1/public/characters")
    Call<MarvelResponseData<MarvelResponseResult<MarvelCharacter>>> getCharacterByName(@Query("name") String name);
}
