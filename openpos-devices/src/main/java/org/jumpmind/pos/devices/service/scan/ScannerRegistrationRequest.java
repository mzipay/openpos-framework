package org.jumpmind.pos.devices.service.scan;

import org.jumpmind.pos.devices.service.DeviceRequest;

public class ScannerRegistrationRequest extends DeviceRequest {

    private static final long serialVersionUID = 1L;

    String callbackUrl;
    
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    public String getCallbackUrl() {
        return callbackUrl;
    }
}
