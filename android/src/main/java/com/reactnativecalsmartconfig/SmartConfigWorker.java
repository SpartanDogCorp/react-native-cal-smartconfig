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

import java.util.List;
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
import androidx.work.Worker;
import androidx.work.WorkInfo;
import androidx.work.WorkerParameters;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.ListenableWorker.Result;
import androidx.work.ListenableWorker;

public class SmartConfigWorker extends Worker {
  public SmartConfigWorker(
      @NonNull Context context,
      @NonNull WorkerParameters params) {
    super(context, params);
  }

  @Override
  public Result doWork() {
    Data data = this.getInputData();
    String ssid = data.getString("ssid");
    String bssid = data.getString("bssid");
    String pass = data.getString("password");

    Logger log = Logger.getGlobal();
    log.info("Provisioning SmartConfig");

    EsptouchTask task = new EsptouchTask(ssid, bssid, pass, this.getApplicationContext());
    // task.setEsptouchListener(new TouchListener(count, promise));

    List<IEsptouchResult> results;

    try {
      results = task.executeForResults(0);
    } catch (RuntimeException e) {
      log.severe(e.getMessage());
      return Result.failure();
    }

    Data out = new Data.Builder().putInt("count", results.size()).build();

    return Result.success(out);
  }
}
