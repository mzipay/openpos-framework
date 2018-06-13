package org.jumpmind.pos.cache.service;

import org.springframework.web.bind.annotation.RequestParam;

//@Component
public class GetValueEndpoint {
    
//    @Endpoint("/getValue")
    public CacheResult getValue(
            @RequestParam(value="deviceId") String deviceId,
            @RequestParam(value="key") String key) {    
        return null;
    }

}
