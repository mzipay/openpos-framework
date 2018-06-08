package org.jumpmind.pos.cache.service;

import org.jumpmind.pos.service.Endpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Transactional(transactionManager="cacheTxManager")
public class ClearAllCachesEndpoint {
    
    @Endpoint("/clearAllCaches")
    public CacheResult clearAllCaches(
            @RequestParam(value="deviceId") String deviceId) {
        return null;
    }  
        
}
