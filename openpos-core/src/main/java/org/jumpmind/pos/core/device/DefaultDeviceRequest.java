package org.jumpmind.pos.core.device;

import java.io.Serializable;

public class DefaultDeviceRequest implements IDeviceRequest, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String requestId;
    private String deviceId;
    private String type = DEFAULT_TYPE;
    private String payload;
    
    public DefaultDeviceRequest() {
    }

    public DefaultDeviceRequest(String requestId, String deviceId, String payload) {
        this.requestId = requestId;
        this.deviceId = deviceId;
        this.payload = payload;
    }
    
    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getRequestId() {
        return this.requestId;
    }

    @Override
    public void setDeviceId(String targetDeviceId) {
        this.deviceId = targetDeviceId;
    }

    @Override
    public String getDeviceId() {
        return this.deviceId;
    }

    @Override
    public void setType(String requestType) {
        this.type = requestType;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String getPayload() {
        return this.payload;
    }

}
