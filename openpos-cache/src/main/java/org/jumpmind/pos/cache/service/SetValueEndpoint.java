package org.jumpmind.pos.cache.service;

import org.jumpmind.pos.cache.service.impl.ICache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

//@Component
//@Scope("prototype")
public class SetValueEndpoint {
    
    @Autowired
    private CacheContainer cacheContainer;
    
//    @Endpoint("/setValue")
    public CacheResult setValue(
            @RequestParam(value="deviceId") String deviceId,        
            @RequestParam(value="cacheId") String cacheId,
            @RequestParam(value="key") String key,
            @RequestParam(value="value") Object value) {
        
        ICache cache = cacheContainer.getCache(cacheId);
        cache.setValue(key, value);
        
        
        return null;
    }

}
