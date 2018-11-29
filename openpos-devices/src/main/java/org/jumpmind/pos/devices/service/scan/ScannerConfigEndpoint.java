package org.jumpmind.pos.devices.service.scan;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScannerConfigEndpoint {

    @Autowired
    ScannerDeviceWrapper wrapper;

    @Endpoint("/scan/config")
    public ServiceResult configureScanner(ScannerConfigRequest req) {
        return wrapper.configure(req);
    }
}
