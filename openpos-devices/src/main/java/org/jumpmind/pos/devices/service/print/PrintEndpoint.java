package org.jumpmind.pos.devices.service.print;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

@Endpoint("/devices/print")
public class PrintEndpoint {
    
    @Autowired
    PrinterDeviceWrapper wrapper;
    
    public ServiceResult print(@RequestBody PrintRequest req) {
        return wrapper.print(req);
    }
}
