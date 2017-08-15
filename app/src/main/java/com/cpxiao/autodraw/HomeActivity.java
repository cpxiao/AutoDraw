package com.cpxiao.autodraw;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpxiao.R;
import com.cpxiao.gamelib.activity.BaseActivity;

public class HomeActivity extends BaseActivity {

    private static final String URL_AUTO_DRAW = "https://www.autodraw.com/";

    private ProgressBar mProgressBar;
    private TextView mLoadErrorView;
    private Button mReloadAutoDrawBtn;
    private Button mOfflineDrawBtn;
    private WebView mWebView;
    private OfflineDrawView mOfflineDrawView;

    /**
     * 是否加载失败
     */
    private boolean isLoadWebViewError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
        mLoadErrorView = (TextView) findViewById(R.id.load_error);
        mReloadAutoDrawBtn = (Button) findViewById(R.id.reloadBtn);
        mOfflineDrawBtn = (Button) findViewById(R.id.offlineBtn);
        mWebView = (WebView) findViewById(R.id.web_view);
        mOfflineDrawView = (OfflineDrawView) findViewById(R.id.offline_draw_view);

        mProgressBar.setVisibility(View.VISIBLE);
        mLoadErrorView.setVisibility(View.GONE);
        mReloadAutoDrawBtn.setVisibility(View.GONE);
        mOfflineDrawBtn.setVisibility(View.GONE);
        mWebView.setVisibility(View.INVISIBLE);
        mOfflineDrawView.setVisibility(View.GONE);

        mReloadAutoDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mLoadErrorView.setVisibility(View.GONE);
                mReloadAutoDrawBtn.setVisibility(View.GONE);
                mOfflineDrawBtn.setVisibility(View.GONE);
                mWebView.setVisibility(View.INVISIBLE);
                mOfflineDrawView.setVisibility(View.GONE);

                loadWebView();
            }
        });
        mOfflineDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.GONE);
                mLoadErrorView.setVisibility(View.GONE);
                mReloadAutoDrawBtn.setVisibility(View.GONE);
                mOfflineDrawBtn.setVisibility(View.GONE);
                mWebView.setVisibility(View.INVISIBLE);
                mOfflineDrawView.setVisibility(View.VISIBLE);
            }
        });

        loadWebView();

        loadAds();
    }

    /**
     * 广告加载时机有待考察：在页面加载完成之后再加载广告
     */
    private void loadAds() {
        initFbAds50("132313060642929_132316300642605");
        initAdMobAds50("ca-app-pub-4157365005379790/9597022469");
        initAdMobAds50("ca-app-pub-4157365005379790/6503955260");
    }

    private void loadWebView() {
        if (mWebView == null) {
            return;
        }
        try {
            isLoadWebViewError = false;
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
                Log.d(TAG, "loadWebView: ");
                e.printStackTrace();
            }
        }
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (DEBUG) {
                Log.d(TAG, "onPageStarted: ");
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (DEBUG) {
                Log.d(TAG, "onPageFinished: ");
            }
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
            if (isLoadWebViewError) {
                mWebView.setVisibility(View.GONE);
                mLoadErrorView.setVisibility(View.VISIBLE);
                mReloadAutoDrawBtn.setVisibility(View.VISIBLE);
                mOfflineDrawBtn.setVisibility(View.VISIBLE);
            } else {
                mWebView.setVisibility(View.VISIBLE);
                mLoadErrorView.setVisibility(View.GONE);
                mReloadAutoDrawBtn.setVisibility(View.GONE);
                mOfflineDrawBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            if (DEBUG) {
                Log.d(TAG, "onReceivedHttpError: ");
            }
            super.onReceivedHttpError(view, request, errorResponse);
            isLoadWebViewError = true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (DEBUG) {
                Log.d(TAG, "onReceivedError: ..");
            }
            super.onReceivedError(view, errorCode, description, failingUrl);
            isLoadWebViewError = true;
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (DEBUG) {
                Log.d(TAG, "onReceivedError: ...");
            }
            super.onReceivedError(view, request, error);
            isLoadWebViewError = true;
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
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (DEBUG) {
                Log.d(TAG, "onReceivedSslError: ");
            }
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }
}
