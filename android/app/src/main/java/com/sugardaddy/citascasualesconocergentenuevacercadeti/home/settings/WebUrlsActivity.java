package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.settings;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.Internet.CheckServer;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;

import java.util.Objects;


public class WebUrlsActivity extends AppCompatActivity {

    public static String WEB_URL_TYPE = "WEB_URL_TYPE";

    private CheckServer mCheckServer;

    Toolbar toolbar;
    private WebView mWebView;

    ProgressBar loadingProgressBar;

    String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_urls);
        toolbar = findViewById(R.id.toolbar);

        webUrl = getIntent().getStringExtra(WEB_URL_TYPE);

        mCheckServer = findViewById(R.id.wait_for_internet_connection);

        setSupportActionBar(toolbar);

        switch (webUrl) {
            case Config.HELP_CENTER:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.help_center));
                break;
            case Config.PRIVACY_POLICY:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.privacy_policy));
                break;
            case Config.TERMS_OF_USE:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.terms_and_conditions));
                break;
        }

        Objects.requireNonNull(getSupportActionBar()).setElevation(4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mWebView = findViewById(R.id.WebView_Privacy);

        mWebView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

        mWebView.setWebViewClient(new MyWebViewClient());

        mWebView.loadUrl(webUrl);

        loadingProgressBar = findViewById(R.id.progressBar);

        mWebView.setWebChromeClient(new WebChromeClient() {

            // this will be called on page loading progress
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                loadingProgressBar.setProgress(newProgress);

                // hide the progress bar if the loading is complete

                if (newProgress == 100) {
                    loadingProgressBar.setVisibility(View.INVISIBLE);

                } else {
                    loadingProgressBar.setVisibility(View.VISIBLE);

                }

            }

        });

        mCheckServer.checkInternetConnection(new CheckServer.OnConnectionIsAvailableListener() {
            @Override
            public void onConnectionAvailable() {

                mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                findUserDialogs();
            }

            @Override
            public void onConnectionNotAvailable() {

            }
        });

    }
    public void findUserDialogs(){
        mCheckServer.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
