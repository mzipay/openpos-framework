package org.jumpmind.pos.core.device;

import java.util.concurrent.TimeUnit;

public interface IDeviceRequest {
    public static final String DEFAULT_TYPE = "DeviceRequest";
    public static final int DEFAULT_TIMEOUT_MILLIS = 10000;
    
    void setRequestId(String requestId);
    String getRequestId();
    
    void setDeviceId(String targetDeviceId);
    String getDeviceId();
    
    void setType(String requestType);
    String getType();
    
    void setPayload(String payload);
    String getPayload();
    
    default void setTimeout(long timeoutMillis) {
        setTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
    }
    
    void setTimeout(long timeout, TimeUnit timeoutUnits);
    /**
     * Returns the timeout value in millisecs.
     * @return The timeout value in millisecs.
     */
    long getTimeout();
}
