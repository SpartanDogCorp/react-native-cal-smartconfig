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
import java.util.Observer;

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
import androidx.work.WorkInfo;

import androidx.lifecycle.LiveData;

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
  public void start(final ReadableMap options, final Promise promise) {
    String ssid = options.getString("ssid");
    String bssid = options.getString("bssid");
    String pass = options.getString("password");

    Data.Builder data = new Data.Builder();
    data.putString("ssid", ssid);
    data.putString("bssid", bssid);
    data.putString("password", pass);

    OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SmartConfigWorker.class)
        .setInputData(data.build())
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        .build();

    WorkManager
        .getInstance(context)
        .enqueue(work);

    LiveData<WorkInfo> info = WorkManager.getInstance(context).getWorkInfoByIdLiveData(work.getId());
    info.observe(context.getCurrentActivity(), new Observer<WorkInfo>() {
      @Override
      public void onChanged(WorkInfo workInfo) {
        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
          Data data = workInfo.getOutputData();
          int count = data.getInt("count");
          promise.resolve(count);
        }
      }
    });
  }
}
