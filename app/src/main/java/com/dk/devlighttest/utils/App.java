package com.dk.devlighttest.utils;

import android.app.Application;
import android.util.Log;

import com.dk.devlighttest.network.MarvelApi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String HASHING_ALGORITHM = "MD5";
    private static final String TIMESTAMP_KEY = "ts";
    private static final String APIKEY_KEY = "apikey";
    private static final String HASH_KEY = "hash";
    private static final String TAG = "MarvelApiManager";
    private static final String PUBLIC_KEY = "a7a92574023f1ea0d50bd5f5373b21ad";
    private static final String PRIVATE_KEY = "41fb51057f720247da4413d19897aea1c54b9c65";
    private static MarvelApi marvelApi;
    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(authInterceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MarvelApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        marvelApi = retrofit.create(MarvelApi.class);
    }

    public static MarvelApi getMarvelApi(){
        return marvelApi;
    }

    private Interceptor authInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            String currentTimestamp = new Timestamp(System.currentTimeMillis()).toString();
            Request request = chain.request();
            HttpUrl url = request.url()
                    .newBuilder()
                    .addQueryParameter(TIMESTAMP_KEY, currentTimestamp)
                    .addQueryParameter(APIKEY_KEY, PUBLIC_KEY)
                    .addQueryParameter(HASH_KEY, getMarvelAuthHash(currentTimestamp))
                    .build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        }
    };

    //Algorithm https://developer.marvel.com/documentation/authorization
    private String getMarvelAuthHash(String timestamp) {
        return getMD5Hash(timestamp + PRIVATE_KEY + PUBLIC_KEY);
    }

    private static String getMD5Hash(String stringToHash){
        byte[] bytes = stringToHash.getBytes(StandardCharsets.UTF_8);
        try { MessageDigest md = MessageDigest.getInstance(HASHING_ALGORITHM);
            bytes = md.digest(bytes);
            StringBuilder sb = new StringBuilder(2 * bytes.length);
            for(byte b : bytes){
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex){
            Log.e(TAG, "getMD5Hash: ", ex);
            return "";
        }
    }
}
