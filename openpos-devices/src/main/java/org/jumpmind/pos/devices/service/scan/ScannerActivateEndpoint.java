package org.jumpmind.pos.devices.service.scan;

import org.jumpmind.pos.service.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScannerActivateEndpoint {

    @Autowired
    ScannerDeviceWrapper wrapper;


    @Endpoint("/scan/activate")
    public ScannerStatusResult activate(ScannerActivateRequest req) {
        return wrapper.activate(req);
    }
}
