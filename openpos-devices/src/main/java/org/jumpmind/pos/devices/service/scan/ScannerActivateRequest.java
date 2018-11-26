package org.jumpmind.pos.devices.service.scan;

import org.jumpmind.pos.devices.service.DeviceRequest;

public class ScannerActivateRequest extends DeviceRequest {

    private static final long serialVersionUID = 1L;

    ScannerMode mode;
    
    public void setMode(ScannerMode mode) {
        this.mode = mode;
    }
    
    public ScannerMode getMode() {
        return mode;
    }
    
}
