package org.jumpmind.pos.devices.service;

import org.jumpmind.pos.devices.service.print.PrintRequest;
import org.jumpmind.pos.devices.service.scan.ScannerActivateRequest;
import org.jumpmind.pos.devices.service.scan.ScannerStatusResult;
import org.jumpmind.pos.service.IService;
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
public interface IDevicesService extends IService {

    @RequestMapping(value = "/scan/activate", method = RequestMethod.PUT)
    @ResponseBody
    public ScannerStatusResult activate(@RequestBody ScannerActivateRequest req);

    @RequestMapping(value = "/scan/deactivate", method = RequestMethod.PUT)
    @ResponseBody
    public ScannerStatusResult deactivate(@RequestBody DeviceRequest req);

    @RequestMapping(value = "/print", method = RequestMethod.PUT)
    @ResponseBody
    public ServiceResult print(@RequestBody PrintRequest req);

}
