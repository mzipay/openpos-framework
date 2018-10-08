package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.model.Device;

public class SystemStatusScreen extends Screen {

    private static final long serialVersionUID = 1L;
    
    private List<Device> devices;
    private String deviceHeader;
    private String statusHeader;
    
    public SystemStatusScreen() {
        setScreenType(ScreenType.SystemStatus);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
    
    public void addDevice(Device device) {
        if(this.devices == null) {
            this.devices = new ArrayList<Device>();
        }
        this.devices.add(device);
    }

    public String getDeviceHeader() {
        return deviceHeader;
    }

    public void setDeviceHeader(String deviceHeader) {
        this.deviceHeader = deviceHeader;
    }

    public String getStatusHeader() {
        return statusHeader;
    }

    public void setStatusHeader(String statusHeader) {
        this.statusHeader = statusHeader;
    }
}