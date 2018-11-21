package org.jumpmind.pos.devices.service;

import java.io.Serializable;

public class DeviceRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    String deviceName;
    
    String profile;
    
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
    
    public void setProfile(String profile) {
        this.profile = profile;
    }
    
    public String getProfile() {
        return profile;
    }

}
