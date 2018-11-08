package org.jumpmind.boot;

import java.io.File;

public class BootConfig extends PropertiesFile {

    public BootConfig(File propertiesFile) {
        super(propertiesFile, true);
    }
    
    public String getDeviceId() {
        return properties.getProperty("device.id");
    }
    
    public String getUpdateUrl() {
        return properties.getProperty("update.url");
    }
    
    public String getAppDir() {
        return properties.getProperty("app.dir");
    }
    
    public boolean isAutoUpdateEnabled() {
        return Boolean.parseBoolean(properties.getProperty("auto.update.enabled", "true"));
    }
    
    public int getUpdateJobPeriodInMinutes() {
        return Integer.parseInt(properties.getProperty("update.job.period.minutes", "15"));
    }
}
