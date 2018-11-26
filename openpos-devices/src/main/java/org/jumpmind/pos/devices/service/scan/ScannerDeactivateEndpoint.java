package org.jumpmind.pos.devices.service.scan;

import org.jumpmind.pos.devices.service.DeviceRequest;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScannerDeactivateEndpoint {

    @Autowired
    ScannerDeviceWrapper wrapper;


    @Endpoint("/scan/deactivate")
    public ServiceResult activate(DeviceRequest req) {
        return wrapper.deactivate(req);
    }
}
