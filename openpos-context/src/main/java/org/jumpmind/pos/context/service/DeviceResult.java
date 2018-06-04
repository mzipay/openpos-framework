package org.jumpmind.pos.context.service;

import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.service.ServiceResult;

public class DeviceResult extends ServiceResult {

    private String deviceId;
    private DeviceModel device;
    
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public DeviceModel getDevice() {
        return device;
    }
    public void setDevice(DeviceModel device) {
        this.device = device;
    }

}
