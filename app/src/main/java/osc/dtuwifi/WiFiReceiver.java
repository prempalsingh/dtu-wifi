package osc.dtuwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WiFiReceiver extends BroadcastReceiver {

    public static final String TAG = "WiFiReceiver";

    public WiFiReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            String ssid = wifiManager.getConnectionInfo().getSSID();
            Log.d(TAG, ssid);
        }
    }
}
