package com.reactnativecalsmartconfig;

import java.util.ArrayList;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchListener;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

public class TouchListener implements IEsptouchListener {
  Promise promise;
  ArrayList<IEsptouchResult> results;
  Integer count;

  TouchListener(Integer count, Promise promise) {
    this.promise = promise;
    this.count = count;
    this.results = new ArrayList<IEsptouchResult>(); // Arguments.createMap();
  }

  public void onEsptouchResultAdded(IEsptouchResult result) {
    results.add(result);
    log.info(result);

    if (this.count = 0) {
      this.promise.resolve(results);
    }

    if (results.size() >= this.count) {
      this.promise.resolve(results);
    }
  }
}
