package com.stackio.jobio.officeApp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import utility.DotProgressBar;
import utility.Helper;
import utility.OptionMenu;

public class WebActivity extends AppCompatActivity {
    AppCompatActivity ac;
    DotProgressBar dotProgressBar;
    WebView web;
    Helper objHelper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ac = this;
        web = (WebView) findViewById(R.id.web);
        dotProgressBar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        web.getSettings().setJavaScriptEnabled(true); // enable javascript
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setBuiltInZoomControls(true);

        if (this.getIntent().getExtras() != null) {
            String url = this.getIntent().getStringExtra("url");
            String title = this.getIntent().getStringExtra("title");
            loadData(url, title);
        }
    }

    private void loadData(String url, String title) {
        objHelper.setActionBar(ac, title);
        web.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dotProgressBar.setVisibility(View.VISIBLE);
                web.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dotProgressBar.setVisibility(View.GONE);
                web.setVisibility(View.VISIBLE);
                String webUrl = web.getUrl();
            }
        });
        web.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                handleBackKey();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        handleBackKey();
    }

    private void handleBackKey() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            finish();
        }
    }

    public static String TALK_TO_US_URL = "https://tawk.to/chat/591b55684ac4446b24a6f766/default/?$_tawk_popout=true";
    public static String HELP_ARTICLE_URL = "https://jobio.io/article.php?articleid=Ng==";
    public static String ABOUT_US_URL = "https://jobio.io/about.php";
}

