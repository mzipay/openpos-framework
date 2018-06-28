package org.jumpmind.pos.ops.service;

import org.jumpmind.pos.service.EndpointDispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops")
public class OpsService {

    @Autowired
    private EndpointDispatcher endpointDispatcher;
    
    @RequestMapping("/deviceStatus/{deviceId}")
    public DeviceStatusResult getDeviceStatus(@RequestParam(value="deviceId", defaultValue="*") String deviceId) {
        return endpointDispatcher.dispatch("/deviceStatus", deviceId);
    }
    
    @RequestMapping("/deviceStatuses")
    public DeviceStatusResult getDeviceStatuses() {
        return endpointDispatcher.dispatch("/deviceStatus");
    }

}
