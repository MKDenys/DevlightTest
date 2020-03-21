package com.dk.devlighttest.model.json.objects;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.model.json.arrays.Comics;
import com.dk.devlighttest.model.json.arrays.MarvelUrl;
import com.dk.devlighttest.model.json.arrays.Series;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "characters")
public class MarvelCharacterJson {
    @PrimaryKey
    @SerializedName("id")
    private long id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "comics_count")
    private int comicsCount;

    @ColumnInfo(name = "series_count")
    private int seriesCount;

    @Ignore
    @SerializedName("comics")
    private ComicsCollection comics;

    @Ignore
    @SerializedName("series")
    private SeriesCollection series;

    @Ignore
    @SerializedName("urls")
    private List<MarvelUrl> links;

    @Embedded
    @SerializedName("thumbnail")
    private CharacterImage image;

    public MarvelCharacterJson(long id, String name, String description, int comicsCount, int seriesCount, CharacterImage image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.comicsCount = comicsCount;
        this.seriesCount = seriesCount;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getComicsCount() {
        return comicsCount;
    }

    public void setComicsCount(int comicsCount) {
        this.comicsCount = comicsCount;
    }

    public int getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(int seriesCount) {
        this.seriesCount = seriesCount;
    }

    public ComicsCollection getComics() {
        return comics;
    }

    public void setComics(ComicsCollection comics) {
        this.comics = comics;
        this.comicsCount = comics.getCount();
    }

    public SeriesCollection getSeries() {
        return series;
    }

    public void setSeries(SeriesCollection series) {
        this.series = series;
        this.seriesCount = series.getCount();
    }

    public List<MarvelUrl> getLinks() {
        return links;
    }

    public void setLinks(List<MarvelUrl> links) {
        this.links = links;
    }

    public CharacterImage getImage() {
        return image;
    }

    public void setImage(CharacterImage image) {
        this.image = image;
    }

    public MarvelCharacter convertToMarvelCharacter(){
        List<Comics> comics = getComics().getItems();
        List<Series> series = getSeries().getItems();
        MarvelCharacter marvelCharacter = new MarvelCharacter(id,
                name, description,
                getComics().getCount(), getSeries().getCount(),
                comics, series, links, image);
        return marvelCharacter;
    }
}
