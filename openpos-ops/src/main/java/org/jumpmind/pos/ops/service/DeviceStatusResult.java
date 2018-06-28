package org.jumpmind.pos.ops.service;

import java.util.List;

import org.jumpmind.pos.ops.model.DeviceStatus;
import org.jumpmind.pos.service.ServiceResult;

public class DeviceStatusResult extends ServiceResult {

    List<DeviceStatus> deviceStatuses;
    
    public void setDeviceStatuses(List<DeviceStatus> deviceStatuses) {
        this.deviceStatuses = deviceStatuses;
    }
    
    public List<DeviceStatus> getDeviceStatuses() {
        return deviceStatuses;
    }
    
    public DeviceStatus getDeviceStatus(String deviceId) {
        for (DeviceStatus deviceStatus : deviceStatuses) {
            if (deviceStatus.getDeviceId().equals(deviceId)) {
                return deviceStatus;
            }
        }
        return null;
    }
}
