package org.jumpmind.pos.cache.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheService {
    
//    @Autowired
//    private EndpointDispatcher endpointDispatcher;
//    
//    @RequestMapping
//    public CacheResult getValue(
//        @RequestParam(value="deviceId") String deviceId,
//        @RequestParam(value="cacheId") String cacheId,
//        @RequestParam(value="key") String key) {
//        return endpointDispatcher.dispatch("/getvalue", deviceId, key);
//    }
//
//    @RequestMapping("/setValue")
//    public CacheResult setValue(
//            @RequestParam(value="deviceId") String deviceId,        
//            @RequestParam(value="cacheId") String cacheId,
//            @RequestParam(value="key") String key,
//            @RequestParam(value="value") Object value) {
//        return endpointDispatcher.dispatch("/setValue", deviceId, cacheId, key, value);
//    }    
//    
//    @RequestMapping("/clearValue")
//    public CacheResult clearValue(
//            @RequestParam(value="deviceId") String deviceId,        
//            @RequestParam(value="cacheId", defaultValue="*") String cacheId,
//            @RequestParam(value="key") String key) {
//        return endpointDispatcher.dispatch("/clearValue", deviceId, cacheId, key);
//    }    
//
//    @RequestMapping("/clearCache")
//    public CacheResult clearCache(
//            @RequestParam(value="deviceId") String deviceId,        
//            @RequestParam(value="cacheId") String cacheId) {
//        return endpointDispatcher.dispatch("/clearCache", deviceId, cacheId);
//    }        
//
//    @RequestMapping("/clearAllCaches")
//    public CacheResult clearAllCaches(
//            @RequestParam(value="deviceId") String deviceId) {
//        return endpointDispatcher.dispatch("/clearAllCaches", deviceId);
//    } 
    
}
