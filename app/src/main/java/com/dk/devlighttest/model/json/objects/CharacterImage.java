package com.dk.devlighttest.model.json.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "image")
public class CharacterImage implements Parcelable {
    private static final String MEDIUM_SIZE = "standard_large";
    private static final String LARGE_SIZE = "detail";
    private static final String DOT = ".";
    private static final String SEPARATOR = "/";

    @SerializedName("path")
    private String path;

    @SerializedName("extension")
    private String extension;

    public CharacterImage(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    private CharacterImage(Parcel in) {
        path = in.readString();
        extension = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(extension);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CharacterImage> CREATOR = new Creator<CharacterImage>() {
        @Override
        public CharacterImage createFromParcel(Parcel in) {
            return new CharacterImage(in);
        }

        @Override
        public CharacterImage[] newArray(int size) {
            return new CharacterImage[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    //Algorithm url build https://developer.marvel.com/documentation/images
    public String getUrlSmall() {
        return path + SEPARATOR + MEDIUM_SIZE + DOT + extension;
    }

    public String getUrlLarge() {
        return path + SEPARATOR + LARGE_SIZE + DOT + extension;
    }
}
