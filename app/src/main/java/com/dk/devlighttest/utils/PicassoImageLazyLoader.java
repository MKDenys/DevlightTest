package com.dk.devlighttest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoImageLazyLoader implements ImageLazyLoader {
    private static final String IMAGE_SAVED_MESSAGE = "image saved";
    private static final String IMAGE_NOT_SAVED_MESSAGE = "image not saved";
    private final Context context;

    public PicassoImageLazyLoader(Context context){
        this.context = context;
    }

    @Override
    public void loadCircleImageFromUrl(final String url, final ImageView target){
        Picasso.with(context).load(url).transform(new CircleTransform()).into(target);
    }

    @Override
    public void loadImageFromUrlWithCallback(String url, ImageView target, Callback callback) {
        Picasso.with(context).load(url).into(target, callback);
    }

    @Override
    public void cachingImage(final String url) {
        Picasso.with(context).load(url).fetch(new Callback() {
            @Override
            public void onSuccess() {
                Log.println( Log.INFO, IMAGE_SAVED_MESSAGE, url);
            }

            @Override
            public void onError() {
                Log.println( Log.INFO, IMAGE_NOT_SAVED_MESSAGE, url);
            }
        });
    }

    private static class CircleTransform implements Transformation {
        private static final String CIRCLE_KEY = "circle";

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return CIRCLE_KEY;
        }
    }
}
