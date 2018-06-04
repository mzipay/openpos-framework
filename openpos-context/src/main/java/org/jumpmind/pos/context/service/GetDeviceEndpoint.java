package org.jumpmind.pos.context.service;

import org.jumpmind.pos.context.model.ContextRepository;
import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Transactional(transactionManager="contextTxManager")
public class GetDeviceEndpoint {
    
    @Autowired
    private ContextRepository contextRepository;    
    
    @Endpoint("/device")
    public DeviceResult getDevice(
            @RequestParam(value="deviceId", defaultValue="*") String deviceId) {
        
        DeviceResult result = new DeviceResult();
        DeviceModel device = contextRepository.findDevice(deviceId);
        
        if (device != null) {
            result.setResultStatus(ServiceResult.RESULT_SUCCESS);
            result.setDeviceId(device.getDeviceId());
            result.setDevice(device);
            return result;
        } else {
            result.setResultStatus(ServiceResult.RESULT_NOT_FOUND);
            result.setResultMessage("No device found for deviceId=" + deviceId);
        }
        
        return result;
    }
}
