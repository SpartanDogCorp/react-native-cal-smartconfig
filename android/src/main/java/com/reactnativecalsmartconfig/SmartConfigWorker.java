package com.reactnativecalsmartconfig;

import androidx.work.Worker;
import androidx.work.Data;

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

    EsptouchTask task = new EsptouchTask(ssid, bssid, pass, this.context);
    task.setEsptouchListener(new TouchListener(count, promise));

    List<IEsptouchResult> results;

    try {
      results = task.executeForResults(0);
    } catch (RuntimeException e) {
      log.severe(e.getMessage());
      return Result.FAILURE;
    }

    Data out = new Data.Builder().putInt("count", results.size()).build();

    return Result.success(out);
  }
}
