package com.dk.devlighttest.model.json.arrays;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "links")
public class MarvelUrl implements Parcelable {

    @ColumnInfo(name = "character_id")
    private long characterId;

    @SerializedName("type")
    private String type;

    @PrimaryKey
    @NonNull
    @SerializedName("url")
    private String url;

    public MarvelUrl(long characterId, String type, @NonNull String url) {
        this.characterId = characterId;
        this.type = type;
        this.url = url;
    }

    protected MarvelUrl(Parcel in) {
        characterId = in.readLong();
        type = in.readString();
        url = Objects.requireNonNull(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(characterId);
        dest.writeString(type);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarvelUrl> CREATOR = new Creator<MarvelUrl>() {
        @Override
        public MarvelUrl createFromParcel(Parcel in) {
            return new MarvelUrl(in);
        }

        @Override
        public MarvelUrl[] newArray(int size) {
            return new MarvelUrl[size];
        }
    };

    public long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }
}
