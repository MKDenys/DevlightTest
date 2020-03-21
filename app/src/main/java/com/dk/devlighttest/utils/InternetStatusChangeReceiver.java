package com.dk.devlighttest.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dk.devlighttest.MainActivity;

public class InternetStatusChangeReceiver extends BroadcastReceiver {
    private MainActivity mainActivity;

    public InternetStatusChangeReceiver(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        mainActivity.internetStatusChangeReceiverCallBack(isConnected(context));
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
