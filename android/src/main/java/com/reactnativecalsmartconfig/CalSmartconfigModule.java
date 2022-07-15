package com.reactnativecalsmartconfig;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.ConnectivityManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchListener;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import androidx.work.Data;
import androidx.work.WorkManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;

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
    WifiInfo info;

    Network[] networks = connManager.getAllNetworks();
    for (int i = 0; i < networks.length; i++) {
      NetworkCapabilities caps = connManager.getNetworkCapabilities(networks[i]);
      if (!caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
          && caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        try {
          info = (WifiInfo) caps.getTransportInfo();
          System.out.println(info.getBSSID());
          System.out.println(info.getSSID());
          results.putString("bssid", info.getBSSID());
          results.putString("ssid", info.getSSID());
        } catch (Exception e) {
          continue;
        }
      }
    }

    promise.resolve(results);
  }

  @ReactMethod
  public void start(final ReadableMap options, final Promise promise) {
    String ssid = options.getString("ssid");
    String bssid = options.getString("bssid");
    String pass = options.getString("password");

    int count = 0;

    Data.Builder data = new Data.Builder();
    data.putString("ssid", ssid);
    data.putString("bssid", bssid);
    data.putString("password", pass);

    OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SmartConfigWorker.class)
        .setInputData(data.build())
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        .build();

    ListenableFuture<Void> future = WorkManager
        .getInstance(context)
        .enqueue(work);

    WorkManager.getWorkInfoById(work.getId());

    // if (options.hasKey("count")) {
    // count = options.getInt("count");
    // }
    provision(ssid, bssid, pass, count, promise);
  }

  @ReactMethod
  public void provision(String apSsid, String apBssid, String apPassword, int count, Promise promise) {
    Logger log = Logger.getGlobal();
    log.info("Provisioning SmartConfig");

    EsptouchTask task = new EsptouchTask(apSsid, apBssid, apPassword, context);
    task.setEsptouchListener(new TouchListener(count, promise));

    try {
      task.executeForResults(count);
    } catch (RuntimeException e) {
      log.severe(e.getMessage());
    }

  }
}
