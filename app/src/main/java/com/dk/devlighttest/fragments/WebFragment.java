package com.dk.devlighttest.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dk.devlighttest.R;
import com.dk.devlighttest.network.MyWebViewClient;

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
    private RelativeLayout progressBarContainer;

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
        setListeners();
        setupWebView();
    }

    private void initViewElements(View view){
        closeButton = view.findViewById(R.id.button_close);
        progressBarContainer = view.findViewById(R.id.webView_progress_bar);
        webView = view.findViewById(R.id.webView);
    }

    private void setListeners(){
        closeButton.setOnClickListener(onClickListener);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient(progressBarContainer));
        loadUrl(url);
    }

    public void loadUrl(String url){
        webView.loadUrl(url);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getFragmentManager().beginTransaction().detach(WebFragment.this).commit();
        }
    };
}
