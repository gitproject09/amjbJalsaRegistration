package org.amcbd.jalsa_registration.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.amcbd.jalsa_registration.R;
import org.amcbd.jalsa_registration.base_url.BaseUrl;

public class WebviewJalsaActivity extends BaseActivity {

    private WebView webView;

    private TextView tvToolbarTitle;
    private ImageView ivBack;
    private long mLastClickTime = 0;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        webView = findViewById(R.id.wvAboutUs);

        initializeToolbar("", true);
        tvToolbarTitle = findViewById(R.id.header_toolbar_title);
        tvToolbarTitle.setText("Registration Web App");
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        //webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLoadsImagesAutomatically(true);

        webView.setWebViewClient(new MyCustomWebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //webView.loadUrl(getResources().getString(R.string.about_us_url));
        webView.loadUrl(BaseUrl.regUrl);

    }

    private class MyCustomWebViewClient extends WebViewClient {

        /**
         * Override Url Loading when load an url in WebView Client
         *
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();

    }

    /**
     * When Back icon pressed or Menu clicked
     *
     * @param item
     * @return boolean true or false
     */
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
