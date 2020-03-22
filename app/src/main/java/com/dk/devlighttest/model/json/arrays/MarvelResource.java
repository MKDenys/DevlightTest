package com.dk.devlighttest.model.json.arrays;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public abstract class MarvelResource implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "character_id")
    private long characterId;

    public MarvelResource(@NonNull String name, long characterId) {
        this.name = name;
        this.characterId = characterId;
    }

    public MarvelResource(Parcel in) {
        this.name = Objects.requireNonNull(in.readString());
        this.characterId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(characterId);
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }
}
