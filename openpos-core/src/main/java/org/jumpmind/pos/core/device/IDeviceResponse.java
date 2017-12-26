package org.jumpmind.pos.core.device;

public interface IDeviceResponse {
    String getRequestId();
    void setRequestId(String sourceRequestId);
    String getDeviceId();
    void setDeviceId(String deviceId);
    String getType();
    void setType(String responseType);
    String getPayload();
    void setPayload(String payload);
}
