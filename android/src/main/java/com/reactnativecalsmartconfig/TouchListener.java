package com.reactnativecalsmartconfig;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchListener;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;

public class TouchListener implements IEsptouchListener {
  Promise promise;
  ArrayList<IEsptouchResult> results;
  Integer count;

  TouchListener(int count, Promise promise) {
    this.promise = promise;
    this.count = count;
    this.results = new ArrayList<IEsptouchResult>(); // Arguments.createMap();
  }

  public void onEsptouchResultAdded(IEsptouchResult result) {
    Logger log = Logger.getGlobal();
    results.add(result);
    log.info(result.getInetAddress().toString());

    if (results.size() >= this.count) {
      Arguments.WriteableArray resultsArray = Arguments.createArray();
      Arguments.WriteableMap resultMap;
      for (int i = 0; i < results.size(); i++) {
        resultMap = Arguments.createMap();
        resultMap.putBool("success", result.isSuc());
        resultMap.putBool("cancelled", result.isCancelled());
        resultMap.putString("bssid", result.getBssid());
        resultMap.putString("address", result.getInetAddress.toString());
        resultsArray.pushMap(resultMap);
      }

      this.promise.resolve(resultsArray);
    }
  }
}
