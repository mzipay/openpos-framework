package org.jumpmind.pos.ops.model;

import java.io.Serializable;

public class DeviceStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    
    enum Status { OPEN, CLOSED }
    
    String deviceId;
    
    Status status;
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Status getStatus() {
        return status;
    }
      

}
