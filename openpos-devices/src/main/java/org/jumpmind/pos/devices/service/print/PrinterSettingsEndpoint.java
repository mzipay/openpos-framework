package org.jumpmind.pos.devices.service.print;

import org.jumpmind.pos.devices.model.PrinterSettingsRequest;
import org.jumpmind.pos.devices.model.PrinterSettingsResult;
import org.jumpmind.pos.service.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Endpoint("/devices/print/settings")
public class PrinterSettingsEndpoint {
    
    @Autowired
    PrinterDeviceWrapper wrapper;

    
    @ResponseBody
    public PrinterSettingsResult printerSettings(@RequestBody PrinterSettingsRequest req) {
        return wrapper.settings(req);
    }
}
