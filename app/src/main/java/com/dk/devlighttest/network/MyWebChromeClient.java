package com.dk.devlighttest.network;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class MyWebChromeClient extends WebChromeClient {
    private static final String LOADING_MESSAGE_TEMPLATE = "Loading...%d%%";
    private static final int MIN_PROGRESS_TO_SHOW_PAGE = 65;
    private LinearLayout progressBarContainer;
    private TextView currentLoadProgress;

    public MyWebChromeClient(LinearLayout progressBarContainer, TextView currentLoadProgress) {
        this.progressBarContainer = progressBarContainer;
        this.currentLoadProgress = currentLoadProgress;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        currentLoadProgress.setText(String.format(Locale.getDefault(), LOADING_MESSAGE_TEMPLATE, newProgress));
        if (newProgress > MIN_PROGRESS_TO_SHOW_PAGE){
            showWebView(view);
        } else {
            showProgressBar(view);
        }
    }

    private void showProgressBar(View view){
        view.setVisibility(View.INVISIBLE);
        progressBarContainer.setVisibility(View.VISIBLE);
    }

    private void showWebView(View view){
        progressBarContainer.setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);
    }

}
