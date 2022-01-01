package com.reactnativecalsmartconfig;

import androidx.annotation.NonNull;
import android.net.wifi.WifiManager;
import android.content.Context;

import com.espressif.iot.esptouch.EsptouchTask;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.Arguments;

@ReactModule(name = CalSmartconfigModule.NAME)
public class CalSmartconfigModule extends ReactContextBaseJavaModule {
  public static final String NAME = "CalSmartconfig";

  ReactApplicationContext context;

  public CalSmartconfigModule(ReactApplicationContext reactContext) {
    super(reactContext);
    context = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(int a, int b, Promise promise) {
    promise.resolve(a * b);
  }

  @ReactMethod
  public void provision(String apSsid, String apBssid, String apPassword, Promise promise) {
    System.out.println("Provisioning smartconfig");
    // apBssid
    EsptouchTask task = new EsptouchTask(apSsid, apBssid, apPassword, context);
    task.executeForResult();
    promise.resolve();
  }
}
