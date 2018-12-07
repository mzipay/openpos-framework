package org.jumpmind.pos.devices.service.scan;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;

@Endpoint("/devices/scan/config")
public class ScannerConfigEndpoint {

    @Autowired
    ScannerDeviceWrapper wrapper;
    
    public ServiceResult configureScanner(ScannerConfigRequest req) {
        return wrapper.configure(req);
    }
}
