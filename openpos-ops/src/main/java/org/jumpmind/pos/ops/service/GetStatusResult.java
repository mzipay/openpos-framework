package org.jumpmind.pos.ops.service;

import java.util.List;

import org.jumpmind.pos.ops.model.UnitStatus;
import org.jumpmind.pos.service.ServiceResult;

public class GetStatusResult extends ServiceResult {

    List<UnitStatus> deviceStatuses;
    
    public void setDeviceStatuses(List<UnitStatus> deviceStatuses) {
        this.deviceStatuses = deviceStatuses;
    }
    
    public List<UnitStatus> getDeviceStatuses() {
        return deviceStatuses;
    }
    
    public UnitStatus getDeviceStatus(String deviceId) {
        for (UnitStatus deviceStatus : deviceStatuses) {
            if (deviceStatus.getUnitId().equals(deviceId)) {
                return deviceStatus;
            }
        }
        return null;
    }
}
