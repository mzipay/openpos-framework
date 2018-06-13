package org.jumpmind.pos.cache.service;

import org.springframework.web.bind.annotation.RequestParam;

//@Component
//@Transactional(transactionManager="cacheTxManager")
public class ClearAllCachesEndpoint {
    
//    @Endpoint("/clearAllCaches")
    public CacheResult clearAllCaches(
            @RequestParam(value="deviceId") String deviceId) {
        return null;
    }  
        
}
