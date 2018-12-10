package org.jumpmind.pos.devices.model;

import java.io.Serializable;

public class DeviceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String deviceName;

    protected String profile;

    public DeviceRequest() {
    }

    public DeviceRequest(String profile, String deviceName) {
        this();
        this.deviceName = deviceName;
        this.profile = profile;
    }

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
