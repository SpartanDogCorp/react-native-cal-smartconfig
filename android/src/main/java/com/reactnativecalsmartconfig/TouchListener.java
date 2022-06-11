package com.reactnativecalsmartconfig;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchListener;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;

public class TouchListener implements IEsptouchListener {
  Promise promise;
  WritableMap results;

  TouchListener(Promise promise) {
    this.promise = promise;
    this.results = Arguments.createMap();
  }

  public void onEsptouchResultAdded(IEsptouchResult result) {
    results.add(result);
    log.info(result);

    if (count = 0) {
      this.promise.resolve(results);
    }

    if (results.size() == count) {
      this.promise.resolve(results);
    }
  }
}
