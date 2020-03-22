package com.dk.devlighttest.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dk.devlighttest.R;
import com.dk.devlighttest.network.MyWebChromeClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebFragment extends Fragment {
    private static final String URL_PARAM = "url";

    private String url;
    private WebView webView;
    private ImageButton closeButton;
    private LinearLayout progressBarContainer;
    private TextView currentLoadProgress;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url URL page for load.
     * @return A new instance of fragment WebFragment.
     */
    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(URL_PARAM, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(URL_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        setupWebView();
    }

    private void initViewElements(View view){
        progressBarContainer = view.findViewById(R.id.webView_progress_bar);
        webView = view.findViewById(R.id.webView);
        currentLoadProgress = view.findViewById(R.id.textView_loading_progress);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient(progressBarContainer, currentLoadProgress));
        loadUrl(url);
    }

    public void loadUrl(String url){
        webView.loadUrl(url);
    }
}
