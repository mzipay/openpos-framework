package org.jumpmind.pos.cache.service;

import org.jumpmind.pos.service.Endpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Transactional(transactionManager="cacheTxManager")
public class ClearValueEndpoint {
    
    @Endpoint("/clearValue")
    public CacheResult clearValue(
            @RequestParam(value="deviceId") String deviceId,        
            @RequestParam(value="cacheId") String cacheId,
            @RequestParam(value="key") String key) {
        return null;
    }

}
