package org.jumpmind.pos.cache.service;

import org.jumpmind.pos.service.Endpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class GetValueEndpoint {
    
    @Endpoint("/getValue")
    public CacheResult getValue(
            @RequestParam(value="deviceId") String deviceId,
            @RequestParam(value="key") String key) {    
        return null;
    }

}
