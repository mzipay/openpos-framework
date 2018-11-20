package org.jumpmind.pos.devices.service;

import java.io.Serializable;

public class ScannerRegistrationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    String callbackUrl;
    
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    public String getCallbackUrl() {
        return callbackUrl;
    }
}
