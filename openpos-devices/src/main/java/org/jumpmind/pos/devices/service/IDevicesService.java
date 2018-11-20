package org.jumpmind.pos.devices.service;

import org.jumpmind.pos.service.IService;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags="Devices Service", description="This service exposes device functionality for sharing devices across multiple clients")
@RestController
@RequestMapping("/devices")
public interface IDevicesService extends IService {

    @RequestMapping(value="/scan/register", method=RequestMethod.PUT)
    @ResponseBody
    public ScannerStatusResult registerForScanEvents(@RequestBody ScannerRegistrationRequest req);
    
    @RequestMapping(value="/scan/unregister", method=RequestMethod.PUT)
    @ResponseBody
    public ScannerStatusResult unRegisterForScanEvents(@RequestBody ScannerRegistrationRequest req);

    
    @RequestMapping(value="/scan/mode/{mode}", method=RequestMethod.PUT)
    @ResponseBody
    public ScannerStatusResult setScanMode(@RequestParam("mode")  String mode);
    
    
    @RequestMapping(value="/print", method=RequestMethod.PUT)
    @ResponseBody
    public ServiceResult print(@RequestBody PrintRequest req);
    

    
}
