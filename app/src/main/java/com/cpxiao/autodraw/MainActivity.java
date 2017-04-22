package com.cpxiao.autodraw;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cpxiao.gamelib.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final String URL_AUTO_DRAW = "https://www.autodraw.com/";

    private WebView mWebView;
    private TextView mLoadErrorView;

    private boolean errorFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mWebView = (WebView) findViewById(R.id.web_view);
        showWebView();
        load();

        mLoadErrorView = (TextView) findViewById(R.id.load_error);
        mLoadErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideLoadErrorView();
                showWebView();
                load();
                loadAds();
            }
        });
        hideLoadErrorView();

        //        loadAds();
    }

    /**
     * 广告加载时机有待考察：在页面加载完成之后再加载广告
     */
    private void loadAds() {
        initFbAds50("132313060642929_132316300642605");
        //        initAdMobAds50("ca-app-pub-4157365005379790/8263455265");
        initAdMobAds50("ca-app-pub-4157365005379790/9597022469");
    }

    private void reloadAds() {
        //        initFbAds50("132313060642929_132316300642605");
        //        initAdMobAds50("ca-app-pub-4157365005379790/8263455265");
        //        initAdMobAds50("ca-app-pub-4157365005379790/9597022469");
    }

    private void load() {
//        loadAds();
        if (mWebView == null) {
            return;
        }
        try {
            errorFlag = false;
            mWebView.setWebViewClient(new MyWebViewClient());
            mWebView.setInitialScale(1);
            mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mWebView.setScrollbarFadingEnabled(false);

            WebSettings settings = mWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);

            mWebView.loadUrl(URL_AUTO_DRAW);
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "load: ");
                e.printStackTrace();
            }
        }
    }

    private void showLoadErrorView() {
        if (mLoadErrorView == null) {
            return;
        }
        mLoadErrorView.setVisibility(View.VISIBLE);
    }

    private void hideLoadErrorView() {
        if (mLoadErrorView == null) {
            return;
        }
        mLoadErrorView.setVisibility(View.INVISIBLE);
    }

    private void showWebView() {
        if (mWebView == null) {
            return;
        }
        mWebView.setVisibility(View.VISIBLE);
    }

    private void hideWebView() {
        if (mWebView == null) {
            return;
        }
        mWebView.setVisibility(View.INVISIBLE);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (DEBUG) {
                Log.d(TAG, "onPageStarted: ");
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (DEBUG) {
                Log.d(TAG, "onPageFinished: ");
            }
            if (errorFlag) {
                showLoadErrorView();
                hideWebView();
            } else {
                loadAds();
                hideLoadErrorView();
                showWebView();
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (DEBUG) {
                Log.d(TAG, "onReceivedHttpError: ");
            }
            errorFlag = true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (DEBUG) {
                Log.d(TAG, "onReceivedError: ..");
            }
            errorFlag = true;
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (DEBUG) {
                Log.d(TAG, "onReceivedError: ...");
            }
            super.onReceivedError(view, request, error);
            errorFlag = true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (DEBUG) {
                Log.d(TAG, "shouldOverrideUrlLoading: ");
            }
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            if (DEBUG) {
                Log.d(TAG, "onReceivedSslError: ");
            }
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }
}
