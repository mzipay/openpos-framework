package org.jumpmind.pos.core.device;

public interface IDeviceRequest {
    public static final String DEFAULT_TYPE = "DeviceRequest";
    
    void setRequestId(String requestId);
    String getRequestId();
    void setDeviceId(String targetDeviceId);
    String getDeviceId();
    void setType(String requestType);
    String getType();
    void setPayload(String payload);
    String getPayload();
}
