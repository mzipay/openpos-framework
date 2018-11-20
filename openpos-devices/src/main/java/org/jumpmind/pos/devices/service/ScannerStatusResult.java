package org.jumpmind.pos.devices.service;

import org.jumpmind.pos.devices.model.ScannerMode;
import org.jumpmind.pos.service.ServiceResult;

public class ScannerStatusResult extends ServiceResult {

    private static final long serialVersionUID = 1L;
    
    ScannerMode mode;
    
    
    public void setMode(ScannerMode mode) {
        this.mode = mode;
    }
    
    public ScannerMode getMode() {
        return mode;
    }

}
