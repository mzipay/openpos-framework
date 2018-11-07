package org.jumpmind.boot;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Boot {

    final Logger log = Logger.getLogger(Boot.class.getName());

    ScheduledExecutorService scheduler;

    public void start(String... args) {
        BootConfig bootConfig = new BootConfig(new File(System.getProperty("boot.config.file", "boot.conf")));
        AppConfig appConfig = new AppConfig(bootConfig);

        int updatePeriodInMinutes = bootConfig.getUpdateJobPeriodInMinutes();        
        if (updatePeriodInMinutes > 0 && bootConfig.isAutoUpdateEnabled()) {
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(new UpdateJob(bootConfig, appConfig), 0, updatePeriodInMinutes, TimeUnit.MINUTES);
        }
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }

    public static void main(String... args) {
        new Boot().start();
    }
}
