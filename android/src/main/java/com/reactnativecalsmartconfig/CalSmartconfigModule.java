package com.reactnativecalsmartconfig;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.ConnectivityManager;
import android.net.VpnTransportInfo;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.espressif.iot.esptouch.EsptouchTask;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

@ReactModule(name = CalSmartconfigModule.NAME)
public class CalSmartconfigModule extends ReactContextBaseJavaModule {
  public static final String NAME = "CalSmartconfig";

  Context context;

  public CalSmartconfigModule(ReactApplicationContext reactContext) {
    super(reactContext);
    context = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void getWifi(Promise promise) {
    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    WritableMap results = Arguments.createMap();

    Network[] networks = connManager.getAllNetworks();
    for (int i = 0; i < networks.length; i++) {
      NetworkCapabilities caps = connManager.getNetworkCapabilities(networks[i]);
      if (!caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
          && caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        final WifiInfo info = (WifiInfo) caps.getTransportInfo();
        System.out.println(info.getBSSID());
        System.out.println(info.getSSID());
        results.putString("bssid", info.getBSSID());
        results.putString("ssid", info.getSSID());
      }
    }

    promise.resolve(results);
  }

  @ReactMethod
  public void provision(String apSsid, String apBssid, String apPassword, Promise promise) {
    System.out.println("Provisioning smartconfig");
    // apBssid
    EsptouchTask task = new EsptouchTask(apSsid, apBssid, apPassword, context);
    task.executeForResult();
    promise.resolve(true);
  }
}
