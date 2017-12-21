package org.jumpmind.pos.core.device;

public interface IDeviceResponse {
    String getRequestId();
    void setRequestId(String sourceRequestId);
    String getDeviceId();
    void setDeviceId(String deviceId);
    String getResponseType();
    void setResponseType(String responseType);
    String getPayload();
    void setPayload(String payload);
}
