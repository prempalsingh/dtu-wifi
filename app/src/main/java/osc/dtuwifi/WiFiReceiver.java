package osc.dtuwifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WiFiReceiver extends BroadcastReceiver {

    public static final String TAG = "WiFiReceiver";
    private WebView webView;

    public WiFiReceiver() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onReceive(final Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected() && info.getExtraInfo().equals("\"DTU-WiFI\"")) {
            //FIXME : this block gets executed twice
            webView = new WebView(context);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Log.d(TAG, "Page finished : " + url);
                    if(url.equals("https://10.50.0.100/connect/PortalMain")){
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                        String username = preferences.getString("username", null);
                        String password = preferences.getString("password", null);
                        if(username != null && password != null){
                            Log.d(TAG, "Logging in..");
                            webView.loadUrl("javascript: {" +
                                    "document.getElementById('LoginUserPassword_auth_username').value = '" + username + "';" +
                                    "document.getElementById('LoginUserPassword_auth_password').value = '" + password + "';" +
                                    "var button = document.getElementById('UserCheck_Login_Button');" +
                                    "button.click(); };");
                        }
                    }
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                    Log.d(TAG, "SSL Error : " + error.toString());
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.d(TAG, "Page loaded : " + url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.loadUrl("https://10.50.0.100/connect/PortalMain");
        }
    }
}
