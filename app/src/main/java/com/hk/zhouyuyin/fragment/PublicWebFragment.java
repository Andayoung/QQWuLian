package com.hk.zhouyuyin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tencent.devicedemo.R;
import com.hk.zhouyuyin.util.ProgressWebView;


public class PublicWebFragment extends Fragment {

    private ProgressWebView webview;
    private String webUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.public_web_fragment_layout, null);
        webview = (ProgressWebView) view
                .findViewById(R.id.real_estate_advertitsing);
        web();
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        return view;
    }

    private void web() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(webUrl);
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public ProgressWebView getWebview() {
        return webview;
    }

}
