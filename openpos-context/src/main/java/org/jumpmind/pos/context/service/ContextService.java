package org.jumpmind.pos.context.service;

import java.util.Date;

import org.jumpmind.pos.service.EndpointDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/context")
public class ContextService {
    
    @Autowired
    private EndpointDispatcher endpointDispatcher;
    
    @RequestMapping("/config/{configName}")
    public ConfigResult getConfig(
            @RequestParam(value="deviceId", defaultValue="*") String deviceId,
            @RequestParam(value="currentTime") Date currentTime,
            @PathVariable(value="configName") String configName) {
        return endpointDispatcher.dispatch("/getConfig", deviceId, currentTime, configName);
    }
    
    @RequestMapping("/config/")
    public ConfigListResult getAllConfigs(
            @RequestParam(value="deviceId", defaultValue="*") String deviceId,
            @RequestParam(value="currentTime") Date currentTime) {
        return endpointDispatcher.dispatch("/getAllConfigs", deviceId, currentTime);
    }
    
    @RequestMapping("/device")
    public DeviceResult getDevice(
            @RequestParam(value="deviceId", defaultValue="*") String deviceId) {
        return endpointDispatcher.dispatch("/device", deviceId);
    }
        
    @RequestMapping("/sequence/{name}/next")
    public Long getNextSequence(@RequestParam("name") String name) {
        return endpointDispatcher.dispatch("/sequence/{name}/next", name);
    }
    
    
    
}
    
