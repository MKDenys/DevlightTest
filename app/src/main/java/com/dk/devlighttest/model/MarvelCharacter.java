package com.dk.devlighttest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import com.dk.devlighttest.model.json.arrays.Comics;
import com.dk.devlighttest.model.json.arrays.MarvelUrl;
import com.dk.devlighttest.model.json.arrays.Series;
import com.dk.devlighttest.model.json.objects.CharacterImage;
import com.dk.devlighttest.model.json.objects.ComicsCollection;
import com.dk.devlighttest.model.json.objects.MarvelCharacterJson;
import com.dk.devlighttest.model.json.objects.SeriesCollection;

import java.util.List;
import java.util.Objects;

public class MarvelCharacter implements Parcelable {

    private long id;
    private String name;
    private String description;
    @ColumnInfo(name = "comics_count")
    private int comicsCount;
    @ColumnInfo(name = "series_count")
    private int seriesCount;
    @Relation(entity = Comics.class, parentColumn = "id", entityColumn = "character_id")
    private List<Comics> comics;
    @Relation(entity = Series.class, parentColumn = "id", entityColumn = "character_id")
    private List<Series> series;
    @Relation(entity = MarvelUrl.class, parentColumn = "id", entityColumn = "character_id")
    private List<MarvelUrl> links;
    @Embedded
    private CharacterImage image;

    public MarvelCharacter(long id, String name, String description, int comicsCount, int seriesCount, List<Comics> comics, List<Series> series, List<MarvelUrl> links, CharacterImage image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.comicsCount = comicsCount;
        this.seriesCount = seriesCount;
        this.comics = comics;
        this.series = series;
        this.links = links;
        this.image = image;
    }

    @Ignore
    public MarvelCharacter() {

    }

    protected MarvelCharacter(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        comicsCount = in.readInt();
        seriesCount = in.readInt();
        comics = in.createTypedArrayList(Comics.CREATOR);
        series = in.createTypedArrayList(Series.CREATOR);
        links = in.createTypedArrayList(MarvelUrl.CREATOR);
        image = in.readParcelable(CharacterImage.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(comicsCount);
        dest.writeInt(seriesCount);
        dest.writeTypedList(comics);
        dest.writeTypedList(series);
        dest.writeTypedList(links);
        dest.writeParcelable(image, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarvelCharacter> CREATOR = new Creator<MarvelCharacter>() {
        @Override
        public MarvelCharacter createFromParcel(Parcel in) {
            return new MarvelCharacter(in);
        }

        @Override
        public MarvelCharacter[] newArray(int size) {
            return new MarvelCharacter[size];
        }
    };

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

    public List<Comics> getComics() {
        return comics;
    }

    public void setComics(List<Comics> comics) {
        this.comics = comics;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
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

    public MarvelCharacterJson convertToMarvelCharacterJson(){
        MarvelCharacterJson marvelCharacterJson = new MarvelCharacterJson(
                id, name, description, comicsCount, seriesCount, image);
        marvelCharacterJson.setComics(new ComicsCollection(comicsCount, comics));
        marvelCharacterJson.setSeries(new SeriesCollection(seriesCount, series));
        marvelCharacterJson.setLinks(links);
        return marvelCharacterJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarvelCharacter character = (MarvelCharacter) o;
        return id == character.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
