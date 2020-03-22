package com.dk.devlighttest.network;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class MyWebViewClient extends WebViewClient {
    private RelativeLayout progressBarContainer;

    public MyWebViewClient(RelativeLayout progressBarContainer) {
        this.progressBarContainer = progressBarContainer;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        showProgressBar(view);
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        showWebView(view);
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
