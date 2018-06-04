package org.jumpmind.pos.context.service;

import java.util.Date;
import java.util.List;

import org.jumpmind.pos.context.model.ConfigModel;
import org.jumpmind.pos.context.model.ContextRepository;
import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.model.TagCalculator;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Transactional(transactionManager="contextTxManager")
public class GetConfigEndpoint {
    
    @Autowired
    private ContextRepository contextRepository;
    @Autowired
    private TagCalculator tagCalculator;
    @Autowired
    ContextService contextService;
    
    @Endpoint("/getConfig")
    public ConfigResult getConfig(
            @RequestParam(value="deviceId", defaultValue="*") String deviceId,
            @RequestParam(value="currentTime") Date currentTime,
            @RequestParam(value="configName", defaultValue="") String configName) {    
        
        ConfigResult result = new ConfigResult();
        
        DeviceResult deviceResult = contextService.getDevice(deviceId);
        if (!deviceResult.isSuccess()) {
            result.setResultStatus(deviceResult.getResultStatus());
            result.setResultMessage(deviceResult.getResultMessage());
            return result;
        }
        
        DeviceModel device = deviceResult.getDevice();
        List<ConfigModel> configs = contextRepository.findConfigs(currentTime, configName);
        ConfigModel config = null;
        if (configs != null) {
            config = (ConfigModel) tagCalculator.getMostSpecific(configs, device.getTags(), ContextRepository.getTagConfig());
            if (config != null) {
                result.setResultStatus(ServiceResult.RESULT_SUCCESS);
                result.setConfigName(configName);
                result.setConfigValue(config.getConfigValue());
                result.setConfig(config);
            }
        } 
        
        if (config == null) {
            result.setResultStatus(ServiceResult.RESULT_NOT_FOUND);
            result.setResultMessage("No config found for " + configName);            
        }
        
        return result;
    }

}
