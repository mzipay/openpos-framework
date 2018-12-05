package org.jumpmind.pos.devices.service;

import org.jumpmind.pos.devices.service.print.PrintRequest;
import org.jumpmind.pos.devices.service.print.PrinterSettingsRequest;
import org.jumpmind.pos.devices.service.print.PrinterSettingsResult;
import org.jumpmind.pos.devices.service.scan.ScannerConfigRequest;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Devices Service", description = "This service exposes device functionality for sharing devices across multiple clients")
@RestController
@RequestMapping("/devices")
public interface IDevicesService {

    @RequestMapping(value = "/scan/config", method = RequestMethod.PUT)
    @ResponseBody
    public ServiceResult configureScanner(@RequestBody ScannerConfigRequest req);

    @RequestMapping(value = "/print", method = RequestMethod.PUT)
    @ResponseBody
    public ServiceResult print(@RequestBody PrintRequest req);
    
    @RequestMapping(value = "/print/settings", method = RequestMethod.PUT)
    @ResponseBody
    public PrinterSettingsResult printerSettings(@RequestBody PrinterSettingsRequest req);

}
