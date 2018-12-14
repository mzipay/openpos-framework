package org.jumpmind.pos.service.util;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Tax Service", description = "This service exposes endpoints to calculate tax")
@RestController
@RequestMapping("/markdown")
public interface IMarkdownTestService {

//    @RequestMapping(value="/calculate", method=RequestMethod.POST)
//    public TaxCalculationResponse calculateTax(TaxCalculationRequest request);
//    @RequestMapping(path = "/device/{deviceId}/config/{configName}", method = RequestMethod.GET)
//    public ConfigModel getConfig(@PathVariable("deviceId") String deviceId, @PathVariable("configName") String configName);
//
//    @RequestMapping(path="/device/{deviceId}/config/all", method = RequestMethod.GET)
//    public List<ConfigModel> getAllConfigs(@PathVariable("deviceId") String deviceId);
//
//    @RequestMapping(path="/device/{deviceId}", method = RequestMethod.GET)
//    public DeviceModel getDevice(@PathVariable("deviceId") String deviceId);
//
//    @RequestMapping(path="/device/{deviceId}/sequence/{sequenceName}/next", method = RequestMethod.GET)
//    public Long getNextSequence(@PathVariable("deviceId") String deviceId, @PathVariable("sequenceName") String sequenceName);
//
//    // TODO should this be in this module or should it be in ops?
//    @RequestMapping(path="/businessunit/{businessUnitId}", method = RequestMethod.GET)
//    public BusinessUnitModel getBusinessUnit(@PathVariable("businessUnitId") String businessUnitId);
//
//    @RequestMapping(path = "/device/{deviceId}/buttons/{buttonGroupName}", method = RequestMethod.GET)
//    public List<ButtonModel> getButtons(@PathVariable("deviceId") String deviceId, @PathVariable("buttonGroupName") String buttonGroupName);
//
//    @RequestMapping(path = "/device/{deviceId}/localtime", method = RequestMethod.GET)
//    public Date getLocalTime(@PathVariable("deviceId") String deviceId);
}
