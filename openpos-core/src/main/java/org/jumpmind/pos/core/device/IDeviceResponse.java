package org.jumpmind.pos.core.device;

public interface IDeviceResponse {
    void getRequestId(String sourceRequestId);
    String getDeviceId();
    String getResponseType();
    String getPayload();
}
