package org.jumpmind.pos.config.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jumpmind.pos.service.EndpointDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigService {
    
    @Autowired
    private EndpointDispatcher endpointDispatcher;
    
    @RequestMapping("/authenticate")
    public ConfigResult getConfig(
            @RequestParam(value="region", defaultValue="*") String region,
            @RequestParam(value="country", defaultValue="*") String country,
            @RequestParam(value="state", defaultValue="*") String state,
            @RequestParam(value="store", defaultValue="*") String store,
            @RequestParam(value="nodeId", defaultValue="*") String nodeId,
            @RequestParam(value="deviceType", defaultValue="*") String deviceType,
            @RequestParam(value="storeType", defaultValue="*") String storeType,
            @RequestParam(value="departmentId", defaultValue="*") String departmentId,
            @RequestParam(value="brandId", defaultValue="*") String brandId,
            @RequestParam(value="configName", defaultValue="") String configName) {
        return endpointDispatcher.dispatch("/getConfig", region, country, state, store, nodeId, deviceType, storeType, departmentId, brandId, configName);
    }
    
    final String THIRTY_MINUTES= "1800000";
    
    @RequestMapping("/config/{configName}?asDate=true") 
    public Date getDate(@PathVariable String configName) {
        Configuration configuration = getConfig(configName);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddTHH:mm:SS");
        try {
            return format.parse(configuration.getConfigValue());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }      
    }
    
    @RequestMapping("/config/{configName}?asInt=true")
    public int getInt(@PathVariable String configName) {
        Configuration configuration = getConfig(configName);
        return Integer.valueOf(configuration.getConfigValue());
    }
    
    @RequestMapping("/config/{configName}?asLong=true")
    public long getLong(@PathVariable String configName) {
        Configuration configuration = getConfig(configName);
        return Long.valueOf(configuration.getConfigValue());
    }
    
    @RequestMapping("/config/{configName}")
    public Configuration getConfig(@PathVariable String configName) {
            
        switch (configName) {
            
            case "openpos.user.max.login.attempts":  return new Configuration(configName, "4");
            case "openpos.user.attempts.reset.period.ms":  return new Configuration(configName, THIRTY_MINUTES);
            default: 
                Configuration config = new Configuration("", "");
                config.setResultMessage("Cannot find configuration for name $configName");
                config.setResultStatus("FAILURE");
                return config;
        }
    }
}
