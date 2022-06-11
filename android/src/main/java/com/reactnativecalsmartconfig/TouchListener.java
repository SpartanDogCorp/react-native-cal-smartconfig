package com.reactnativecalsmartconfig;

private class TouchListener implements IEsptouchListener {
  void onEsptouchResultAdded(IEsptouchResult result) {
    results.add(result);
    log.info(result);

    if (count = 0) {
      promise.resolve(results);
    }

    if (results.size() == count) {
      promise.resolve(results);
    }
  }
}
