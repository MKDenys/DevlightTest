package com.dk.devlighttest.utils;

import android.widget.ImageView;

import com.squareup.picasso.Callback;

public interface ImageLazyLoader {
    void loadCircleImageFromUrl(String url, ImageView target);
    void loadImageFromUrlWithCallback(String url, ImageView target, Callback callback);
    void cachingImage(String url);
}
