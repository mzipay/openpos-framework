package org.jumpmind.pos.core.device;

public interface IDeviceRequest {
    void setRequestId(String requestId);
    String getRequestId();
    void setDeviceId(String targetDeviceId);
    String getDeviceId();
    void setRequestType(String requestType);
    String getRequestType();
    void setPayload(String payload);
    String getPayload();
}
