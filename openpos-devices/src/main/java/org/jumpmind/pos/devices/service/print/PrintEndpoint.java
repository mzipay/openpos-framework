package org.jumpmind.pos.devices.service.print;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PrintEndpoint {
    
    @Autowired
    PrinterDeviceWrapper wrapper;

    @Endpoint("/print")
    public ServiceResult print(@RequestBody PrintRequest req) {
        return wrapper.print(req);
    }
}
