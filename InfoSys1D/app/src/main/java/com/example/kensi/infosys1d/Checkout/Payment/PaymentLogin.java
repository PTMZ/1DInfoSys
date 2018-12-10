package com.example.kensi.infosys1d.Checkout.Payment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.kensi.infosys1d.Checkout.CheckoutMain;
import com.example.kensi.infosys1d.OCBI_API.Utils;
import com.example.kensi.infosys1d.R;

import java.util.HashMap;

public class PaymentLogin extends AppCompatActivity {

    private boolean accessed;
    public final static String SESSION_TOKEN = "Session Token";
    public final static String JUST_LOGIN = "JUST_LOGIN";
    private HashMap<String,String> login_params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_login);

        // Build the Bank Login URL
        login_params.put("client_id",getString(R.string.client_id));
        login_params.put("redirect_uri",getString(R.string.redirect_uri));
        String login_url = buildUrl(getString(R.string.login_url), login_params);

        // These webview settings are required for the login form to work
        final WebView myWebView = findViewById(R.id.webview);
        myWebView.clearCache(true);
        myWebView.clearHistory();
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDatabaseEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getUrl();

        myWebView.setWebViewClient(new WebViewClient() {
            WebResourceRequest req;
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Get request to get URL
                req = request;
                view.loadUrl(request.getUrl().toString());
                return false;
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onPageFinished(WebView view, String url){
                String currentURL = req.getUrl().toString();
                // Parse URL to get access_token
                HashMap<String, String> out = Utils.getParams(currentURL);
                if (out.containsKey("access_token") & !accessed){
                    accessed = true;
                    Intent i = new Intent(PaymentLogin.this, CheckoutMain.class);
                    i.putExtra(SESSION_TOKEN, out.get("access_token"));
                    i.putExtra(JUST_LOGIN, 1);
                    setResult(PaymentLogin.RESULT_OK,i);
                    finish();
                }
            }
        });

        myWebView.loadUrl(login_url);

    }

    public String buildUrl(String url, HashMap<String,String> parameters){
        String out = "";
        out += url + "?";
        for (String k: parameters.keySet()){
            out += k+"="+parameters.get(k)+"&";
        }
        out += "\b";
        return out;
    }

}