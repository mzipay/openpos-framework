package org.jumpmind.pos.core.device;

import java.io.Serializable;

public class DefaultDeviceResponse implements IDeviceResponse, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String requestId;
    private String deviceId;
    private String type;
    private String payload;
    
    @Override
    public String getRequestId() {
        return this.requestId;
    }
    
    @Override
    public void setRequestId(String sourceRequestId) {
        this.requestId = sourceRequestId;
    }
    
    @Override
    public String getDeviceId() {
        return this.deviceId;
    }
    
    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    @Override
    public void setType(String responseType) {
        this.type = responseType;
    }
    
    @Override
    public String getPayload() {
        return this.payload;
    }
    
    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }
    

}
