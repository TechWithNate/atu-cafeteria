package com.nate.atucafeteria.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nate.atucafeteria.R;

public class PaymentWebView extends AppCompatActivity {

    private WebView paymentWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_web_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        paymentWebView = findViewById(R.id.payment_webview);

        // Enable JavaScript for the WebView
        paymentWebView.getSettings().setJavaScriptEnabled(true);
        paymentWebView.setWebViewClient(new WebViewClient());

        // Get the authorization URL passed from the previous activity
        String authorizationUrl = getIntent().getStringExtra("AUTHORIZATION_URL");
        String access_code = getIntent().getStringExtra("access_code");



        // Load the URL into the WebView
        if (authorizationUrl != null && access_code != null) {
            paymentWebView.loadUrl(authorizationUrl);
            Toast.makeText(this, "Access Code: "+access_code, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Auth URL: "+authorizationUrl, Toast.LENGTH_SHORT).show();

        }

        // Handle SSL errors and page redirects
        paymentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Handle page load completion
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });



    }
}