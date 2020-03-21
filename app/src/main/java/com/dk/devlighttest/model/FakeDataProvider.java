package com.dk.devlighttest.model;

import androidx.lifecycle.MutableLiveData;

import com.dk.devlighttest.model.json.arrays.Comics;
import com.dk.devlighttest.model.json.arrays.MarvelUrl;
import com.dk.devlighttest.model.json.arrays.Series;
import com.dk.devlighttest.model.json.objects.CharacterImage;

import java.util.ArrayList;
import java.util.List;

public class FakeDataProvider implements MarvelCharactersProvider {
    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacters(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, int limit, int offset) {
        charactersLiveData.setValue(createMarvelCharacterList(limit, "Spider-Man"));
        return charactersLiveData;
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacterByName(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, String name) {
        charactersLiveData.setValue(createMarvelCharacterList(1, name));
        return charactersLiveData;
    }

    @Override
    public MutableLiveData<List<MarvelCharacter>> getCharacterById(
            final MutableLiveData<List<MarvelCharacter>> charactersLiveData, long id) {
        charactersLiveData.setValue(createMarvelCharacterList(1, "Spider-Man"));
        return charactersLiveData;
    }

    private List<MarvelCharacter> createMarvelCharacterList(int limit, String name){
        long id = 1009610;
        Comics comics1 = new Comics("Spider-Man: 101 Ways to End the Clone Saga (1997) #1", id);
        Comics comics2 = new Comics("2099 Alpha (2019) #1", id);
        Comics comics3 = new Comics("A Year of Marvels (Trade Paperback)", id);
        Series series1 = new Series("Superior Spider-Man Vol. 2: Otto-matic (2019)", id);
        Series series2 = new Series("2099 Alpha (2019)", id);
        Series series3 = new Series("A Year of Marvels (2017)", id);
        MarvelUrl url1 = new MarvelUrl(id, "detail", "http://marvel.com/characters/54/spider-man?utm_campaign=apiRef&utm_source=a7a92574023f1ea0d50bd5f5373b21ad");
        MarvelUrl url2 = new MarvelUrl(id, "wiki", "http://marvel.com/universe/Spider-Man_(Peter_Parker)?utm_campaign=apiRef&utm_source=a7a92574023f1ea0d50bd5f5373b21ad");
        MarvelUrl url3 = new MarvelUrl(id, "comiclink", "http://marvel.com/comics/characters/1009610/spider-man?utm_campaign=apiRef&utm_source=a7a92574023f1ea0d50bd5f5373b21ad");
        List<Comics> comics = new ArrayList<>();
        comics.add(comics1);
        comics.add(comics2);
        comics.add(comics3);
        List<Series> series = new ArrayList<>();
        series.add(series1);
        series.add(series2);
        series.add(series3);
        List<MarvelUrl> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);
        urls.add(url3);
        CharacterImage characterImage = new CharacterImage(
                "http://i.annihil.us/u/prod/marvel/i/mg/3/50/526548a343e4b", "jpg");
        String description = "Bitten by a radioactive spider, high school student Peter Parker gained the speed, strength and powers of a spider. Adopting the name Spider-Man, Peter hoped to start a career using his new abilities. Taught that with great power comes great responsibility, Spidey has vowed to use his powers to help people. ";
        List<MarvelCharacter> characterList = new ArrayList<>();
        for (int i = 0; i < limit; i++){
            characterList.add(new MarvelCharacter(1009610, name,
                    description, 5, 5, comics, series, urls, characterImage));
        }
        return characterList;
    }
}
