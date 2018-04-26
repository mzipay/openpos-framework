package org.jumpmind.pos.config.service;

import org.jumpmind.pos.config.model.ConfigModel;
import org.jumpmind.pos.service.ServiceResultImpl;

public class ConfigResult extends ServiceResultImpl {

    private String configName;
    private String configValue;
    private ConfigModel config;
    
    public String getConfigName() {
        return configName;
    }
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    public String getConfigValue() {
        return configValue;
    }
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    public ConfigModel getConfig() {
        return config;
    }
    public void setConfig(ConfigModel config) {
        this.config = config;
    }
    
}
