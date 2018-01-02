package org.jumpmind.pos.core.device;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class DefaultDeviceRequest implements IDeviceRequest, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String requestId;
    private String deviceId;
    private String type = DEFAULT_TYPE;
    private String subType;
    private String payload;
    private long timeoutMillis = DEFAULT_TIMEOUT_MILLIS;
    
    public DefaultDeviceRequest() {
    }

    public DefaultDeviceRequest(String deviceId, String payload) {
        this(deviceId, null, payload);
    }

    public DefaultDeviceRequest(String deviceId, String subType, String payload) {
        this.deviceId = deviceId;
        this.subType = subType;
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
    public void setSubType(String subType) {
        this.subType = subType;
    }

    @Override
    public String getSubType() {
        return this.subType;
    }
    

    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String getPayload() {
        return this.payload;
    }

    @Override
    public void setTimeout(long timeout, TimeUnit timeoutUnits) {
        this.timeoutMillis = timeoutUnits.toMillis(timeout);
    }

    @Override
    public long getTimeout() {
        return this.timeoutMillis;
    }


}
